package com.es.core;

import com.es.core.model.phone.color.Color;
import com.es.core.model.phone.color.JdbcColorDAO;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/applicationContext-core-test.xml")

public class JdbcColorDaoIntTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcColorDAO jdbcColorDAO;

    private static Color color;

    @BeforeClass
    public static void setupBeforeClass() {
        color = new Color();
        color.setCode("white");
    }

    @Before
    public void setup() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "colors");
    }

    @Test
    public void saveTest() {
        jdbcColorDAO.save(color);
        int rows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "colors");
        assertEquals(rows, 1);
    }

    @Test
    public void getTest() {
        color.setId(1L);
        jdbcColorDAO.save(color);
        Optional<Color> colorOptional = jdbcColorDAO.get(color.getId());
        colorOptional.ifPresent(color1 -> assertEquals(color1.getId(), color.getId()));
    }


}