package com.example.dao;

import com.example.model.Entity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityDao {
    void save(Entity entity);
    Optional<Entity> findById(UUID id);
    List<Entity> findAll();
    void update(Entity entity);
    void delete(UUID id);
}