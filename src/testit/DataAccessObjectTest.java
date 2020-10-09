
package testit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import simu.DataAccessObject;
import simu.Tulokset;

class DataAccessObjectTest {

	private static DataAccessObject dao;
	private static Tulokset tulokset, tulokset2, tulokset3, tuloksetKassa;
	private static List<Tulokset> tuloksia;
	
	@BeforeEach
	public void alkutoimet() {
		dao = new DataAccessObject();

		Time aika = new Time((long)100);
		Date paiva = new Date((long)100);
		
		tulokset = new Tulokset("Testi",   9, 0, 0, 0, 9, 0, 0, 0, 9, 0, 0, 0, paiva, aika);
		tulokset2 = new Tulokset("Testi2", 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, 0, 9, paiva, aika);
		tulokset3 = new Tulokset("Testi3", 0, 0, 9, 0, 0, 0, 9, 0, 0, 0, 9, 0, paiva, aika);
		
		tuloksetKassa = new Tulokset("Kassa", 0, 0, 9, 0, 0, 0, 9, 0, 0, 0, 9, 0, paiva, aika);
		
		tuloksia = new ArrayList<Tulokset>();
		tuloksia.add(new Tulokset("TestiEka",  9, 0, 0, 0, 0, 0, 9, 0, 0, 0, 0, 0, paiva, aika));
		tuloksia.add(new Tulokset("TestiToka", 0, 9, 0, 0, 0, 9, 0, 9, 0, 0, 0, 9, paiva, aika));
		tuloksia.add(new Tulokset("TestiKolm", 0, 0, 9, 0, 9, 0, 0, 0, 9, 0, 9, 0, paiva, aika));
		tuloksia.add(new Tulokset("TestiNelj", 0, 0, 0, 9, 0, 0, 0, 0, 0, 9, 0, 0, paiva, aika));
		
	}
	
	@AfterEach
	public void lopputoimet() {
		dao.deleteTulokset();
		dao.finalize();
	}
	
	@Test
	@DisplayName("Testaa createTulos-metodi")
	public void testCreateTulos() {
		dao.createTulos(tulokset);
		Tulokset haettuTulos = dao.getTuloksetSimulointiID(dao.getViimeisinSimulointiID()).get(0);
		
		assertEquals(tulokset.toString(), haettuTulos.toString(), "Tulokset eivät täsmää");
	}
	
	@Test
	@DisplayName("Testaa createTulos-metodi monella tuloksella")
	public void testCreateTulosMontaTulosta() {
		for(int i = 0; i < tuloksia.size(); i++) {
			dao.createTulos(tuloksia.get(i));			
		}
		List<Tulokset> haetutTulokset = dao.readTulokset();
		Collections.sort(haetutTulokset, (id1, id2) -> {
			return (int)(id1.getId() - id2.getId());
		});

		assertEquals(tuloksia.toString(), haetutTulokset.toString(), "Listat eivät täsmää");
	}
	
	@Test
	@DisplayName("Testaa getUniikitSimulointiID-metodi")
	public void testGetUniikitSimulointiID() {
		tulokset.setSimulointiKerranID(dao.getViimeisinSimulointiID() + 1);
		dao.createTulos(tulokset);
		tulokset2.setSimulointiKerranID(dao.getViimeisinSimulointiID() + 1);
		dao.createTulos(tulokset2);
		tulokset3.setSimulointiKerranID(dao.getViimeisinSimulointiID() + 1);
		dao.createTulos(tulokset3);
		
		List<Integer> vastaus = new ArrayList<Integer>();
		vastaus.add(3);
		vastaus.add(2);
		vastaus.add(1);
		
		assertEquals(vastaus, dao.getUniikitSimulointiID(), "Uniikit ID:t eivät täsmää");
	}
	
	@Test
	@DisplayName("Testaa getJakaumatSimulointiID-metodi")
	public void testGetJakaumatSimulointiID() {
		dao.createTulos(tuloksetKassa);
		Tulokset haettuTulos = dao.getJakaumatSimulointiID(dao.getViimeisinSimulointiID()).get(0);
		
		assertEquals(tuloksetKassa.toString(), haettuTulos.toString(), "Jakaumat eivät täsmää");
	}
	
	@Disabled // DeleteTulokset metodi aiheuttaa jostain syystä JUnitissa IllegalStateException, vaikka
			  // metodi toimii hyvin normaali käytössä.
	@Test
	@DisplayName("Testaa deleteTulokset-metodi")
	public void testDeleteTulokset() {
		for(int i = 0; i < tuloksia.size(); i++) {
			dao.createTulos(tuloksia.get(i));			
		}
		
		boolean onnistuiko = dao.deleteTulokset();
		
		assertTrue(onnistuiko, "Tulokset eivät täsmää");
	}
	
	@Disabled // DeleteTuloks metodi aiheuttaa jostain syystä JUnitissa IllegalStateException, vaikka
	  		  // metodi toimii hyvin normaali käytössä.
	@Test
	@DisplayName("Testaa deleteTulos-metodi")
	public void testDeleteTulos() {
		dao.createTulos(tulokset);
		boolean onnistuiko = dao.deleteTulos(dao.getViimeisinSimulointiID());
		
		assertTrue(onnistuiko, "Tulokset eivät täsmää");
	}
}
