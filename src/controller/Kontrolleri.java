package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import simu.Asiakas;
import simu.IMoottori;
import simu.Kello;
import simu.Moottori;
import simu.Tulokset;
/**
 * 
 * Kontrolleri-luokan tarkoitus on toimia välikätenä käyttöliittymän ja moottorin välillä.
 * Kontrolleri toimittaa kaikki moottorin tekemät tulokset käyttöliittymälle ja käyttö-
 * liittymä antaa moottorille ehtoje, joiden avulla moottorin tuottaa tuloksia.
 * 
 * @author Eric Keränen
 * @version 1.7
 *
 */
import view.ITerveyskeskusGUI;
public class Kontrolleri implements IKontrolleri {
	
	private IMoottori moottori;
	private ITerveyskeskusGUI gui;
	
	/**
	 * Kontrolleri-konstruktori alustaa käyttöliittymän kontrolleri käyttöön
	 * @param gui on käyttöliittymä
	 */
	public Kontrolleri(ITerveyskeskusGUI gui) {
		this.gui = gui;
	}
	
	/**
	 * Sulkee yhteyden tietokantaan ja sulkee ohjelman
	 */
	@Override
	public void poistu() {
		if(gui.getKaynnistetty()) {moottori.suljeDAO();}
		Platform.exit();
        System.exit(0);
	}
	
	// ========================================================= //
	// DAO:N OHJAUSTA
	// ========================================================= //
	
	/**
	 * Hakee moottorin kautta DAO:sta kaikki uniikit
	 * simulointikertojen id:t
	 * @return palauttaa kaikki uniikit simulointikertojen id:t
	 */
	@Override
	public List<Integer> getUniikitSimulointiID(){
		return moottori.getUniikitSimulointiID();
	}
	
	/**
	 * Hakee moottorin kautta kaikki tulokset DAO:sta
	 * tietyllä simulointikerran id:llä.
	 * @param id on sen simulointikerran id, josta halutaan hakea kaikki tulokset
	 * @return palauttaa kaikki tulokset tietyllä id:llä
	 */
	@Override
	public List<Tulokset> getTuloksetSimulointiID(int id){
		return moottori.getTuloksetSimulointiID(id);
	}
	
	/**
	 * Hakee moottorin kautta DAO:sta kaikki jakaumat, mitä on
	 * käytetty tietyllä simulointikerran id:llä
	 * @param id on sen simulointikerran id, josta halutaan hakea kaikki jakaumat
	 * @return palauttaa kaikki jakaumat tietyllä id:llä
	 */
	@Override
	public List<Tulokset> getJakaumatSimulointiID(int id){
		return moottori.getJakaumatSimulointiID(id);
	}
	
	/**
	 * Hakee moottorin kautta DAO:sta päivän ja ajan, mikä on
	 * asetettu tietylle simulointikerran id:lle
	 * @param id on sen simulointikerran id, josta halutaan hakea päivä ja aika
	 * @return palauttaa tietyn simulointikerran päivän ja ajan
	 */
	@Override
	public String getPaivaJaAika(int id){
		return moottori.getPaivaJaAika(id);
	}
	
	/**
	 * Lisää tietokantaan näytöllä olevat tiedot DAO:n kautta
	 */
	@Override
	public void lisaaTietokantaan() {
		moottori.lisaaTietokantaan();
	}
	
	/**
	 * Poistaa tietokannasta tietyn simulointikerran tiedot id:llä
	 * @param id on sen simulointikerran id, minkä tiedot halutaan poistaa
	 */
	@Override
	public void poistaTietokannasta(int id) {
		moottori.poistaTietokannasta(id);
	}
	
	/**
	 * Poistaa tietokannasta kaiken
	 */
	@Override 
	public void poistaKaikkiTietokannasta() {
		moottori.poistaKaikkiTietokannasta();
	}

	/**
	 * Sulkee yhteyden tietokantaan
	 */
	@Override
	public void suljeDAO() {
		moottori.suljeDAO();
	}
	
	// ========================================================= //
	// MOOTTORIN OHJAUSTA
	// ========================================================= //
	
