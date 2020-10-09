package controller;

import java.util.List;

import simu.Tulokset;
/**
 * 
 * IKontrolleri-rajapinnan tarkoitus on tarjota käyttöliittymälle ja moottorille metodeja, joita
 * ne voivat käyttää.
 * 
 * Käyttöliittymälle kontrolleri tarjoaa metodeja, joilla hakea tietokannasta tietoa, jota näyttää
 * käyttäjälle sekä metodeja, joilla kommunikoida tietokannan kanssa.
 * 
 * Moottorille kontrolleri tarjoaa metodeja, joilla moottori visualisoi omaa toimintaansa käyttäjälle
 * ja lopuksi näyttää lopputulokset, mitä moottori sai aikaan käyttäjän antamilla ehdoilla.
 * 
 * @author Eric Keränen
 * @version 1.6
 *
 */
public interface IKontrolleri {
	
	// Rajapinta, joka tarjotaan  käyttöliittymälle:
	
	public void kaynnistaSimulointi();
	public void nopeuta();
	public void hidasta();
	public void poistu();
	public List<Integer> getUniikitSimulointiID();
	public List<Tulokset> getTuloksetSimulointiID(int id);
	public List<Tulokset> getJakaumatSimulointiID(int id);
	public String getPaivaJaAika(int id);
	public void lisaaTietokantaan();
	public void poistaTietokannasta(int id);
	public void poistaKaikkiTietokannasta();
	public void suljeDAO();
	
	// Rajapinta, joka tarjotaan moottorille:
	
	public void visualisoiAsiakas(boolean rontgen, String nimi);
	public void kumitaAsiakas(String nimi);
	public void naytaTuloksetTableView(Tulokset tulos);
	public void naytaSimulointiaika();
	public List<Integer> annaJakaumat();
	public void aktivoiNappi();
}
