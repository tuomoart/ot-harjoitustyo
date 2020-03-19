/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

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
public class KassapaateTest {
    private Kassapaate paate;
    private Maksukortti kortti;
    
    public KassapaateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        paate = new Kassapaate();
        kortti = new Maksukortti(1000);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void alussaOikeaSaldo() {
        assertEquals(100000,paate.kassassaRahaa());
    }
    
    @Test
    public void alussaOikeaMaaraEdullisiaMyyty() {
        assertEquals(0,paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void alussaOikeaMaaraMaukkaitaMyyty() {
        assertEquals(0,paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullisenKateisostoKasvattaaKassanSaldoaJosMaksuRiittava() {
        paate.syoEdullisesti(400);
        assertEquals(100000+240,paate.kassassaRahaa());
    }
    
    @Test
    public void edullisenKateisostoPalauttaaOikeinJosMaksuRiittava() {
        assertEquals(160,paate.syoEdullisesti(400));
    }
    
    @Test
    public void edullisenKateisostoKasvattaaMyytyjenEdullistenMaaraa() {
        paate.syoEdullisesti(400);
        assertEquals(1,paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanKateisostoKasvattaaKassanSaldoaJosMaksuRiittava() {
        paate.syoMaukkaasti(500);
        assertEquals(100000+400,paate.kassassaRahaa());
    }
    
    @Test
    public void maukkaanKateisostoPalauttaaOikeinJosMaksuRiittava() {
        assertEquals(100,paate.syoMaukkaasti(500));
    }
    
    @Test
    public void maukkaanKateisostoKasvattaaMyytyjenMaukkaidenMaaraa() {
        paate.syoMaukkaasti(500);
        assertEquals(1,paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullisenKateisOstoEiMuutaKassaSaldoaJosMaksuEiRiita() {
        paate.syoEdullisesti(100);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void edullisenKateisOstoPalauttaaKaikenJosMaksuEiRiita() {
        assertEquals(100,paate.syoEdullisesti(100));
    }
    
    @Test
    public void maukkaanKateisOstoEiMuutaKassaSaldoaJosMaksuEiRiita() {
        paate.syoMaukkaasti(100);
        assertEquals(100000, paate.kassassaRahaa());
    }
    
    @Test
    public void maukkaanKateisOstoPalauttaaKaikenJosMaksuEiRiita() {
        assertEquals(100,paate.syoMaukkaasti(100));
    }
    
    @Test
    public void maukkaanKorttiostoOttaaRahaaKortiltaJosRahaaTarpeeksi() {
        paate.syoMaukkaasti(kortti);
        assertEquals(600,kortti.saldo());
    }
    
    @Test
    public void maukkaanKorttiostoPalauttaaTrueJosRahaaTarpeeksi() {
        assertEquals(true, paate.syoMaukkaasti(kortti));
    }
    
    @Test
    public void maukkaanKorttiostoKasvattaaMyytyjenMaukkaidenMaaraa() {
        paate.syoMaukkaasti(kortti);
        assertEquals(1,paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanKorttiostoEiMuutaMyytyjenMaaraaJosSaldoEiRiita() {
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        assertEquals(2,paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukkaanKorttiostoPalauttaaFalseJosSaldoEiRiita() {
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        assertEquals(false, paate.syoMaukkaasti(kortti));
    }
    
    @Test
    public void edullisenKorttiostoOttaaRahaaKortiltaJosRahaaTarpeeksi() {
        paate.syoEdullisesti(kortti);
        assertEquals(760,kortti.saldo());
    }
    
    @Test
    public void edullisenKorttiostoPalauttaaTrueJosRahaaTarpeeksi() {
        assertEquals(true, paate.syoEdullisesti(kortti));
    }
    
    @Test
    public void edullisenKorttiostoKasvattaaMyytyjenEdullistenMaaraa() {
        paate.syoEdullisesti(kortti);
        assertEquals(1,paate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullisenKorttiostoEiMuutaMyytyjenMaaraaJosSaldoEiRiita() {
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        paate.syoEdullisesti(kortti);
        assertEquals(2,paate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullisenKorttiostoPalauttaaFalseJosSaldoEiRiita() {
        paate.syoMaukkaasti(kortti);
        paate.syoMaukkaasti(kortti);
        assertEquals(false, paate.syoEdullisesti(kortti));
    }
    
    @Test
    public void rahaaLadattaessaKortinSaldoMuuttuu() {
        paate.lataaRahaaKortille(kortti, 100);
        assertEquals(1100, kortti.saldo());
    }
    
    @Test
    public void rahaaLadattaessaKassanSaldoKasvaaOikein() {
        paate.lataaRahaaKortille(kortti, 100);
        assertEquals(100100,paate.kassassaRahaa());
    }
    
    @Test
    public void rahaaLadattaessKortinSaldoEiVahene() {
        paate.lataaRahaaKortille(kortti, -100);
        assertEquals(1000, kortti.saldo());
    }
    
    @Test
    public void rahaaLadattaessaKassanSaldoEiKasvaJosLadattavaSummaNegatiivinen() {
        paate.lataaRahaaKortille(kortti, -100);
        assertEquals(100000,paate.kassassaRahaa());
    }
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
