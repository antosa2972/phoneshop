package com.es.core;

import com.es.core.model.phone.JdbcPhoneDao;
import com.es.core.model.phone.Phone;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/context/applicationContext-core-test.xml")

public class JdbcPhoneDaoIntTest {
    public static final String SQL_PHONES_TABLE_NAME = "phones";
    public static final long KEY = 1L;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcPhoneDao jdbcPhoneDao;

    private static Phone phone;

    @BeforeClass
    public static void setupBeforeClass() {
        phone = new Phone();
        phone.setBrand("Apple");
        phone.setModel("Iphone");
    }


    @Test
    public void saveTest() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SQL_PHONES_TABLE_NAME);
        jdbcPhoneDao.save(phone);
        int rowsCounter = JdbcTestUtils.countRowsInTable(jdbcTemplate, SQL_PHONES_TABLE_NAME);
        assertEquals(rowsCounter, 1);
    }

    @Test(expected = DuplicateKeyException.class)
    public void saveTestDuplicate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SQL_PHONES_TABLE_NAME);
        jdbcPhoneDao.save(phone);
        jdbcPhoneDao.save(phone);
        int rowsCounter = JdbcTestUtils.countRowsInTable(jdbcTemplate, SQL_PHONES_TABLE_NAME);
        assertEquals(rowsCounter, 1);
    }

    @Test
    public void getTest() {
        phone.setId(KEY);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SQL_PHONES_TABLE_NAME);
        jdbcPhoneDao.save(phone);
        Optional<Phone> optionalPhone = jdbcPhoneDao.get(KEY);
        assertNotNull(optionalPhone);
    }

    @Test
    public void findAllTest() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SQL_PHONES_TABLE_NAME);
        jdbcPhoneDao.save(phone);
        phone.setModel("New Model");
        jdbcPhoneDao.save(phone);
        assertNotNull(jdbcPhoneDao.findAll(0,Integer.MAX_VALUE));

    }


}