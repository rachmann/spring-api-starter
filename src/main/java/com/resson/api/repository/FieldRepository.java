package com.resson.api.repository;

import com.resson.api.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query("SELECT f FROM Field f where f.id = :id") 
    Field findFieldById(@Param("id") Long id);

    @Query("SELECT f FROM Field f where f.name = :name") 
    Field findFieldByName(@Param("name") String name);

}
