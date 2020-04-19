/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import domain.Fractal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tuomoart
 */
public class SQLiteHistoryDaoTest {
    private SQLiteHistoryDao dao;
    
    public SQLiteHistoryDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.dao = new SQLiteHistoryDao("test.db");
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void constructorTest() {
        SQLiteHistoryDao testDao = new SQLiteHistoryDao("test.db");
    }
    
    @Test
    public void saveModificationSimpleWorks() {
        this.dao.saveModification("1,2,3,4");
        assertEquals("1,2,3,4",this.dao.getLatest());
    }
    
    @Test
    public void saveModificationFromFractalWorks() {
        Fractal f = new Fractal(this.dao);
        
        f.setIterations(35);
        f.setNumber(0.3,0.5);
        
        f.generateJuliaSet(10, 10);
        
        f.setIterations(100);
        f.setNumber(1,1);
        
        f.getLatestSettings();
        
        assertEquals(35,f.getIterations());
        assertEquals(0.3,f.getReal(),0.0001);
        assertEquals(0.5,f.getImg(),0.0001);
    }
    
    @Test
    public void undoSimpleWorks() {
        this.dao.saveModification("1,2,3,4");
        this.dao.saveModification("5,6,7,8");
        
        assertEquals("1,2,3,4", this.dao.undo());
    }
    
    @Test
    public void undoFromFractalWorks() {
        Fractal f = new Fractal(this.dao);
        
        f.setIterations(35);
        f.setNumber(0.3,0.5);
        
        f.generateJuliaSet(10, 10);
        
        f.setIterations(100);
        f.setNumber(1,1);
        
        f.generateJuliaSet(10, 10);
        
        f.undo();
        
        assertEquals(35,f.getIterations());
        assertEquals(0.3,f.getReal(),0.0001);
        assertEquals(0.5,f.getImg(),0.0001);
    }

}
