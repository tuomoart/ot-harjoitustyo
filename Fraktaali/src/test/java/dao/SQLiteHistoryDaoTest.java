package dao;

import dao.SQLiteHistoryDao;
import domain.Fractal;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SQLiteHistoryDaoTest {
    private SQLiteHistoryDao dao;
    private Properties properties;
    
    public SQLiteHistoryDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        this.properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/testConfig.properties"));
        this.dao = new SQLiteHistoryDao(properties.getProperty("testHistoryDatabase"));
    }
    
    @After
    public void tearDown() throws Exception {
        this.dao.empty();
    }
    
    @Test
    public void constructorTest() {
        try {
            SQLiteHistoryDao testDao = new SQLiteHistoryDao(properties.getProperty("testHistoryDatabase"));
        } catch (SQLException e) {
            fail("Constructor threw an exception");
        }
    }
    
    @Test
    public void undoSimpleWorks() throws SQLException {
        this.dao.saveModification("1,2,3,4");
        this.dao.saveModification("5,6,7,8");
        
        assertEquals("1,2,3,4", this.dao.undo());
    }
    
    @Test
    public void undoFromFractalWorks() throws Exception {
        Fractal f = new Fractal(this.dao, properties);
        
        f.setIterations(35);
        f.setNumber(0.3,0.5);
        
        f.saveModifications();
        
        f.setIterations(100);
        f.setNumber(1,1);
        
        f.saveModifications();
        
        f.undo();
        
        assertEquals(35,f.getIterations());
        assertEquals(0.3,f.getReal(),0.0001);
        assertEquals(0.5,f.getImg(),0.0001);
    }
    
    @Test
    public void emptyWorks() throws SQLException {
        this.dao.saveModification("1");
        this.dao.saveModification("1");
        this.dao.empty();
        
        try {
            this.dao.undo();
            fail("Should throw an exception");
        } catch (SQLException e) {
            
        }
    }
}
