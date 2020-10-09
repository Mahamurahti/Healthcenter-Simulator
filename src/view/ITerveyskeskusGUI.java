package view;

import simu.Tulokset;
/**
 * 
 * ITerveyskeskusGUI-rajapinta tarjoaa metodeja kontrollerille, jotta kontrolleri voi
 * kommunikoida moottorin kanssa ja myös jotta moottori voi kommunikoida käyttöliittymän kanssa.
 * 
 * @author Eric Keränen
 * @version 1.7
 *
 */
public interface ITerveyskeskusGUI {
	
	// Kontrolleri tarvitsee syötteitä, jotka se välittää Moottorille
	
	public double getAika();
	public long getViive();
	public int getHuoneet();
	public int getSaapumisjakauma();
	public int getLaakarienJakaumat();
	public int getVastaanottoJakauma();
	public int getRontgenJakauma();
	public int getKassaJakauma();
	public boolean getKaynnistetty();
	public void aktivoiNappi();
	
	// Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa 
	
	public void setTulokset(Tulokset tulos);
	public void setSimulointiAika(double aika);
	public void tyhjennaTulokset();
	public Visualisointi getVisualisointi();

}
