package simu;

import java.util.List;
/**
 * 
 * IMoottori-rajapinnan tarkoitus on tarjota kontrollerille metodeja, joilla
 * kontrolleri pystyy ohjaamaan moottorin toimintaa ja hakea tietokannasta tietoa
 * moottrin kautta sekä päivittää näytölle tietoja.
 * 
 * @author Eric Keränen
 * @version 2.0
 *
 */
public interface IMoottori {
		
	// Kontrolleri käyttää tätä rajapintaa
	
	public void setSimulointiaika(double aika);
	public void setViive(long aika);
	public long getViive();
	public double getAika();
	public int getPalvelupisteidenMaara();
	public Tulokset getTulos(int indeksi);
	
	public void suljeDAO();
	public List<Integer> getUniikitSimulointiID();
	public List<Tulokset> getTuloksetSimulointiID(int id);
	public List<Tulokset> getJakaumatSimulointiID(int id);
	public String getPaivaJaAika(int id);
	public void lisaaTietokantaan();
	public void poistaTietokannasta(int id);
	public void poistaKaikkiTietokannasta();
}
