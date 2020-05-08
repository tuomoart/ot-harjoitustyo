package domain;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class ComplexNumberTest {
    private ComplexNumber cn;
    
    public ComplexNumberTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.cn = new ComplexNumber(0.3,0.2);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void squareWorks() {
        assertEquals(0.05,cn.square().getReal(),0.001);
        assertEquals(0.12,cn.square().getImg(),0.001);
    }
    
    @Test
    public void addWorks() {
        assertEquals(0.6,cn.add(cn).getReal(),0.001);
        assertEquals(0.4,cn.add(cn).getImg(),0.001);
    }
    
    @Test
    public void magnitudeWorks() {
        assertEquals(0.13,cn.magnitude(),0.001);
    }
}
