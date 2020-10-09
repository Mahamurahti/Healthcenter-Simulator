package simu;

import java.sql.Date;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

import controller.IKontrolleri;
import eduni.distributions.*;
/**
 * 
 * Moottori-luokka on koko simulaattorin ydin, jossa kaikki simulaattorin osat tekevät yhteistyötä.
 * Moottori luo palvelupisteet ja asiakkaita kiertämään palvelupisteissä.
 * 
 * @author Eric Keränen
 * @version 2.1
 */
public class Moottori extends Thread implements IMoottori{
	
	private IKontrolleri kontrolleri;
	
	// Data Access Object tietokantaan tallentamista varten
	private IDAO tuloksetDAO;
	
	private double simulointiaika = 0;
	private long viive = 0;

	private int laakarinHuoneidenMaara;
	private Palvelupiste[] palvelupisteet;
	private Kello kello;

	private Saapumisprosessi saapumisprosessi;
	private Tapahtumalista tapahtumalista;
	private Tulokset[] tulokset;
	
	/**
	 * Moottori-luokan konstruktori luo palvelupisteet, saapumisprosessin, tapahtumalistan ja alustaa
	 * kaiken generoimalla ensimmäisen tapahtuman valmiiksi tapahtumalistaan.
	 * 
	 * Palvelupisteiden luonnissa annetaan myös jokaiselle palvelupisteelle oma generaattori, joka generoi
	 * kyseiselle palvelupisteelle oman palveluajan. Jokaiselle generaattori voi käyttäjä antaa oman
	 * jakauman käyttöliittymässä, jota generaattori käyttää. Käyttäjä myös antaa lääkärinhuoneiden määrän.
	 * @param vastaanottoJakauma on käyttöliittymässä asetettu keskiarvo vastaanoton jakaumalle
	 * @param rontgenJakauma on käyttöliittymässä asetettu keskiarvo röntgensalin jakaumalle
	 * @param kassaJakauma on käyttöliittymässä asetettu keskiarvo kassan jakaumalle
	 * @param laakarienJakaumat on käyttöliittymässä asetettu keskiarvo lääkärinhuoneiden jakaumille
	 * @param saapumisJakauma on käyttöliittymässä asetettu keskiarvo saapumis jakaumalle
	 * @param huoneidenMaara on käyttöliittymässä asetettu määrä lääkärinhuoneille
	 * @param kontrolleri on välikäsi, joka välittää moottorin tarjoamia metodeja
	 */
	public Moottori(int vastaanottoJakauma, int rontgenJakauma, int kassaJakauma, int laakarienJakaumat, int saapumisJakauma, int huoneidenMaara, IKontrolleri kontrolleri){
		
		this.kontrolleri = kontrolleri;
		tuloksetDAO = new DataAccessObject();
		
		laakarinHuoneidenMaara = huoneidenMaara;
		palvelupisteet = new Palvelupiste[laakarinHuoneidenMaara + 3];
		palvelupisteet[0] = new Palvelupiste(new Normal(vastaanottoJakauma, vastaanottoJakauma / 2), this, TapahtumanTyyppi.DEP1, "Vastaanotto");				// Vastaanotto
		palvelupisteet[1] = new Palvelupiste(new Normal(rontgenJakauma, rontgenJakauma / 2), this, TapahtumanTyyppi.DEP5, "Röntgensali");						// Röntgensali
		palvelupisteet[2] = new Palvelupiste(new Normal(kassaJakauma, kassaJakauma / 2), this, TapahtumanTyyppi.DEP6, "Kassa");									// Kassa
		switch(laakarinHuoneidenMaara) {
			case 1: palvelupisteet[3] = new Palvelupiste(new Normal(laakarienJakaumat, laakarienJakaumat / 2), this, TapahtumanTyyppi.DEP2, "Lääkärinhuone 1");	// Lääkärinhuone 1
			 break;
			case 2: palvelupisteet[3] = new Palvelupiste(new Normal(laakarienJakaumat, laakarienJakaumat / 2), this, TapahtumanTyyppi.DEP2, "Lääkärinhuone 1");	// Lääkärinhuone 1
					palvelupisteet[4] = new Palvelupiste(new Normal(laakarienJakaumat, laakarienJakaumat / 2), this, TapahtumanTyyppi.DEP3, "Lääkärinhuone 2");	// Lääkärinhuone 2
			 break;
			case 3: palvelupisteet[3] = new Palvelupiste(new Normal(laakarienJakaumat, laakarienJakaumat / 2), this, TapahtumanTyyppi.DEP2, "Lääkärinhuone 1");	// Lääkärinhuone 1
					palvelupisteet[4] = new Palvelupiste(new Normal(laakarienJakaumat, laakarienJakaumat / 2), this, TapahtumanTyyppi.DEP3, "Lääkärinhuone 2");	// Lääkärinhuone 2
					palvelupisteet[5] = new Palvelupiste(new Normal(laakarienJakaumat, laakarienJakaumat / 2), this, TapahtumanTyyppi.DEP4, "Lääkärinhuone 3");	// Lääkärinhuone 3
			 break;
		}

		// Otetaan kello talteen muuttujaan
		kello = Kello.getInstance();
		
		saapumisprosessi = new Saapumisprosessi(new Negexp(saapumisJakauma, saapumisJakauma / 2), this, TapahtumanTyyppi.ARR1);
		tapahtumalista = new Tapahtumalista();	
		// Generoidaan ensimmäinen tapahtuma
		saapumisprosessi.generoiSeuraava();
	}
	
