package domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dao.HistoryDao;
import dao.SQLiteHistoryDao;
import domain.Fractal;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tuomoart
 */
public class FractalTest {
    private Fractal fractal;
    private HistoryDao h;
    
    private int defaultIterations;
    
    public FractalTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));
            
            defaultIterations = Integer.valueOf(properties.getProperty("iterations"));
            
            h = new SQLiteHistoryDao(properties.getProperty("testHistoryDatabase"));
            this.fractal = new Fractal(h, properties);
        } catch (SQLException e) {
            
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void generateJuliaSetWorks() {
        boolean[][] correct = {{false,false,false,false,false,false,false},
                    {false,false,false,false,false,true,false},
                    {false,false,false,false,false,true,false},
                    {false,false,false,false,false,true,false},
                    {false,false,true,false,false,false,false},
                    {false,false,true,false,false,false,false},
                    {false,false,true,false,false,false,false}};
        
        fractal.setAreaHeight(7);
        fractal.setAreaWidth(7);
        boolean[][] returned = fractal.generateJuliaSet(7, 7);
        
        assertArrayEquals(correct,returned);
    }
    
    @Test
    public void isInSetIsCorrectWhenIsInSet() {
        
    }
    
    @Test
    public void setIterationsSetsTheIterations() {
        assertEquals(50, this.fractal.getIterations());
        this.fractal.setIterations(100);
        assertEquals(100,this.fractal.getIterations());
    }
    
    @Test
    public void setIterationsNotSetIfValueLessThanZero() {
        int originalValue = this.fractal.getIterations();
        this.fractal.setIterations(0);
        assertEquals(originalValue, this.fractal.getIterations());
        this.fractal.setIterations(-10);
        assertEquals(originalValue, this.fractal.getIterations());
    }
    
    @Test
    public void setXWorks() {
        this.fractal.setX(100);
        assertEquals(100,this.fractal.getX(),0.001);
    }
    
    @Test
    public void setYWorks() {
        this.fractal.setY(100);
        assertEquals(100,this.fractal.getY(),0.001);
    }
    
    @Test
    public void getRealWorks() {
        assertEquals(-0.223,this.fractal.getReal(),0.00001);
    }
    
    @Test
    public void getImgWorks() {
        assertEquals(0.745,this.fractal.getImg(),0.00001);
    }
    
    @Test
    public void setNumberWorks() {
        double r = 0.1;
        double i = -0.1;
        this.fractal.setNumber(r,i);
        assertEquals(r,this.fractal.getReal(),0.00001);
        assertEquals(i,this.fractal.getImg(),0.00001);
    }
    
    @Test
    public void getWidthWorks() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        int correct = Integer.valueOf(properties.getProperty("drawAreaSize"));
        
        assertEquals(correct, this.fractal.getWidth());
        
    }
    
    @Test
    public void undoResetsToDefaultsWhenNoHistory() throws SQLException {
        this.h.empty();
        
        this.fractal.setIterations(3);
        
        this.fractal.undo();
        
        assertEquals(defaultIterations, this.fractal.getIterations());
        //TODO ensure all parameters, not just this one
    }
}
