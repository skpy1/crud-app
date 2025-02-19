package com.example.dao;

import com.example.database.Database;
import com.example.model.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EntityDaoTest {
    private EntityDao dao = new EntityDaoImpl();

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM entities");
        }
    }

    @Test
    void testSaveAndFindAll() {
        Entity entity = new Entity();
        entity.setName("Тест Entity");
        entity.setDescription("Тест описание");
        dao.save(entity);
        List<Entity> entities = dao.findAll();
        assertEquals(1, entities.size());
        assertEquals("Тест Entity", entities.get(0).getName());
    }

    @Test
    void testUpdate() {
        Entity entity = new Entity();
        entity.setName("Старое Имя");
        dao.save(entity);
        entity.setName("Новое Имя");
        dao.update(entity);
        Entity updated = dao.findById(entity.getId()).get();
        assertEquals("Новое имя", updated.getName());
    }

    @Test
    void testDelete() {
        Entity entity = new Entity();
        dao.save(entity);
        dao.delete(entity.getId());
        assertTrue(dao.findById(entity.getId()).isEmpty());
    }
}