	/**
	 * Sulkee istuntotehtaan sekä yhteyden tietokantaan
	 */
	@Override
	public void suljeDAO() {
		tuloksetDAO.finalize();
	}
	
	/**
	 * Pyörittää koko ohjelmaa, kun kontrolleri aloittaa Moottori säikeen toiminnan.
	 * Säie pyörii niin kauan, kunnes se saavuttaa käyttäjän asettaman aikarajan, jonka
	 * jälkeen tulostetaan simulaattorin tulokset näytölle
	 */
	@Override
	public void run(){
		while (simuloidaan()){
			viive();
			kello.setAika(nykyaika());
			suoritaBTapahtumat();
			yritaCTapahtumat();
		}
		tulokset();
	}
	
	/**
	 * Tämä metodi suorittaa kaikki tapahtumat tapahtumalistasta, keiden ajat
	 * ovat samat kuin simulaattorin sen hetkinen aika. suoritaTapahtuma-metodissa
	 * on määritelty mitä tapahtumassa olevalle asiakkaalle tapahtuu, kun tapahtuma
	 * on suoritettu.
	 */
	private void suoritaBTapahtumat(){
		while (tapahtumalista.getSeuraavanAika() == kello.getAika()){
			// Poista listasta tapahtuma ja vie se suoritaTapahtuman parametriksi
			suoritaTapahtuma(tapahtumalista.poista());
		}
	}

	/**
	 * Tämä metodi yrittää suorittaa palvelupisteessä olevalle asiakkaalle palvelun.
	 * aloitaPalvelu-metodi asettaa palvelulle ajan ja luo uuden tapahtuman nykyajan
	 * ja palveluajan päähän (Kello.getInstance().getAika() + palveluaika).
	 */
	private void yritaCTapahtumat(){
		for (Palvelupiste palvelupiste: palvelupisteet){
			if (!palvelupiste.onVarattu() && palvelupiste.onJonossa()){
				palvelupiste.aloitaPalvelu();
			}
		}
	}

