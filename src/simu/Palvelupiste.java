package simu;
import java.util.LinkedList;
import eduni.distributions.ContinuousGenerator;
/**
 * 
 * Palvelupiste-luokan tarkoitus on pitää listaa asiakkaista, jotka ovat jonossa kyseiseen
 * palvelupisteeseen ja palvella jonon ensimmäistä asiakasta palveluajan verran.
 * 
 * @author Eric Keränen
 * @version 1.5
 *
 */
public class Palvelupiste {

	private Moottori moottori;

	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();
	private ContinuousGenerator generator;
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;
	private String nimi;
			
	private int palvellutAsiakkaat = 0, jononPituus = 0, max = 0;
	private double aktiiviAika = 0, kaikkiOdotusAjat = 0, avg = 0;

	private boolean varattu = false;

	/**
	 * Palvelupiste-luokan konstruktori alustaa luokan muuttujia. Jokaisella palvelupisteellä
	 * on oma tapahtuman tyyppi, riippuen siitä monesko palvelupiste on, generaattori ja nimi.
	 * @param generator on satunnaislukugeneraattori
	 * @param moottori on koko ohjelman pyörittäjä
	 * @param tyyppi on tapahtuman tyyppi enum (esim. DEP1 == Departure 1)
	 * @param nimi on palvelupisteen nimi
	 */
	public Palvelupiste(ContinuousGenerator generator, Moottori moottori, TapahtumanTyyppi tyyppi, String nimi){
		this.moottori = moottori;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;	
		this.nimi = nimi;
	}

	/**
	 * Lisää jonoon asiakkaan. Jonon ensimmäinen asiakas on aina palvelussa.
	 * 
	 * Jokaisella asiakkaalla on 10% mahdollisuus olla röntgenpotilaita.
	 * Asiakkaat voidaan vain kerran määritellä röntgenpotilaaksi, jos ensimmäisellä
	 * kerralla asiakas ei muutu röntgenpotilaaksi, ei hänellä ole enään mahdollisuutta
	 * muuttua sellaiseksi.
	 * 
	 * Tässä metodissa tapahtuu myös asiakkaan visuaalinen lisääminen näytölle.
	 * @param asiakas, joka lisätään jonoon
	 */
	public void lisaaJonoon(Asiakas asiakas){
		jono.add(asiakas);
		jononPituus++;
		if(max < jononPituus) { max = jononPituus; }
		for(Asiakas tempAsiakas : jono) {
			if(!tempAsiakas.getRikkomisenYritys()) {
				tempAsiakas.rikoAsiakkaanLuu(10);
			}
		}
		moottori.visualisoiAsiakkaat(asiakas.getTarvitseeRontgen(), this.getNimi());
		asiakas.setPalveluPisteeseenTulo(Kello.getInstance().getAika());
	}

	/**
	 * Tämä metodi poistaa jonon ensimmäisen asiakkaan eli asiakkaan, joka on palvelussa.
	 * 
	 * Tässä metodissa tapahtuu myös asiakkaa visuaalinen poistaminen näytöltä
	 * @return asiakas, joka oli jonossa, jotta hänet voidaan viedä seuraavaan palvelupisteeseen
	 */
	public Asiakas otaJonosta(){
		varattu = false;
		
		palvellutAsiakkaat++;
		jononPituus--;
		
		moottori.kumitaAsiakas(this.getNimi());
		kaikkiOdotusAjat += (Kello.getInstance().getAika() - jono.peek().getPalveluPisteeseenTulo());
		return jono.poll();
	}

	/**
	 * Tämä metodi aloittaa uuden palvelun asiakkaalle ja generoi ajan, jonka mukaan
	 * luodaan uusi tapahtuma generoidun ajan päähän nykyajasta. Tämä toimii asiakkaan
	 * palveluaikana. Asiakas pysyy jonossa ensimmäisenä palvelun ajan.
	 */
	public void aloitaPalvelu(){
		varattu = true;
		double palveluaika = generator.sample();
		aktiiviAika += palveluaika;
		moottori.uusiTapahtuma(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
	}
	
	public String getNimi() {
		return this.nimi;
	}
	
	public Asiakas getEnsimAsiakas() {
		return jono.peek();
	}
	
	public int getPalvellutAsiakkaat() {
		return this.palvellutAsiakkaat;
	}
	
	public double getAktiiviAika() {
		return this.aktiiviAika;
	}
	
	public String getPalvelupisteenNimi() {
		return this.nimi;
	}
	
	public int jononMaxPituus() {
		return this.max;
	}
	
	public double jononKeskimPituus() {
		avg = kaikkiOdotusAjat / Kello.getInstance().getAika();
		return Math.ceil(this.avg);
	}
	
	/**
	 * Tarkistaa, onko palvelupiste varattu
	 * @return true, jos palvelupiste varattu
	 */
	public boolean onVarattu(){
		return varattu;
	}

	/**
	 * Tarkistaa, onko jono tyhjä
	 * @return true, jos jonossa ei ole ketään
	 */
	public boolean onJonossa(){
		return jono.size() != 0;
	}
}
