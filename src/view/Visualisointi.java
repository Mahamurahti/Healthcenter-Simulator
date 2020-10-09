package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * 
 * Visualisointi-luokan tarkoitus on visualisoida näytölle moottorin toimintaa. Palvelupisteen
 * jonoon lisätty asiakas näkyy näytöllä pallona, jonka väri määräytyy sen mukaan, tarvitseeko
 * asiakas röntgensalin palveluita vai ei. Pallon positio näytöllä määräytyy sen mukaan, mikä
 * palvelupiste on kutsunut metodeja tästä luokasta.
 * 
 * @author Eric Keränen
 * @version 1.6
 *
 */
public class Visualisointi extends Canvas{

	private GraphicsContext gc;
	
	private double i = 0;
	private double j = 0;
	
	private int v = 0, r = 0, k = 0, l1 = 0, l2 = 0, l3 = 0;
	/**
	 * Visualisointi-luokan konstruktori, joka määrittelee visualisoinnille alueen, jonne voi piirtää.
	 * @param w määrittelee visualisointi alueen leveyden
	 * @param h määrittelee visualisointi alueen korkeuden
	 */
	public Visualisointi(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		tyhjennaNaytto();
	}
	
	/**
	 * Tyhjentä näytön palloista ja nollaa muuttujat
	 */
	public void tyhjennaNaytto() {
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
		gc.strokeRect(0, 0, this.getWidth(), this.getHeight());
		
		piirraPalvelupisteet();
		
		v = 0;
		r = 0;
		k = 0;
		l1 = 0;
		l2 = 0;
		l3 = 0;
	}
	
	/**
	 * Pirtää näytölle kuusi laatikkoa vaakasuunnassa ja antaa 
	 * jokaiselle laatikolle oman nimen (palvelupisteiden nimet)
	 */
	public void piirraPalvelupisteet() {
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(3);
		gc.strokeRect(0, 0, this.getWidth(), 67);
		gc.strokeRect(0, 0, this.getWidth(), 134);
		gc.strokeRect(0, 0, this.getWidth(), 201);
		gc.strokeRect(0, 0, this.getWidth(), 268);
		gc.strokeRect(0, 0, this.getWidth(), 335);
		gc.strokeRect(0, 0, this.getWidth(), 400);
		gc.strokeRect(0, 0, 120, 400);
		
		gc.setLineWidth(1);
		gc.strokeText("Vastaanotto", 	 6, 40);
		gc.strokeText("Röntgensali", 	 6, 105);
		gc.strokeText("Kassa", 			 6, 175);
		gc.strokeText("Lääkärinhuone 1", 6, 240);
		gc.strokeText("Lääkärinhuone 2", 6, 308);
		gc.strokeText("Lääkärinhuone 3", 6, 370);
	}
	
	/**
	 * Tämä metodi piirtää näytölle asiakkaat riippuen siitä, mikä palvelupiste
	 * kutsui metodia. Palvelupisteen kutsuessa metodia, antaa palvelupiste oman
	 * nimesä parametrina ja sen mukaan pallo piirretään oikean laatikon sisälle
	 * näytölle.
	 * @param rontgen määrittelee pallon värin, röntgen potilaat ovat vaalean-oransseja
	 * 		  ja normaalit lääkäriä tarvitsevat potilaat ovat mustia
	 * @param nimi määrittelee sen mihin kohtaa pallo piirretään
	 */
	public void piirraAsiakas(boolean rontgen, String nimi) {
		if(rontgen) {
			gc.setFill(Color.DARKSALMON);
		}else {
			gc.setFill(Color.BLACK);
		}
		
		switch(nimi) {
			case "Vastaanotto":
				i = 132 + (v++ * 12);
				j = 34;
				gc.fillOval(i,j,10,10);
				break;
			case "Röntgensali":
				i = 132 + (r++ * 12);
				j = 101;
				gc.fillOval(i,j,10,10);
				break;
			case "Kassa":
				i = 132 + (k++ * 12);
				j = 168;
				gc.fillOval(i,j,10,10);
				break;
			case "Lääkärinhuone 1":
				i = 132 + (l1++ * 12);
				j = 235;
				gc.fillOval(i,j,10,10);
				break;
			case "Lääkärinhuone 2":
				i = 132 + (l2++ * 12);
				j = 302;
				gc.fillOval(i,j,10,10);
				break;
			case "Lääkärinhuone 3":
				i = 132 + (l3++ * 12);
				j = 369;
				gc.fillOval(i,j,10,10);
				break;
		}
	}
	
	/**
	 * Tämä metodi kumittaa näytöltä asiakkaat riippuen, mikä palvelupiste
	 * kutsui metodia. Toimii täysin samalla tavalla kuin piirraAsiakas-metodi,
	 * mutta vain muuttujien plussaamisen sijaan tämä metodi miinustaa metodeista
	 * @param nimi määrittelee sen mistä kohtaa pallo kumitetaan
	 */
	public void kumitaAsiakas(String nimi) {
		gc.setFill(Color.WHITE);
		
		switch(nimi) {
			case "Vastaanotto":
				i = 132 + (v-- * 12);
				j = 34;
				gc.fillRect(i,j,10,10);
				break;
			case "Röntgensali":
				i = 132 + (r-- * 12);
				j = 101;
				gc.fillRect(i,j,10,10);
				break;
			case "Kassa":
				i = 132 + (k-- * 12);
				j = 168;
				gc.fillRect(i,j,10,10);
				break;
			case "Lääkärinhuone 1":
				i = 132 + (l1-- * 12);
				j = 235;
				gc.fillRect(i,j,10,10);
				break;
			case "Lääkärinhuone 2":
				i = 132 + (l2-- * 12);
				j = 302;
				gc.fillRect(i,j,10,10);
				break;
			case "Lääkärinhuone 3":
				i = 132 + (l3-- * 12);
				j = 369;
				gc.fillRect(i,j,10,10);
				break;
		}
	}
}
