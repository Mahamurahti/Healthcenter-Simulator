package simu;
/**
 * 
 * Asiakas-luokkaa tarvitaan kuvaamaan yksittäisen asiakkaan tietoja. Asiakas on se olio,
 * jota simulaattorissa viedään järjestelmän läpi.
 * 
 * @author Eric Keränen
 * @version 1.2
 *
 */
public class Asiakas {
	private double saapumisaika, poistumisaika, palveluPisteeseenTulo;

	private int id;
	private static int seuraavaVapaaID = 1, asiakkaidenLkm = 0;
	private static long sum = 0;
	
	private boolean tarvitseeRontgen = false, rikkomisenYritys = false;
	private static int tarvitsiRontgenLkm = 0;
	
	/**
	 * Asiakas-luokan konstruktori asettaa asiakkaalle aina uuden id:n
	 * ja antaa asiakkaalle palveluun saapumisen ajankohdan
	 */
	public Asiakas(){
	    id = seuraavaVapaaID++;
	    asiakkaidenLkm++;;
	    
		saapumisaika = Kello.getInstance().getAika();
	}

	public double getPoistumisaika() {
		return poistumisaika;
	}

	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	public double getSaapumisaika() {
		return saapumisaika;
	}

	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	public double getPalveluPisteeseenTulo() {
		return palveluPisteeseenTulo;
	}

	public void setPalveluPisteeseenTulo(double palveluPisteeseenTulo) {
		this.palveluPisteeseenTulo = palveluPisteeseenTulo;
	}
	
	public boolean getTarvitseeRontgen() {
		return this.tarvitseeRontgen;
	}
	
	public boolean getRikkomisenYritys() {
		return this.rikkomisenYritys;
	}
	
	public static int seuraavaVapaaID() {
		return seuraavaVapaaID;
	}
	
	public static int getAsiakkaidenLkm() {
		return asiakkaidenLkm;
	}
	
	public static int getTarvitsiRontgenLkm() {
		return tarvitsiRontgenLkm;
	}
	
	/**
	 * Tämä metodi kutsutaan jokaisen simulointi kerran käynnistämisen
	 * yhteydessä. Jotta tulokset olisivat aina paikkaansa pitäviä,
	 * pitää alkuarvot aina nollata.
	 */
	public static void nollaaAsiakas() {
		asiakkaidenLkm = 0;
		sum = 0;
		tarvitsiRontgenLkm = 0;
		seuraavaVapaaID = 1;
	}
	
	/**
	 * Tämä metodi yrittää rikkoa asiakkaan luun parametrissä annetun
	 * mahdollisuuden mukaan. Tämän metodin voi kutsua kerran per asiakas.
	 * RikkomisenYritys muuttujan ollessa true, ei voi tätä metodia enään kutsua.
	 * @param mahdollisuus luun rikkoutumiseen (10 = 10%)
	 */
	public void rikoAsiakkaanLuu(int mahdollisuus) {
		rikkomisenYritys = true;
		
		int sample = (int)(Math.random() * 100);
		if(sample < mahdollisuus) {
			tarvitseeRontgen = true;
			tarvitsiRontgenLkm++;
		}
	}
	
	/**
	 * Tämä metodi raportoi tietoja asiakkaasta, käytetään testaamisessa
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " saapui:" + saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas " + id + " poistui:" + poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas " + id + " viipyi:" + (poistumisaika - saapumisaika));
		sum += (poistumisaika - saapumisaika);
		double keskiarvo = sum / id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo " + keskiarvo);
	}

}
