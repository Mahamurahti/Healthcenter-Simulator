package view;

import controller.IKontrolleri;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * 
 * PoistoGUI-luokan tarkoituksena on kysyä käyttäjältä varmistusta tietokannasta poistaessa
 * 
 * @author Eric Keränen
 * @version 1.2
 *
 */
public class PoistoGUI {
	/**
	 * Näyttää uudessa ikkunassa tekstikentän, johon laitetaan minkä simulointi-
	 * kerran ID:n käyttäjä haluaa poistaa tietokannasta tai jos kyseessä on
	 * kokonaispoisto, niin näytölle tulee varmistuskysymys, että haluaako
	 * käyttäjä oikeasti poistaa koko tietokannan
	 * @param kontrolleri määrittelee metodeja tietokannasta poistamista varten
	 * @param kokonaisPoisto määrittelee onko poiston tyyppi kokonaispoisto vai ei
	 * @param viesti määrittelee sen mitä ikkunassa lukee
	 */
	public PoistoGUI(IKontrolleri kontrolleri, boolean kokonaisPoisto, String viesti) {
				
		Stage naytto = new Stage();
		
		naytto.initModality(Modality.APPLICATION_MODAL);
		naytto.setTitle("Poisto");
		naytto.setMinWidth(400);
		naytto.setMinHeight(200);
		naytto.setResizable(false);
		
		Label poistoTeksti = new Label();
		poistoTeksti.setText(viesti);
		
		TextField syottoKentta = new TextField();
		syottoKentta.setMaxWidth(100);			

		
		Button okPainike = new Button("OK");
		okPainike.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
            	if(kokonaisPoisto) {
            		try {
            			kontrolleri.poistaKaikkiTietokannasta();
            			naytto.close();
            			naytaInfo("Kaikki tiedot poistettiin onnistuneesti tietokannasta.");
            		}catch(Exception ex) {
            			naytaAlert("Tietokannassa ei ole mitään");
            		}
            	}else {
            		try {
            			kontrolleri.poistaTietokannasta(Integer.parseInt(syottoKentta.getText().toString()));            			
            			naytto.close();
            			naytaInfo("Tietokannasta poistettiin tiedot ID:stä " + syottoKentta.getText().toString());
            		}catch(Exception ex) {
            			naytaAlert("Kyseisellä ID:llä ei löydy mitään");
            		}
            	}
            }
        });;
		Button peruutaPainike = new Button("Peruuta");
		peruutaPainike.setOnAction(e -> naytto.close());
		
		HBox painikkeet = new HBox();
		painikkeet.getChildren().addAll(okPainike, peruutaPainike);
		painikkeet.setAlignment(Pos.CENTER);
		
		VBox root = new VBox(10);
		if(kokonaisPoisto) {
			root.getChildren().addAll(poistoTeksti, painikkeet);	
		}else {
			root.getChildren().addAll(poistoTeksti, syottoKentta, painikkeet);			
		}
		root.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add("css/styles.css");
		naytto.setScene(scene);
		naytto.showAndWait();
	}
	
	/**
	 * Näyttää näytölle alertin, jos komentoa ei pystytty suorittamaa
	 * @param teksti, joka kuvaa ongelmaa
	 */
	private void naytaAlert(String teksti) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(null);
		alert.setContentText(teksti);
		
		alert.showAndWait();
	}
	
	/**
	 * Näyttää näytölle infon siitä, mitä on tapahtunut
	 * @param teksti, joka kuvaa tapahtumaa
	 */
	private void naytaInfo(String teksti) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("INFO");
		alert.setHeaderText(null);
		alert.setContentText(teksti);
		
		alert.showAndWait();
	}
	
}
