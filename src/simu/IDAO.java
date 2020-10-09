package simu;

import java.util.List;
/**
 * 
 * IDAO-rajapinnan tarkoitus on tarjota moottorille (ja kontrollerille)
 * metodeja, joiden avulla moottori voi olla yhteistyössä tietokannan kanssa.
 * 
 * @author Eric Keränen
 * @version 1.8
 *
 */
public interface IDAO {
	public List<Integer> getUniikitSimulointiID();
	public List<Tulokset> getTuloksetSimulointiID(int id);
	public List<Tulokset> getJakaumatSimulointiID(int id);
	public int getViimeisinSimulointiID();
	public String getPaivaJaAika(int id); 
	public boolean createTulos(Tulokset tulokset);
	public boolean deleteTulos(int id);
	public boolean deleteTulokset();
	public void finalize();
}
