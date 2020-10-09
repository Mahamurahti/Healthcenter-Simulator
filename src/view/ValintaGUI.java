package view;

import java.util.List;

import controller.IKontrolleri;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
/**
 * 
 * ValintaGUI-luokan tarkoituksena on näyttää käyttäjä ikkuna, jossa käyttäjä voi valita, minkä tallennetun
 * simulointikerran tulokset käyttäjä haluaa nähdä.
 * 
 * @author Eric Keränen
 * @version 1.0
 *
 */
/**
 * @author Eric Keränen
 *
 */
public class ValintaGUI {
	
	private TietokantaGUI tietokanta;
	
	/**
	 * Näyttää uudessa ikkunassa listan uniikeista simulointikerroista ID:stä.
	 * Tiettyä ID:tä klikkaamalla avautuu sen simulointikerran tulokset ja jakaumat.
	 * @param kontrolleri määrittelee metodeja, joilla hakea tietokannasta oikeat tulokset
	 */
	public ValintaGUI(IKontrolleri kontrolleri) {
		Stage naytto = new Stage();
		
		naytto.setTitle("Valinta");
		naytto.setMinWidth(300);
		naytto.setMinHeight(400);
				
		Label valitseText = new Label("Valitse alta:");
		valitseText.getStyleClass().add("label-bottom-border");
		valitseText.setFont(Font.font("Futura", FontWeight.BOLD, 20));
		valitseText.setAlignment(Pos.BASELINE_CENTER);
		valitseText.setMinWidth(naytto.getMinWidth());
		valitseText.setPrefHeight(50);
		
		ListView listView = new ListView();
		listView.setMinWidth(naytto.getMinWidth());
		
		List<Integer> uniikitSimId = kontrolleri.getUniikitSimulointiID();
		
		for(Integer intti : uniikitSimId) {
			listView.getItems().add(kontrolleri.getPaivaJaAika(intti));	
		}
		
		listView.setOnMouseClicked(e -> {
			String[] teksti = listView.getSelectionModel().getSelectedItem().toString().split("/");
			int nro = Integer.parseInt(teksti[0]);
			tietokanta = new TietokantaGUI(kontrolleri, nro);
		});
		
		GridPane root = new GridPane();
		root.setVgap(20);
		root.add(valitseText, 0, 0);
		root.add(listView, 0, 1);
		
		Scene scene = null;
		
		if(listView.getItems().isEmpty()) {
			naytaAlert("Tietokannasta ei löytynyt mitään");
		}else {
			scene = new Scene(root);
			scene.getStylesheets().add("css/styles.css");			
			naytto.setScene(scene);
			naytto.showAndWait();
		}
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
	
}
