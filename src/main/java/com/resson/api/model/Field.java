package com.resson.api.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "fields")
public class Field extends BaseModel {
    
    private static final long serialVersionUID = 8558895620357545742L;

    @Id
    @GeneratedValue(generator = "field_id_generator")
    @SequenceGenerator(name = "field_id_generator", sequenceName = "field_id_sequence", initialValue = 1000)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    private String bucketPath;

    @Column(columnDefinition = "text")
    private String image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucketPath() {
        return bucketPath;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
