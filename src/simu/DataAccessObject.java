package simu;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
/**
 * 
 * DataAccessObject-luokkaa tarvitaan koodin ja tietokannan välisen kommunikoimisen kannalta. DataAccessObject, tai
 * lyhyesti DAO, sisältää kaikki metodit, joilla kommunikoidaan tietokannan kanssa.
 * 
 * @author Eric Keränen
 * @version 1.8
 *
 */
public class DataAccessObject implements IDAO {

	private SessionFactory istuntoTehdas = null;
	
	/**
	 * DAO luokan konstruktori luo uuden istuntotehtaan ja yhteyden tietokantaan.
	 */
	public DataAccessObject() {
		try {
			istuntoTehdas = new Configuration().configure().buildSessionFactory();
		}catch(Exception e) {
			System.err.println("Istuntotehtaan luonti ei onnistunut: " + e.getMessage());
		}
	}

	/**
	 * Hakee tietokannasta kaikki uniikit simulointikertojen ID:t
	 * @return lista uniikeista simulointikertojen ID:stä
	 */
	@Override
	public List<Integer> getUniikitSimulointiID() {
		List<Tulokset> tulokset = new ArrayList<Tulokset>();
		List<Integer> uniikitID = new ArrayList<Integer>();
		try (Session istunto = istuntoTehdas.openSession()){
			tulokset = istunto.createQuery("from Tulokset").getResultList();
			for(Tulokset tulos : tulokset) {
				if(!uniikitID.contains(tulos.getSimulointiKerranID())) {
					uniikitID.add(tulos.getSimulointiKerranID());					
				}
			}
		}catch(Exception e) {
			System.err.println("Ongelma getUniikitSimulointiID-metodissa!");
			return null;
		}
		return uniikitID;
	}
	
	/**
	 * Hakee kaikki tulokset tietyn simulointikerran ID:n perusteella tietokannasta
	 * @param id on simulointikerran ID, jolla haetaan tietokkansta tuloksia
	 * @return lista tuloksista tietyn simulointikerran ID:n perusteella
	 */
	@Override
	public List<Tulokset> getTuloksetSimulointiID(int id){
		List<Tulokset> tulokset = new ArrayList<Tulokset>();
		try (Session istunto = istuntoTehdas.openSession()){
			tulokset = istunto.createQuery("from Tulokset where simuKerId = " + id).getResultList();
			if(tulokset.isEmpty()) { throw new Exception("Taulukko tyhjä!"); }
		}catch(Exception e) {
			System.err.println("Ongelma getTuloksetSimulointiID-metodissa!");
			return null;
		}
		return tulokset;
	}
	
	/**
	 * Hakee kaikki jakaumat tietyn simulointikerran ID:n perusteella tietokannasta
	 * ja vain yhdeltä riviltä, koska jakaumat ovat joka rivillä samat samalla
	 * simulointikerran ID:llä
	 * @param id on simulointikerran ID, jolla haetaan tietokkansta jakaumia
	 * @return lista jakaumista tietyn simulointikerran ID:n perusteella 
	 */
	@Override
	public List<Tulokset> getJakaumatSimulointiID(int id){
		List<Tulokset> tulokset = new ArrayList<Tulokset>();
		try (Session istunto = istuntoTehdas.openSession()){
			// Haetaan kassasta, koska kassa on aina olemassa simuloinnin aikana ja tarvitaan jakaumat vain yhdeltä riviltä
			tulokset = istunto.createQuery("from Tulokset where simuKerId = " + id + " and palvPisteNimi = 'kassa'").getResultList();
			if(tulokset.isEmpty()) { throw new Exception("Taulukko tyhjä!"); }
		}catch(Exception e) {
			System.err.println("Ongelma getJakaumatSimulointiID-metodissa!");
			return null;
		}
		return tulokset;
	}
	
	/**
	 * Hakee viimeisimmän simulointikerran ID:n tietokannasta, jotta uusien tietojen
	 * tallentaminen tietokantaan tulisi uudelle simulointikerta ID:lle eikä yli-
	 * kirjoittaisi vanhoja simulointituloksia
	 * @return viimeisimmän simulointikerran ID
	 */
	@Override
	public int getViimeisinSimulointiID() {
		int id = 0;
		List<Tulokset> tulokset = new ArrayList<Tulokset>();
		try (Session istunto = istuntoTehdas.openSession()){
			tulokset = istunto.createQuery("from Tulokset").getResultList();
			if(tulokset.isEmpty()) { throw new Exception("Taulukko tyhjä!"); }
			for(Tulokset tulos : tulokset) {
				if(tulos.getSimulointiKerranID() > id) { id = tulos.getSimulointiKerranID(); }
			}
		}catch(Exception e) {
			System.err.println("Ongelma getViimeisinSimulointiID-metodissa!");
			return 0;
		}
		return id;
	}
	
