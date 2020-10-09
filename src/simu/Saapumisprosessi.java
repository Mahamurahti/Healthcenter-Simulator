package simu;
import eduni.distributions.*;
/**
 * 
 * Saapumisprosessi-luokan tarkoitus on luodan järjestelmään tapahtumia tapahtumalistaan,
 * joihin asiakkaat lopulta menevät.
 * 
 * @author Eric Keränen
 * @version 1.0
 *
 */
public class Saapumisprosessi {
	private Moottori moottori;
	private ContinuousGenerator generaattori;
	private TapahtumanTyyppi tyyppi;
	
	/**
	 * Saapumisprosessi-luokan konstruktori alustaa luokassa olevat muuttujat
	 * @param moottori on koko ohjelman pyörittäjä
	 * @param generaattori on satunnaislukugeneraattori
	 * @param tyyppi on tapahtuman tyyppi (esim. ARR1 == Arrival 1)
	 */
	public Saapumisprosessi(ContinuousGenerator generaattori, Moottori moottori, TapahtumanTyyppi tyyppi){
		this.generaattori = generaattori;
		this.moottori = moottori;
		this.tyyppi = tyyppi;
	}

	/**
	 * Tämä metodi generoi uuden tapahtuman ja antaa sen moottorille.
	 */
	public void generoiSeuraava(){
		Tapahtuma tapahtuma = new Tapahtuma(tyyppi, Kello.getInstance().getAika() + generaattori.sample());
		moottori.uusiTapahtuma(tapahtuma);
	}

}
