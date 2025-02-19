package com.example.service;

import com.example.dao.EntityDao;
import com.example.dao.EntityDaoImpl;
import com.example.model.Entity;
import com.example.database.DatabaseHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EntityService {
    private final EntityDao dao = new EntityDaoImpl();
    private final DatabaseHelper dbHelper = new DatabaseHelper();

    public List<Entity> getAllEntities() {
        List<Entity> entities = dao.findAll();
        return entities;
    }


    public void updateEntity(Entity entity) {
        dao.update(entity);
    }

    public void deleteEntity(UUID id) {
        dao.delete(id);
    }
    public void addEntity(Entity entity) {
        if (entity.getName() == null || entity.getName().length() < 3 || entity.getName().length() > 50) {
            throw new IllegalArgumentException("Имя должно содержать от 3 до 50 символов и не может быть пустым.");
        }
        if (entity.getDescription() != null && entity.getDescription().length() > 255) {
            throw new IllegalArgumentException("Описание должно содержать до 255 символов и не может быть пустым.");
        }

        String sql = "INSERT INTO entities (name, description, created_at, updated_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = dbHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            dao.save(entity);

        }
        catch (SQLException e) {throw new RuntimeException(e); }

    }
}
