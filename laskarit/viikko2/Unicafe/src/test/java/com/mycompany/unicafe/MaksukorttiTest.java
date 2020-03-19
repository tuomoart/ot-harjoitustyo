package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void saldoAlussaOikein() {
        assertTrue(kortti.saldo()==10);
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(5);
        assertTrue(kortti.saldo()==15);
    }
    
    @Test
    public void saldoVaheneeOikeinJosRahaaTarpeeksi() {
        kortti.otaRahaa(5);
        assertTrue(kortti.saldo()==5);
    }
    
    @Test
    public void otaRahaaEiVieSaldoaNegatiiviseksi() {
        kortti.otaRahaa(15);
        assertTrue(kortti.saldo()==10);
    }
    
    @Test
    public void otaRahaaPalauttaaOikeinKunOnVaraa() {
        assertTrue(kortti.otaRahaa(5));
    }
    
    @Test
    public void otaRahaaPalauttaaOikeinJosEiVaraa() {
        assertTrue(!kortti.otaRahaa(15));
    }
    
    @Test
    public void toStringToimii() {
        assertEquals("saldo: 0.10",kortti.toString());
    }
}
