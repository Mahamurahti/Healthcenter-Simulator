package testit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import simu.Asiakas;
import simu.Kello;

class AsiakasTest {

	private static Asiakas[] asTaulukko;
	
	@BeforeEach
	public void alkutoimet() {
		asTaulukko = new Asiakas[10];
		for(int i = 0; i < 10; i++) {
			asTaulukko[i] = new Asiakas();
		}
	}
	
	@AfterEach
	public void lopputoimet() {
		Asiakas.nollaaAsiakas();
	}
	
	@Test
	@DisplayName("Testaa asiakkaidenLkm")
	public void testAsiakkaidenLkm() {
		assertEquals(10, Asiakas.getAsiakkaidenLkm(), "Asiakkaiden lukumäärät eivät täsmää");
	}
	
	@Test
	@DisplayName("Testaa getTarvitseeRontgen")
	public void testGetTarvitseeRontgen() {
		Asiakas as = asTaulukko[0];
		assertFalse(as.getTarvitseeRontgen(), "Asiakkaan luu on rikki, vaikka ei pitäisi olla");
		as.rikoAsiakkaanLuu(100);
		assertTrue(as.getTarvitseeRontgen(), "Asiakkaan luu ei ole rikki, vaikka pitäisi olla");
	}
	
	@Test
	@DisplayName("Testaa rikoAsiakkaan luu yhdelle")
	public void testRikoAsiakkaanLuuYhdelle() {
		Asiakas as = asTaulukko[0];
		as.rikoAsiakkaanLuu(100);
		
		assertEquals(true, as.getTarvitseeRontgen(), "Asiakkaan luu ei ole rikki");
	}
	
	@Test
	@DisplayName("Testaa rikoAsiakkaan luu joka toinen")
	public void testRikoAsiakkaanLuuJokaToinen() {
		for(int i = 0; i < 10; i++) {
			if(i % 2 == 0) {
				asTaulukko[i].rikoAsiakkaanLuu(100);
			}
		}
		int rontgenPotilas = 0;
		for(int i = 0; i < 10; i++) {
			if(asTaulukko[i].getTarvitseeRontgen()) {
				rontgenPotilas++;
			}
		}
		
		assertEquals(5, rontgenPotilas, "Joka toisella luu ei ole rikki");
	}
	
	@Test
	@DisplayName("Testaa getPalveluPisteeseenTulo")
	public void testGetPalveluPisteeseenTulo() {
		for(int i = 0; i < 10; i++) {
			Kello.getInstance().setAika(i);
			asTaulukko[i].setPalveluPisteeseenTulo(Kello.getInstance().getAika());
		}
		
		assertEquals(0, asTaulukko[0].getPalveluPisteeseenTulo(), "Palvelupisteeseen tulo aika ei täsmää");
		assertEquals(4, asTaulukko[(asTaulukko.length - 1) / 2].getPalveluPisteeseenTulo(), "Palvelupisteeseen tulo aika ei täsmää");
		assertEquals(9, asTaulukko[asTaulukko.length - 1].getPalveluPisteeseenTulo(), "Palvelupisteeseen tulo aika ei täsmää");
	}
	
	@Test
	@DisplayName("Testaa getRikkomisenYritys")
	public void testGetRikkomisenYritys() {
		Asiakas as = asTaulukko[0];
		assertFalse(as.getRikkomisenYritys(), "Rikkomisen yritys pitäisi olla false");
		as.rikoAsiakkaanLuu(0);
		assertTrue(as.getRikkomisenYritys(), "Rikkomisen yrits pitäisi olla true");
		assertFalse(as.getTarvitseeRontgen(), "Mutta asiakkaan luu pitäis olla ehjä");
	}
	
}