	/**
	 * Käynnistää simuloinnin asettamalla globaalin kellon ajan nollaan,
	 * nollaamalla asiakkaiden staattiset muuttujat (uusien tuloksien laskemista varten),
	 * luomalla uuden moottorin, johon parametrina tuodaan käyttäjän asettamat
	 * jakaumat ja muut tiedot, asettamalla simulointiajan ja viiveen ja lopuksi
	 * tyhjentämällä näytön mahdollisten viime kertojen tuloksista.
	 * 
	 * Tämän kaiken jälkeen moottori käynnistetään säikeenä, jotta näytölle
	 * voidaan piirtää samanaikaisesti sitä, mitä moottorissa tapahtuu.
	 */
	@Override
	public void kaynnistaSimulointi() {
		
		Kello.getInstance().setAika(0.0);
		Asiakas.nollaaAsiakas();
		
		moottori = new Moottori(gui.getVastaanottoJakauma(),
								gui.getRontgenJakauma(),
								gui.getKassaJakauma(),
								gui.getLaakarienJakaumat(),
								gui.getSaapumisjakauma(),
								gui.getHuoneet(), this);
		moottori.setSimulointiaika(gui.getAika());
		moottori.setViive(gui.getViive());
		gui.getVisualisointi().tyhjennaNaytto();
		gui.tyhjennaTulokset();
		((Thread)moottori).start();
	}
	
	/**
	 * Simuloinnin ollessa valmis, moottori luo uudet tulokset ja näiden tuloksien
	 * kanssa tallennetaan myös jakaumat, jos käyttäjä haluaan lisätä kyseisen simulointikerran tietokantaan.
	 * @return palauttaa kaikki käyttäjän asettamat jakaumat käyttöliittymästä
	 */
	@Override
	public List<Integer> annaJakaumat(){
		List<Integer> jakaumat = new ArrayList<Integer>();
		jakaumat.add(gui.getVastaanottoJakauma());
		jakaumat.add(gui.getRontgenJakauma());
		jakaumat.add(gui.getKassaJakauma());
		jakaumat.add(gui.getLaakarienJakaumat());
		jakaumat.add(gui.getHuoneet());
		jakaumat.add(gui.getSaapumisjakauma());
		return jakaumat;
	}
	
	/**
	 * Hidastaa moottori säikeen kulkua
	 */
	@Override
	public void hidasta() {
		moottori.setViive((long)(moottori.getViive() + 1));
	}

	/**
	 * Nopeuttaa moottori säikeen kulkua
	 */
	@Override
	public void nopeuta() {
		if(moottori.getViive() == 0) {
			System.out.println("Viive ei saa olla negatiivinen!");
		}else {
			moottori.setViive((long)(moottori.getViive() - 1));			
		}
	}
	
	/**
	 * Aktivoi simuloinnin käynnistysnapin
	 */
	@Override
	public void aktivoiNappi() {
		gui.aktivoiNappi();
	}
	
	// ========================================================= //
	// VISUALISOINTI
	// ========================================================= //
	
	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska gui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen
	
	/**
	 * Tulostaa nykyisen simulointiajan käyttöliittymään, jotta käyttäjä tietää, kuinka kauan simulointi vielä pyörii
	 */
	@Override
	public void naytaSimulointiaika() {
		Platform.runLater(() -> gui.setSimulointiAika(moottori.getAika()));
	}
	
	/**
	 * Asetetaan simuloinnin päätyttyä tulokset näkyville käyttöliittymään
	 * @param tulos on yksittäisen palveluspisteen tulos
	 */
	@Override
	public void naytaTuloksetTableView(Tulokset tulos) {
		Platform.runLater(() -> gui.setTulokset(tulos));
	}

	/**
	 * Visualisoidaan asiakkaita näytöllä piirtämällä pallo palvelupisteen eteen
	 * kuvastamaan asiakasta, joka on jonossa
	 * @param rontgen määrittelee asiakkaan värin järjestelmässä, koska asiakkailla
	 * 		  on vain 10% mahdollisuus olla röntgenpotilaita, niin on ne hyvä
	 * 		  erottaa normaali potilaista.
	 * @param nimi on palvelupisteen nimi ja tätä nimeä tarvitaan määrittelemään
	 * 		  mihinkä kohtaa koordinaatistoa pallo piirretään
	 */
	@Override
	public void visualisoiAsiakas(boolean rontgen, String nimi) {
		Platform.runLater(new Runnable(){
			public void run(){
				gui.getVisualisointi().piirraAsiakas(rontgen, nimi);
			}
		});
	}
	
	/**
	 * Kumitetaan asiakkaita näytöltä, jotka eivät ole enään jonossa
	 * @param nimi on palvelupisteen nimi ja tätä nimeä tarvitaan määrittelemään
	 * 		  mistä kohtaa koordinaatistoa asiakas kumitetaan
	 */
	@Override
	public void kumitaAsiakas(String nimi) {
		Platform.runLater(new Runnable(){
			public void run(){
				gui.getVisualisointi().kumitaAsiakas(nimi);
			}
		});
	}
}
