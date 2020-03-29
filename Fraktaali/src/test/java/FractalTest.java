/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import domain.Fractal;
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
    Fractal fractal;
    
    public FractalTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.fractal = new Fractal();
        this.fractal.setIterations(50);
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
    
    
}
