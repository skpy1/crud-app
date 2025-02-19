package com.example.dao;

import com.example.database.Database;
import com.example.model.Entity;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.format.DateTimeFormatter;

public class EntityDaoImpl implements EntityDao {
    @Override
    public void save(Entity entity) {
        String sql = "INSERT INTO entities (id, name, description, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getId().toString());
            pstmt.setString(2, entity.getName());
            pstmt.setString(3, entity.getDescription());
            pstmt.setString(4, entity.getCreatedAt().toString());
            pstmt.setString(5, entity.getUpdatedAt().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении записи: " + e.getMessage());
        }
    }


    @Override
    public Optional<Entity> findById(UUID id) {
        String sql = "SELECT * FROM entities WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Entity> findAll() {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT * FROM entities";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entities.add(mapRowToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return entities;
    }


    @Override
    public void update(Entity entity) {
        String sql = "UPDATE entities SET name = ?, description = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.setString(4, entity.getId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM entities WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    private Entity mapRowToEntity(ResultSet rs) throws SQLException {
        Entity entity = new Entity();

        String idString = rs.getString("id");
        if (idString != null) {
            entity.setId(UUID.fromString(idString));
        } else {
            throw new SQLException("ID cannot be null");
        }

        String name = rs.getString("name");
        entity.setName(name != null ? name : "");

        String description = rs.getString("description");
        entity.setDescription(description != null ? description : "");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        String createdAtString = rs.getString("created_at");
        if (createdAtString != null) {
            entity.setCreatedAt(LocalDateTime.parse(createdAtString, formatter));
        } else {
            throw new SQLException("CreatedAt cannot be null");
        }

        String updatedAtString = rs.getString("updated_at");
        if (updatedAtString != null) {
            entity.setUpdatedAt(LocalDateTime.parse(updatedAtString, formatter));
        } else {
            throw new SQLException("UpdatedAt cannot be null");
        }

        return entity;
    }

}