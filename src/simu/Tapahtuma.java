package simu;
/**
 * 
 * Tapahtuma-luokan tarkoitus on luoda "aikoja" palvelupisteisiin, joihin asiakkaat
 * menevät. Jokaisella tapahtuma on tapahtuma-aika sekä -tyyppi.
 * 
 * @author Eric Keränen
 * @version 1.0
 *
 */
public class Tapahtuma implements Comparable<Tapahtuma> {
	
		
	private TapahtumanTyyppi tyyppi;
	private double aika;
	
	/**
	 * Tapahtuma-luokan konstruktori alustaa luokan muuttujia.
	 * @param tyyppi on tapahtuman tyyppi
	 * @param aika on aika, milloin tapahtuma tapahtuu
	 */
	public Tapahtuma(TapahtumanTyyppi tyyppi, double aika){
		this.tyyppi = tyyppi;
		this.aika = aika;
	}
	
	public void setTyyppi(TapahtumanTyyppi tyyppi) {
		this.tyyppi = tyyppi;
	}
	public TapahtumanTyyppi getTyyppi() {
		return tyyppi;
	}
	public void setAika(double aika) {
		this.aika = aika;
	}
	public double getAika() {
		return aika;
	}

	/**
	 * Tapahtumat ovat tapahtumalistassa priorityQueue-listassa, joka tarvitsee ehdon,
	 * minkä mukaan lista järjestää tapahtumansa ja compareTo-metodi kertoo järjestyksen
	 */
	@Override
	public int compareTo(Tapahtuma arg) {
		if (this.aika < arg.aika) {
			return -1;
		}else if (this.aika > arg.aika) {
			return 1;
		}else {
			return 0;			
		}
	}
}
