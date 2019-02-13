package com.resson.api.controller;

import com.resson.api.exception.ResourceNotFoundException;
import com.resson.api.model.Field;
import com.resson.api.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class FieldController {

    @Autowired
    private FieldRepository fieldRepository;

    @GetMapping("/fields")
    public Page<Field> getFields(Pageable pageable) {
        return fieldRepository.findAll(pageable);
    }


    @PostMapping("/fields")
    public Field createField(@Valid @RequestBody Field field) {
        return fieldRepository.save(field);
    }

    @PutMapping("/fields/{fieldId}")
    public Field updateField(@PathVariable Long fieldId,
                                   @Valid @RequestBody Field fieldRequest) {
        return fieldRepository.findById(fieldId)
                .map(field -> {
                    field.setName(fieldRequest.getField());
                    field.setImage(fieldRequest.getImage());
                    return fieldRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Field not found with id " + fieldId));
    }


    @DeleteMapping("/fields/{fieldId}")
    public ResponseEntity<?> deleteField(@PathVariable Long fieldId) {
        return fieldRepository.findById(fieldId)
                .map(field -> {
                    fieldRepository.delete(field);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Field not found with id " + fieldId));
    }
}
