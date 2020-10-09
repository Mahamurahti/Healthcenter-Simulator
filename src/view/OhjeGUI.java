package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
/**
 * 
 * OhjeGUI-luokan tarkoituksena on näyttää simulaattorin käyttöohjeet käyttäjälle.
 * 
 * @author Eric Keränen
 * @version 1.1
 *
 */
public class OhjeGUI {

	/**
	 * Näyttää uudessa ikkunassa ohjeet, kuinka simulaattoria käytetään
	 */
	public OhjeGUI() {
		Stage naytto = new Stage();
		naytto.setTitle("Käyttöohje");
		naytto.setMinWidth(800);
		naytto.setMinHeight(400);
		naytto.setResizable(false);
		
		BorderPane borderPane = new BorderPane();
		
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		
		Label otsikko = new Label("Käyttöohje");
		otsikko.setFont(Font.font("Futura", FontWeight.BOLD, 20));
		otsikko.setAlignment(Pos.BASELINE_CENTER);
		otsikko.setPrefWidth(500);
		otsikko.getStyleClass().add("label-bottom-border");
		
		Label syotot = new Label("Syötöt");
		syotot.setFont(Font.font("Futura", FontWeight.BOLD, 16));
		syotot.setAlignment(Pos.BASELINE_CENTER);
		syotot.setPrefWidth(500);
		syotot.getStyleClass().add("ohje-label");
		
		Label ekaText = new Label("1. Aika määrittelee millisekunneissa, kuinka kauan simulointi pyörii.\n"
								+ "    Simulaattorin sisäisen ajan voi kuitenkin tulkita minuutteina.\n"
								+ "    Mitä pidempää simulaattori pyörii, sitä tarkemmiksi simulaattorin\n"
								+ "    tulokset tulevat.");
		ekaText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		ekaText.setAlignment(Pos.BASELINE_LEFT);
		ekaText.setPrefWidth(500);
		ekaText.getStyleClass().add("ohje-label");
		
		Label tokaText = new Label("2. Viive määrittelee millisekunneissa, kuinka kauan simulaattori\n"
								 + "   odottaa jokaisen simulointi kierroksen jälkeen. Simulointi\n"
								 + "   kierroksella tarkoitetaa sitä aikaa, mikä simulaattorilla\n"
								 + "   kestää prosessoida tietynajanhetken palvelupisteiden asiakkaat.");
		tokaText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		tokaText.setAlignment(Pos.BASELINE_LEFT);
		tokaText.setPrefWidth(500);
		tokaText.getStyleClass().add("ohje-label");
		
		Label kolmasText = new Label("3. Huoneet määrittelee lääkärinhuoneiden määrän.\n"
				 				   + "    Lääkärinhuoneita voi olla maksimissaa kolme.\n"
								   + "    Potilaat jakautuvat eri lääkärihuoneisiin tasaisesti,\n"
								   + "    jos huoneita on enemmän kuin yksi.");
		kolmasText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		kolmasText.setAlignment(Pos.BASELINE_LEFT);
		kolmasText.setPrefWidth(500);
		kolmasText.getStyleClass().add("ohje-label");
		
		Label neljasText = new Label("4. Saapumis jakauma määrittelee kuinka nopeasti\n"
								   + "    asiakkaita tulee terveyskeskukseen. Mitä pienempi luku\n"
								   + "    saapumis jakaumassa on, sitä nopeammin asiakkaita tulee.\n"
								   + "    Saapumis jakauma on negatiivinen eksponentiaalinen\n"
								   + "    jakauma, joka tarkoittaa sitä, että aluksi asiakkaita\n"
								   + "    ei tule niin paljoa kuin loppua kohden.");
		neljasText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		neljasText.setAlignment(Pos.BASELINE_LEFT);
		neljasText.setPrefWidth(500);
		neljasText.getStyleClass().add("ohje-label");
		
		Label jakaumat = new Label("Jakaumat");
		jakaumat.setFont(Font.font("Futura", FontWeight.BOLD, 16));
		jakaumat.setAlignment(Pos.BASELINE_CENTER);
		jakaumat.setPrefWidth(500);
		jakaumat.getStyleClass().add("ohje-label");
		
		Label viidesText = new Label("5. Vastaanotto jakauma määrittelee kuinka kauan vastaanotolla\n"
				   				   + "    kestää palvella yksi asiakas. Vastaanoton jakauma on\n"
				   				   + "    normaali jakauma, joka tarkoittaa sitä, että palveluajat\n"
				   				   + "    ovat suurimmaksi osaksi tekstikenttään asetettu luku ja\n"
				   				   + "    poikkeaa vähän siitä");
		viidesText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		viidesText.setAlignment(Pos.BASELINE_LEFT);
		viidesText.setPrefWidth(500);
		viidesText.getStyleClass().add("ohje-label");
		
		Label kuudesText = new Label("6. Röntgensalin jakauma määrittelee kuinka kauan röntgensalilla\n"
				   				   + "    kestää palvella yksi asiakas. Röntgensalin jakauma on\n"
				   				   + "    normaali jakauma.");
		kuudesText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		kuudesText.setAlignment(Pos.BASELINE_LEFT);
		kuudesText.setPrefWidth(500);
		kuudesText.getStyleClass().add("ohje-label");
		
		Label seitsemasText = new Label("7. Kassan jakauma määrittelee kuinka kauan kassalla\n"
				   					  + "    kestää palvella yksi asiakas. Kassan jakauma on\n"
				   					  + "    normaali jakauma.");
		seitsemasText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		seitsemasText.setAlignment(Pos.BASELINE_LEFT);
		seitsemasText.setPrefWidth(500);
		seitsemasText.getStyleClass().add("ohje-label");
		
		Label kaheksasText = new Label("8. Lääkärinhuoneiden jakaumat määrittelevät kuinka kauan\n"
									 + "    lääkärinhuoneilla kestää palvella yksi asiakkaistaan.\n"
									 + "    Lääkärinhuoneiden jakaumat ovat normaali jakaumia.\n"
									 + "    Röntgenpotilaat eivät käy lääkärinhuoneissa, sillä\n"
									 + "    simulaattori olettaa, että röntgenkuvat pitää ensin\n"
									 + "    analysoida ennen lääkärile menoa.");
		kaheksasText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		kaheksasText.setAlignment(Pos.BASELINE_LEFT);
		kaheksasText.setPrefWidth(500);
		kaheksasText.getStyleClass().add("ohje-label");
		
		Label yheksasText = new Label("Simulaattorin graafisessa osuudessa näkyy jokainen palvelupiste"
				 					+ ", joihin simulaattorin käynnistettyä tulee palloja. Yksittäinen\n"
									+ "musta pallo kuvastaa yhtä asiakasta kyseisen palvelupisteen"
									+ " jonossa. Mitä enemmän palloja yhdellä palvelupisteellä on\n"
									+ "sitä pidempi jono kyseisellä palvelupisteellä on. Vaalean-"
									+ "oranssin väriset pallot ovat asiakkaita, jotka tarvitsevat"
									+ " röntgensalin palveluita.\nKyseiset asiakkaat käyvät vain"
									+ " röntgensalissa. Asiakkailla on 10% mahdollisuus olla röntgenkuvauksien"
									+ " tarpeessa.");
		yheksasText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
		yheksasText.setAlignment(Pos.BASELINE_LEFT);
		yheksasText.setPrefWidth(1010);
		yheksasText.getStyleClass().add("ohje-label");
		
		gridPane.add(yheksasText, 0, 0, 2, 1);
		gridPane.add(syotot, 0, 1);
		gridPane.add(ekaText, 0, 2);
		gridPane.add(tokaText, 0, 3);
		gridPane.add(kolmasText, 0, 4);
		gridPane.add(neljasText, 0, 5);
		gridPane.add(jakaumat, 1, 1);
		gridPane.add(viidesText, 1, 2);
		gridPane.add(kuudesText, 1, 3);
		gridPane.add(seitsemasText, 1, 4);
		gridPane.add(kaheksasText, 1, 5);
		
		borderPane.setCenter(otsikko);
		borderPane.setBottom(gridPane);
		
		Scene scene = new Scene(borderPane);
		scene.getStylesheets().add("css/styles.css");
		naytto.setScene(scene);
		naytto.showAndWait();
	}
	
}