	/**
	 * Keskitetty algoritmi, joka käsittelee tapahtumat tapahtumalistasta ja siirtää asiakkaat
	 * seuraavien palvelupisteidne jonoihin riippuen ehdoista.
	 * @param tapahtuma on tapahtumalistasta seuraavaksi tapahtuva tapahtuma, joka poistetaan listasta ja tuodaan suoraan tänne
	 */
	private void suoritaTapahtuma(Tapahtuma tapahtuma){

		// Tähän muuttujaan tallennetaan aina palvelupisteestä poistettu asiakas
		Asiakas asiakas;
		switch (tapahtuma.getTyyppi()){
			case ARR1: palvelupisteet[0].lisaaJonoon(new Asiakas());				// Vastaanotto palvelupiste
				       saapumisprosessi.generoiSeuraava();							// Generoidaan seuraava tapahtuma
		        break;
			case DEP1: asiakas = palvelupisteet[0].otaJonosta();
					   if(asiakas.getTarvitseeRontgen()) {							// Tarvitseeko röntgen?
						   palvelupisteet[1].lisaaJonoon(asiakas);					// Jos kyllä, niin siirretään röntgensaliin
					   }else{														// Jos ei, niin siirretään lääkärille.
						   switch(laakarinHuoneidenMaara) {							// Riippuen montako lääkäriä on paikalla
							   case 1: palvelupisteet[3].lisaaJonoon(asiakas);		// siirto tapahtuu asianmukaisesti
								break;
							   case 2: int mahd2 = (int)(Math.random() * 2) + 1;
									   if(mahd2 == 2) { palvelupisteet[4].lisaaJonoon(asiakas); }
							   		   else { palvelupisteet[3].lisaaJonoon(asiakas); }
								break;
							   case 3: int mahd3 = (int)(Math.random() * 3) + 1;
									   if(mahd3 == 3) { palvelupisteet[5].lisaaJonoon(asiakas); }
					   		   		   else if(mahd3 == 2){ palvelupisteet[4].lisaaJonoon(asiakas); }
					   		   		   else { palvelupisteet[3].lisaaJonoon(asiakas); }
							   	break;
						   }
					   }
				break;
			case DEP2: asiakas = palvelupisteet[3].otaJonosta();					// Lääkäriltä asiakas
				   	   palvelupisteet[2].lisaaJonoon(asiakas); 						// siirretään kassalle
				break;  
			case DEP3: asiakas = palvelupisteet[4].otaJonosta();					// Lääkäriltä 2 asiakas
					   palvelupisteet[2].lisaaJonoon(asiakas);						// siirretään kassalle
				break;
			case DEP4: asiakas = palvelupisteet[5].otaJonosta();					// Lääkäriltä 3 asiakas
			   		   palvelupisteet[2].lisaaJonoon(asiakas);						// siirretään kassalle
		   		break;
			case DEP5: asiakas = palvelupisteet[1].otaJonosta();					// Röntgensalista asiakas
	   		   		   palvelupisteet[2].lisaaJonoon(asiakas); 						// siirretään kassalle
	   		   	break;																
			case DEP6: asiakas = palvelupisteet[2].otaJonosta();					// Otetaan kassalla asiakas
			   		   asiakas.setPoistumisaika(kello.getAika());					// jonosta ja asetetaan lopetus-
			   		   asiakas.raportti(); 											// aika ja tulostetaan raportti
		}	
	}
	
	/**
	 * Visualisoi asiakkaat näytölle palloina, joiden väri riippuu siitä tarvitseeko
	 * asiakas röntgeniä vai ei.
	 * @param rontgen määrittelee pallon värin, röntgen potilaat ovat vaalean-oransseja, muut mustia
	 * @param nimi määrittelee mihin kohtaa näyttöä pallo piirretään
	 */
	public void visualisoiAsiakkaat(boolean rontgen, String nimi) {
		kontrolleri.visualisoiAsiakas(rontgen, nimi);
	}
	
	/**
	 * Kumita asiakkaat näytöltä
	 * @param nimi määrittelee sen, mistä kohtaa asiakas kumitetaan
	 */
	public void kumitaAsiakas(String nimi) {
		kontrolleri.kumitaAsiakas(nimi);
	}

	/**
	 * Tämä metodi lisää tapahtumalistaan uuden tapahtuman.
	 * Saapumisprosessi kutsuu tätä metodia.
	 * @param tapahtuma on tapahtuma, joka lisätään listaan
	 */
	public void uusiTapahtuma(Tapahtuma tapahtuma){
		tapahtumalista.lisaa(tapahtuma);
	}

	/**
	 * Asettaa kellolle seuraavan tapahtuman aika
	 * @return seuraavan palvelun aika tapahtumalistasta
	 */
	public double nykyaika(){
		return tapahtumalista.getSeuraavanAika();
	}
	
