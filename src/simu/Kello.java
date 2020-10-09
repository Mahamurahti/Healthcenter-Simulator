package simu;
/**
 * Kello-luokkaa käytetään simulaattorin sisäisen ajan seuraamiseen. Luokka on singleton, joka tarkoittaa
 * että luokasta on olemasta yksi instanssi, jossa on yksi aika, joka on jokaiselle luokalle sama.
 * Tällä tavalla voidaan jokaisessa luokassa tarkastella aikaa ja aika on jokaiselle sama.
 * 
 * @author Eric Keränen
 * @version 1.0
 *
 */
public class Kello {

	private double aika;
	private static Kello instanssi;
	
	/**
	 * Kello-luokan konstruktori on private, sillä se luodaan heti
	 * ohjelman käynnistymisen ohella. Luokka on singleton, joten
	 * jokainen luokka pääsee siihen käsiksi kutsumalla .getInstance()
	 * metodia.
	 */
	private Kello(){
		aika = 0;
	}
	
	/**
	 * Tämä metodi palauttaa tämän luokan instanssin, jota käyttää kellon
	 * siirtämiseen
	 * @return tämän luokan instanssi
	 */
	public static Kello getInstance(){
		if (instanssi == null){
			instanssi = new Kello();	
		}
		return instanssi;
	}
	
	public void setAika(double aika){
		this.aika = aika;
	}

	public double getAika(){
		return aika;
	}
}