	/**
	 * Hakee tietokannasta päivän ja ajan tietyn simulointikerran ID:n perusteella ja vain
	 * yhdeltä riviltä, koska päivä ja aika ovat joka rivillä samat samalla simulointikerran ID:llä
	 * @param id on simulointikerran ID, jolla haetaan tietokkansta päivä ja aika
	 * @return simulointikerran numero, päivä ja aika stringinä
	 */
	@Override
	public String getPaivaJaAika(int id){
		List<Integer> nro = null;
		List<Date> date = null;
		List<Time> time = null;
		String tulos;
		try (Session istunto = istuntoTehdas.openSession()){
			nro = istunto.createQuery("select simuKerId from Tulokset where simuKerId = " + id + " and palvPisteNimi = 'kassa'").getResultList();
			date = istunto.createQuery("select paiva from Tulokset where simuKerId = " + id + " and palvPisteNimi = 'kassa'").getResultList();
			time = istunto.createQuery("select aika from Tulokset where simuKerId = " + id + " and palvPisteNimi = 'kassa'").getResultList();
			tulos = nro.get(0).toString() + "/" + date.get(0).toString() + " " + time.get(0).toString();
		}catch(Exception e) {
			System.err.println("Ongelma getPaivaJaAika-metodissa!" + e.getMessage());
			return null;
		}
		return tulos;
	}
	
	/**
	 * Tallentaa tietokantaan tulokset, jakaumat ja ajankohdan
	 * @param tulokset, jotka tallennetaan tietokantaan
	 * @return true, jos tallentaminen onnistui, muutoin false
	 */
	@Override
	public boolean createTulos(Tulokset tulokset) {
		Transaction transaktio = null;
		try (Session istunto = istuntoTehdas.openSession()){
			transaktio = istunto.beginTransaction();
			istunto.saveOrUpdate(tulokset);
			transaktio.commit();
		}catch(Exception e) {
			System.err.println("Ongelma createTulokset-metodissa!");
			if(transaktio != null) { transaktio.rollback(); }
			return false;
		}
		return true;
	}
	
	/**
	 * Poistaa tietokannasta tietyn simulointikerran tulokset.
	 * Metodi ensin hakee tietokannasta kaikki tulokset tietyllä simulointi-
	 * kerran ID:llä ja sitten poistaa ne, jos tietokannasta ei löydy mitään
	 * niin metodi heittää poikkeuksen.
	 * @param id, jolla poistetaan tietyn simulointikerran tulokset
	 * @return true, jos poistaminen onnistui, muutoin false
	 */
	@Override
	public boolean deleteTulos(int id) {
		Transaction transaktio = null;
		List<Tulokset> tulokset = new ArrayList<Tulokset>();
		try (Session istunto = istuntoTehdas.openSession()){
			transaktio = istunto.beginTransaction();
			tulokset = getTuloksetSimulointiID(id);
			if(tulokset.isEmpty()) { throw new Exception("Taulukko tyhjä!"); }
			for(Tulokset tulos : tulokset) {
				istunto.delete(tulos);
			}
			transaktio.commit();
		}catch(Exception e) {
			System.err.println("Ongelma deleteTulokset-metodissa!");
			if(transaktio != null) { transaktio.rollback(); }
			return false;
		}
		return true;
	}
	
	/**
	 * Poistaa tietokannasta kaikki tiedot. Jos tietoja ei ole tietokannassa,
	 * heittää metodi poikkeuksen.
	 * @return true, jos tietojen poistaminen onnistui, muutoin false
	 */
	@Override
	public boolean deleteTulokset() {
		Transaction transaktio = null;
		try (Session istunto = istuntoTehdas.openSession()){
			transaktio = istunto.beginTransaction();
			List<Tulokset> instanssit = readTulokset();
			if(instanssit.isEmpty()) { throw new Exception("Taulukko tyhjä!"); }
			for(Tulokset tulos : instanssit) {
				istunto.delete(tulos);
			}
			transaktio.commit();
		}catch(Exception e) {
			System.err.println("Ongelma deleteKaikiTulokset-metodissa!");
			if(transaktio != null) { transaktio.rollback(); }
			return false;
		}
		return true;
	}

	/**
	 * Lukee tietokannasta kaikki tulokset
	 * @return lista kaikista tietokannassa olevista tuloksista
	 */
	public List<Tulokset> readTulokset() {
		List<Tulokset> tulokset = new ArrayList<Tulokset>();
		try (Session istunto = istuntoTehdas.openSession()){
			tulokset = istunto.createQuery("from Tulokset").getResultList();
			if(tulokset.isEmpty()) { throw new Exception("Taulukko tyhjä!"); }
		}catch(Exception e) {
			System.err.println("Ongelma readTulokset-metodissa!");
			return null;
		}
		return tulokset;
	}

	/**
	 * Sulkee istuntotehtaan ja samalla yhteyden tietokantaan.
	 */
	@Override
	public void finalize() {
		try {
			if(istuntoTehdas != null) { istuntoTehdas.close(); }
		}catch(Exception e) {
			System.err.println("Istuntotehtaan sulkeminen ei onnistunut: " + e.getMessage());
			System.exit(0);
		}
	}
}