	/**
	 * Tämä metodi palauttaa ehdon, jolla simulointi pyörii.
	 * @return true, jos simulaattorin aika on vähemmän kuin käyttäjän asettama aika, muutoin false
	 */
	private boolean simuloidaan(){
		Trace.out(Trace.Level.INFO, "Kello on: " + kello.getAika());
		kontrolleri.naytaSimulointiaika();
		return kello.getAika() < simulointiaika;
	}
	
	/**
	 * Hakee kellonajan pyöristettynä
	 */
	public double getAika() {
		return Math.floor(kello.getAika());
	}
	
	/**
	 * Tämä metodi asettaa simulaatiolle ajan, kuinka kauan simulaatio kestää
	 * @param aika määrittelee kuinka kauan simulaatio kestää (millisekunteja)
	 */
	@Override
	public void setSimulointiaika(double aika) {
		simulointiaika = aika;
	}
	
	/**
	 * Tämä metodi ilmoittaa mikä viive on nyt
	 * @return tämänhetkinen viive
	 */
	@Override
	public long getViive() {
		return viive;
	}

	/**
	 * Tämä metodi asettaa simulaatiolla viiveen, jonka tarkoituksena on
	 * hidatsaa simulaattorin toimintaa, jotta käyttäjä ehtii näkemään moottorin
	 * toimintaa reaaliajassa.
	 * @param viive määrittelee kuinka kauan simulaatio odottaa jokaisen
	 * 		  kierroksen jälkeen (millisekunteina)
	 */
	@Override
	public void setViive(long viive) {
		this.viive = viive;
	}
	
