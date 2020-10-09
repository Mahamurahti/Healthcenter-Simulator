package view;

import java.util.List;

import controller.IKontrolleri;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import simu.Tulokset;
/**
 * 
 * TietokantaGUI-luokan tarkoituksena on näyttää näytöllä tietokannasta haetut tulokset
 * 
 * @author Eric Keränen
 * @version 1.2
 *
 */
public class TietokantaGUI {
	/**
	 * Näyttää uudessa ikkunassa kaksi listaa, joissa on ylemmässä
	 * listattuna sen simulointikerran tulokset ja alemmassa listattuna 
	 * sen simulointikerran jakaumat, jotka käyttäjä valitsi ValintaGUI:ssa.
	 * @param kontrolleri määrittelee metodeja, jos niitä tarvitsee
	 * @param id määrittelee sen, minkä simulointikerran tuloksia tarkastellaan
	 */
	public TietokantaGUI(IKontrolleri kontrolleri, int id) {
		
		Stage naytto = new Stage();
		
		naytto.setTitle("Tietokanta");
		naytto.setMinWidth(1000);
		naytto.setMaxHeight(500);
		
		TableView upperTable = new TableView();
        
        TableColumn<String, Tulokset> column1 = new TableColumn<>("Palvelupisteen nimi");
        TableColumn<String, Tulokset> column2 = new TableColumn<>("Asiakkaita");
        TableColumn<String, Tulokset> column3 = new TableColumn<>("Aktiiviaika");
        TableColumn<String, Tulokset> column4 = new TableColumn<>("Keskimääräinen palveluaika");
        TableColumn<String, Tulokset> column5 = new TableColumn<>("Käyttöaste");
        TableColumn<String, Tulokset> column6 = new TableColumn<>("Jonon max pituus");
        TableColumn<String, Tulokset> column7 = new TableColumn<>("Jonon keskim. pituus");
        
        column1.setCellValueFactory(new PropertyValueFactory<>("palvPisteNimi"));
        column2.setCellValueFactory(new PropertyValueFactory<>("asLkm"));
        column3.setCellValueFactory(new PropertyValueFactory<>("akAika"));
        column4.setCellValueFactory(new PropertyValueFactory<>("kmPalvAika"));
        column5.setCellValueFactory(new PropertyValueFactory<>("kaAste"));
        column6.setCellValueFactory(new PropertyValueFactory<>("jonoMax"));
        column7.setCellValueFactory(new PropertyValueFactory<>("jonoKeskim"));
        
        column1.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        column2.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        column3.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        column4.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        column5.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        column6.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        column7.prefWidthProperty().bind(upperTable.widthProperty().multiply(0.1428));
        
        List<Tulokset> tulokset = kontrolleri.getTuloksetSimulointiID(id);
        for(Tulokset tulos : tulokset) {
        	upperTable.getItems().add(tulos);
        }
        
        upperTable.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
        
        TableView lowerTable = new TableView();
        
        TableColumn<String, Tulokset> column8 = new TableColumn<>("Vastaanoton jakauma");
        TableColumn<String, Tulokset> column9 = new TableColumn<>("Röntgenin jakauma");
        TableColumn<String, Tulokset> column10 = new TableColumn<>("Kassan jakauma");
        TableColumn<String, Tulokset> column11 = new TableColumn<>("Lääkärien jakaumat");
        TableColumn<String, Tulokset> column12 = new TableColumn<>("Huoneiden määrä");
        TableColumn<String, Tulokset> column13 = new TableColumn<>("Saapumisjakauma");
        
        column8.setCellValueFactory(new PropertyValueFactory<>("vastotJak"));
        column9.setCellValueFactory(new PropertyValueFactory<>("rontgenJak"));
        column10.setCellValueFactory(new PropertyValueFactory<>("kassaJak"));
        column11.setCellValueFactory(new PropertyValueFactory<>("laakariJak"));
        column12.setCellValueFactory(new PropertyValueFactory<>("huoneet"));
        column13.setCellValueFactory(new PropertyValueFactory<>("saapumisJak"));
        
        column8.prefWidthProperty().bind(lowerTable.widthProperty().multiply(0.166666));
        column9.prefWidthProperty().bind(lowerTable.widthProperty().multiply(0.166666));
        column10.prefWidthProperty().bind(lowerTable.widthProperty().multiply(0.166666));
        column11.prefWidthProperty().bind(lowerTable.widthProperty().multiply(0.166666));
        column12.prefWidthProperty().bind(lowerTable.widthProperty().multiply(0.166666));
        column13.prefWidthProperty().bind(lowerTable.widthProperty().multiply(0.166666));
        
        List<Tulokset> tulokset2 = kontrolleri.getJakaumatSimulointiID(id);
        for(Tulokset tulos : tulokset2) {
        	lowerTable.getItems().add(tulos);
        }
        
        lowerTable.getColumns().addAll(column8, column9, column10, column11, column12, column13);
        
        SplitPane root = new SplitPane();
        root.setOrientation(Orientation.VERTICAL);
        root.getItems().addAll(upperTable, lowerTable);
        
		Scene scene = new Scene(root);
        scene.getStylesheets().add("css/styles.css");
		naytto.setScene(scene);
		naytto.showAndWait();
	}
}
