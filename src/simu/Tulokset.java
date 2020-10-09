package simu;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.*;

/**
 * 
 * Tulokset-luokan tarkoituksena on toimia tietokannan "Entitynä" ja säilöntäpaikkana simulointien
 * tuloksille. Tulokset säilyttävät simuloinnin tulokset, käyttäjän antamat jakaumat sekä tapahtuma-ajan.
 * 
 * @author Eric Keränen
 * @version 1.6
 *
 */
@Entity
@Table(name="tulokset")
public class Tulokset {

	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	@Column
	private int simuKerId = 0;
	@Column
	private String palvPisteNimi;
	@Column
	private int asLkm;
	@Column
	private int jonoMax;
	@Column
	private int jonoKeskim;
	@Column
	private double akAika;
	@Column
	private double kmPalvAika;
	@Column
	private double kaAste;
	@Column
	private int vastotJak;
	@Column
	private int rontgenJak;
	@Column
	private int kassaJak;
	@Column
	private int laakariJak;
	@Column
	private int huoneet;
	@Column
	private int saapumisJak;
	@Column
	private Date paiva;
	@Column 
	private Time aika;
	
	public Tulokset() {		}
	
	/**
	 * Tulokset-luokan konstruktori pitää tallessa simulaattorin tuloksia.
	 * 
	 * Tulokset-luokka on myös tietokannan kanssa sidoksissa.
	 * 
	 * @param nimi        = palvelupisteen nimi
	 * @param asLkm		  = asiakkaiden lukumäärä
	 * @param akAika	  = aktiiviaika
	 * @param kmPalvAika  = keskimääräinen palveluaika
	 * @param kaAste      = käyttöaste
	 * @param jonoMax     = jonon maksimi pituus
	 * @param jonoKeskim  = jonon keskimääräinen pituus
	 * @param vastot      = vastaanoton jakauma
	 * @param rontgen     = röntgensalin jakauma
	 * @param kassa       = kassan jakauma
	 * @param laakari     = lääkärinhuoneiden jakaumat
	 * @param huoneet     = lääkärinhuoneiden määrä
	 * @param saapumis    = saapumis jakauma
	 * @param paiva		  = tämänhetkinen päivä
	 * @param aika 		  = tämänhetkinen aika
	 */
	public Tulokset(String nimi, int asLkm, double akAika, double kmPalvAika, double kaAste, int jonoMax, int jonoKeskim,
					int vastot, int rontgen, int kassa, int laakari, int huoneet, int saapumis, Date paiva, Time aika) {	
				
		this.palvPisteNimi = nimi;
		this.asLkm = asLkm;
		this.akAika = konvertoi(akAika, 2);
		
		if(Double.isNaN(kmPalvAika) || Double.isInfinite(kmPalvAika)) { this.kmPalvAika = 0.0; }
		else { this.kmPalvAika = konvertoi(kmPalvAika, 2); }
		
		this.kaAste = konvertoi(kaAste, 2);
		this.jonoMax = jonoMax;
		this.jonoKeskim = jonoKeskim;
		
		this.vastotJak = vastot;
		this.rontgenJak = rontgen;
		this.kassaJak = kassa;
		this.laakariJak = laakari;
		this.huoneet = huoneet;
		this.saapumisJak = saapumis;
		this.paiva = paiva;
		this.aika = aika;
	}
	
	public Long getId() {
		return id;
	}
	
	public int getSimulointiKerranID() {
		return simuKerId;
	}
	
	public void setSimulointiKerranID(int id) {
		simuKerId = id;
	}
	
	public String getPalvPisteNimi() {
		return palvPisteNimi;
	}

	public int getAsLkm() {
		return asLkm;
	}
	
	public double getAkAika() {
		return akAika;
	}
	
	public double getKmPalvAika() {
		return kmPalvAika;
	}
	
	public double getKaAste() {
		if(kaAste > 100) { kaAste = 100; }
		return kaAste;
	}

	public int getJonoMax() {
		return jonoMax;
	}

	public int getJonoKeskim() {
		return jonoKeskim;
	}
	
	public int getVastotJak() {
		return vastotJak;
	}

	public int getRontgenJak() {
		return rontgenJak;
	}
	
	public int getKassaJak() {
		return kassaJak;
	}

	public int getLaakariJak() {
		return laakariJak;
	}

	public int getHuoneet() {
		return huoneet;
	}

	public int getSaapumisJak() {
		return saapumisJak;
	}
	
	public Date getPaiva() {
		return paiva;
	}
	
	public Time getAika() {
		return aika;
	}
	
	/**
	 * Tämä metodi konvertoi desimaaliluvut helpommin ymmärrettäviksi
	 * esim. 1045.3246048324703 = 1045.32
	 * @param arvo määrittelee mikä arvo muunnetaan
	 * @param tarkkuus kuinka tarkaksi arvo muunnetaan
	 * @return muunnettu arvo
	 */
	private double konvertoi(double arvo, int tarkkuus) {
		BigDecimal bd;
		bd = new BigDecimal(arvo).setScale(tarkkuus, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	@Override
	public String toString() {
		return "Nimi: " + palvPisteNimi + ", Asiakkaiden lukumäärä: " + asLkm + ", Aktiiviaika: " + akAika + ", Keskimääräinen palveluaika: " + 
				kmPalvAika + ", Käyttöaste: " + kaAste + ", Jonon max pituus: " + jonoMax + ", Jonon pituus keskim.: " + jonoKeskim;
	}
	
}