	/**
	 * Tämä metodi suorittaa viiveen simulaattorissa
	 */
	private void viive() {
		System.out.println ("Viive " + viive);
		try {
			sleep(viive);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tämä metodi tulostaa simulaation tuottamat tulokset konsoliin sekä näytölle, kun simulaatio on loppunut.
	 */
	public void tulokset(){
		tulokset = new Tulokset[palvelupisteet.length];
		
		System.out.println("\n-----------------------TULOKSET-----------------------\n");
		System.out.printf("%-32s %-8.2f %n", "Simulointi päättyi kello: ", kello.getAika());
		System.out.printf("%-32s %-8d %n", "Asiakkaita tuli järjestelmään: ", Asiakas.getAsiakkaidenLkm());
		System.out.printf("%-32s %-8d %n", "Asiakkaista röntgeniä tarvitsi: ", Asiakas.getTarvitsiRontgenLkm());
				
		for(int i = 0; i < palvelupisteet.length; i++) {
			System.out.printf("%-20s %-32s %-4d %-32s %-4.2f %-32s %-4.2f %-32s %-4.5f %-32s %-4.2f %-32s %-4d %-32s %-4.0f %n", 
							  "\n---" + palvelupisteet[i].getPalvelupisteenNimi() + "---",
							  "\nAsiakkaita: ", palvelupisteet[i].getPalvellutAsiakkaat(),
							  "\nAktiiviaika: ", palvelupisteet[i].getAktiiviAika(),
							  "\nKeskimääräinen palveluaika: ", palvelupisteet[i].getAktiiviAika() / palvelupisteet[i].getPalvellutAsiakkaat(),
							  "\nSuoritusteho: ", palvelupisteet[i].getPalvellutAsiakkaat() / kello.getAika() * 60, // Tätä ei tule tuloksiin
							  "\nKäyttöaste: ", palvelupisteet[i].getAktiiviAika() / kello.getAika() * 100,
							  "\nJonon max pituus: ", palvelupisteet[i].jononMaxPituus(),
							  "\nJonon keskimääräinen pituus: ", palvelupisteet[i].jononKeskimPituus());
			tulokset[i] = new Tulokset(palvelupisteet[i].getPalvelupisteenNimi(), 
									   palvelupisteet[i].getPalvellutAsiakkaat(), 
									   palvelupisteet[i].getAktiiviAika(),
									   palvelupisteet[i].getAktiiviAika() / palvelupisteet[i].getPalvellutAsiakkaat(),
									   palvelupisteet[i].getAktiiviAika() / kello.getAika() * 100, 
									   palvelupisteet[i].jononMaxPituus(), 
									   (int)palvelupisteet[i].jononKeskimPituus(),
									   kontrolleri.annaJakaumat().get(0),
					                   kontrolleri.annaJakaumat().get(1),
					                   kontrolleri.annaJakaumat().get(2),
						               kontrolleri.annaJakaumat().get(3),
						               kontrolleri.annaJakaumat().get(4),
						               kontrolleri.annaJakaumat().get(5),
						               luoDate(), luoTime());
			tulokset[i].setSimulointiKerranID(tuloksetDAO.getViimeisinSimulointiID() + 1);
			kontrolleri.naytaTuloksetTableView(tulokset[i]);
		}	
		kontrolleri.aktivoiNappi();
	}
	
	/**
	 * Luo tuloksille päivän, jolloin simulointitulos on saatu
	 * @return tämänhetkinen päivä
	 */
	private Date luoDate() {
		Calendar cal = Calendar.getInstance();
		java.util.Date curDate = cal.getTime();
		java.sql.Date date = new java.sql.Date(curDate.getTime());
		return date;
	}
	
	/**
	 * Luo tuloksille ajan, jolloin simulointitulos on saatu
	 * @return tämänhetkinen aika
	 */
	private Time luoTime() {
		Calendar cal = Calendar.getInstance();
		java.util.Date curDate = cal.getTime();
		Time time = new Time(curDate.getTime());
		return time;
	}
	
	public int getPalvelupisteidenMaara() {
		return this.palvelupisteet.length;
	}
	
	public Tulokset getTulos(int indeksi) {
		return tulokset[indeksi];
	}
	
	// ========================================================= //
	// TIETOKANTA METODEJA
	// ========================================================= //
	
	/**
	 * Hakee tietokannasta kaikki uniikit simulointikertojen ID:t
	 * @return lista uniikeista simulointikertojen ID:stä
	 */
	@Override
	public List<Integer> getUniikitSimulointiID(){
		return tuloksetDAO.getUniikitSimulointiID();
	}
	
	/**
	 * Hakee tietokannasta tulokset tietyllä simulointikerran ID:n perusteella
	 * @param id, jonka perusteella tietokanansta haetaan tietoja
	 * @return lista tietyn simulointikerran tuloksista
	 */
	@Override
	public List<Tulokset> getTuloksetSimulointiID(int id){
		return tuloksetDAO.getTuloksetSimulointiID(id);
	}
	
	/**
	 * Hakee tietokannasta jakaumat tietyllä simulointikerran ID:n perusteella
	 * @param id, jonka perusteella tietokanansta haetaan tietoja
	 * @return lista tietyn simulointikerran tuloksista
	 */
	@Override
	public List<Tulokset> getJakaumatSimulointiID(int id){
		return tuloksetDAO.getJakaumatSimulointiID(id);
	}
	
	/**
	 * Hakee tietokannasta päivän ja ajan tietyn simulointikerran ID:n perusteella
	 * @param id, jonka perusteella tietokanansta haetaan tietoja
	 * @return simulointikerran ID, päivä ja aika stringinä
	 */
	@Override
	public String getPaivaJaAika(int id){
		return tuloksetDAO.getPaivaJaAika(id);
	}

	/**
	 * Tallentaa tietokantaa jokaisen palvelupisteen tulokset
	 */
	@Override
	public void lisaaTietokantaan() {
		for(int i = 0; i < palvelupisteet.length; i++) {
			tuloksetDAO.createTulos(tulokset[i]);
		}
	}
	
	/**
	 * Poistaa tietokannasta tietoja tietyn simulointikerran ID:n perusteella
	 * @param id, jonka perusteella tietokanansta poistetaan tietoja
	 */
	@Override
	public void poistaTietokannasta(int id) {
		tuloksetDAO.deleteTulos(id);
	}
	
	/**
	 * Poistaa kaiken tiedon tietokannasta
	 */
	@Override
	public void poistaKaikkiTietokannasta() {
		tuloksetDAO.deleteTulokset();
	}
}