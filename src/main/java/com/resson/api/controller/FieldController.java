package com.resson.api.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.storage.Blob;
import com.resson.api.exception.ResourceNotFoundException;
import com.resson.api.model.Field;
import com.resson.api.repository.FieldRepository;
import com.resson.api.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

@RestController
public class FieldController {

    final Logger logger = LoggerFactory.getLogger(com.resson.api.controller.FieldController.class);

    @Autowired
    private FieldRepository fieldRepository;

    private StorageService storageService;

    @GetMapping("/fields")
    public Page<Field> getFields(Pageable pageable) {
        Page<Field> fs = fieldRepository.findAll(pageable);
        storageService = new StorageService();
        storageService.init();

        for (Field ff : fs) {
            // create signed URL for each field image
            Blob b = storageService.getBlob(ff.getName());
            URL u = b.signUrl(3600, TimeUnit.SECONDS);
            ff.setBucketPath(u.toString());
        }
        return fs;

    }

    @GetMapping("/fields/{fieldname}")
    public Field getField(String fieldName) {
       
        Field f = null;

        try{

            f = fieldRepository.findFieldByName(fieldName);

            storageService = new StorageService();
            storageService.init();

            String base64Image = storageService.getBase64FromBlob(fieldName);
            if(f.getImage() != base64Image)
            {
              logger.warn("Field image does not match database record");
            }

        } catch (Exception ex){
            logger.error("Error getting field: {}", ex.getMessage());
        } finally{

        }

        return f;
        
    }

    @PostMapping("/fields")
    public Field createField(@Valid @RequestBody Field field) {
        Field f = null;
        try {
            if(field.getCreatedAt() == null){
                Date d = new Date();
                field.setCreatedAt(d);
            }
            storageService = new StorageService();
            storageService.init();
            storageService.setBlobFromBase64(field.getName(), field.getImage());
            f = fieldRepository.save(field);

        } catch (Exception e) {
            logger.error("Error getting field: {} : {}", e.getMessage(), e.getStackTrace());
        }

        return f;

    }

    @PutMapping("/fields/{fieldId}")
    public Field updateField(@PathVariable Long fieldId, @Valid @RequestBody Field fieldRequest) {
        Field f = null;
        try {
            f = fieldRepository.findById(fieldId).map(field -> {
                field.setName(fieldRequest.getName());
                field.setImage(fieldRequest.getImage());
                field.setUpdatedAt(fieldRequest.getUpdatedAt() == null ? new Date() : fieldRequest.getUpdatedAt());

                return fieldRepository.save(field);
            }).orElseThrow(() -> new ResourceNotFoundException("Field not found with id " + fieldId));
        } catch (Exception e) {
            logger.error("Error getting field: {} : {}", e.getMessage(), e.getStackTrace());
        }
        return f;
    }

    @DeleteMapping("/fields/{fieldId}")
    public ResponseEntity<?> deleteField(@PathVariable Long fieldId) {
        return fieldRepository.findById(fieldId).map(field -> {
            fieldRepository.delete(field);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Field not found with id " + fieldId));
    }
}
