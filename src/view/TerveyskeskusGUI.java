package view;

import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import simu.Trace;
import simu.Trace.Level;
import simu.Tulokset;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
/**
 * 
 * TerveyskeskusGUI-luokan tarkoituksena on näyttää simulaattori graafisesti näytöllä käyttäjälle. Graafinenkäyttöliittymä
 * tarjoaa käyttäjälle syöttökenttiä, jiden avulla käyttäjä pystyy vaikuttaa simulaattorin lopputuloksiin. Näytölle
 * tulee myös visuaalisesti piirrettyä tietoa, mitä moottorissa tapahtuu ja lopuksi simuloinnin tulokset.
 * 
 * @author Eric Keränen
 * @version 1.4
 *
 */
public class TerveyskeskusGUI extends Application implements ITerveyskeskusGUI{

	// Tarvitaan tietokannan aktivoimiseen
	private boolean kaynnistetty = false;
	// Tarvitaan tieotkantaan tallentamisessa
	private boolean tallennettu = false;
	
	// Kontrollerin esittely (tarvitaan käyttöliittymässä)
	private IKontrolleri kontrolleri;

	//----------- Käyttöliittymäkomponentit ----------------//
	// --- Yläosa --- //
	private Label syotto, jakaumat;
	
	private TextField aika, viive, huoneidenMaara, saapumisJakauma;
	private Label aikaText, viiveText, huoneetText, saapumisText;
	private Button kaynnistaButton, hidastaButton, nopeutaButton;
	
	private TextField vastOtJakauma, rontgenJakauma, kassaJakauma, laakarienJakaumat;
	private Label vastOtText, rontgenText, kassaText, laakariText;
	
	private TextField simulointiAika;
	private Label simulointiAikaText;
	
	private Menu menuTiedosto;
	private Menu menuTietokanta;
	
    private TietokantaGUI tietokanta;
    private PoistoGUI poisto;
    private ValintaGUI valinta;
    private OhjeGUI ohje;
	private Visualisointi naytto;
	// -- Alaosa --- //
	private TableView tableView;
	//-----------------------------------------------------//
	
	/**
	 * Asettaa tuloksien tason ja luo koko ohjelmalle kontrollerin.
	 */
	@Override
	public void init(){
		Trace.setTraceLevel(Level.ERR);
		kontrolleri = new Kontrolleri(this);
	}

	/**
	 * Luo päänäytön, jossa on graafinenkäyttöliittymä käyttäjälle. Päänäytöllä on seuraavat elementit:
	 * 
	 * - Yläpalkki, jossa kaksi valikkoa. Toisessa voi poistua ohjelmasta, avata ohjeet ja toisessa vuorovaikuttaa tietokannan kanssa
	 * - Visualisointialue, johon piirretään palloja ja poistetaan niitä, riippuen siitä mitä moottorissa tapahtuu
	 * - Useita tekstikenttä alueita, joihin käyttäjä voi kirjoittaa omat arvonsa, joiden perusteella simulaattori toimii
	 * - Napit, joiden avulla simuloinnin voi käynnistää ja sen kulkua nopeuttaa / hidastaa
	 * - Tulos alue, jossa näkyy simulaattorin tulokset, kun simulointi on valmis
	 */
	@Override
	public void start(Stage primaryStage) {
		// Käyttöliittymän rakentaminen
		try {
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			    	if(kaynnistetty) { kontrolleri.suljeDAO(); }
			        Platform.exit();
			        System.exit(0);
			    }
			});	
			
			primaryStage.setTitle("Terveyskeskussimulaattori");
			primaryStage.setResizable(false);
			
			// --------------- MenuBar --------------- //
	        
	        menuTiedosto = new Menu("Tiedosto");
	        MenuItem menuItem1 = new MenuItem("Ohje");
	        menuItem1.setOnAction(e -> ohje = new OhjeGUI());
	        MenuItem menuItem2 = new MenuItem("Poistu");
	        menuItem2.setOnAction(e -> kontrolleri.poistu());
	        menuTiedosto.getItems().add(menuItem1);
	        menuTiedosto.getItems().add(menuItem2);
	        
	        menuTietokanta = new Menu("Tietokanta");
	        menuTietokanta.setDisable(true);
	        MenuItem menuItem3 = new MenuItem("Näytä tietokanta");
	        menuItem3.setOnAction(e -> valinta = new ValintaGUI(kontrolleri));
	        MenuItem menuItem4 = new MenuItem("Lisää tietokantaan");
	        menuItem4.setOnAction(e -> {
	        	if(!tallennettu) {
	        		kontrolleri.lisaaTietokantaan();
	        		tallennettu = true;
	        		naytaInfo("Viimeisin simulointi lisättiin tietokantaan onnistuneesti.");
	        	}else{
	        		naytaAlert("Et voi tallentaa samoja tuloksia kahteen kertaan!");
	        	}
	        });
	        MenuItem menuItem5 = new MenuItem("Poista tietokannasta");
	        menuItem5.setOnAction(e -> poisto = new PoistoGUI(kontrolleri, false, "Anna rivin ID, joka poistetaan"));
	        MenuItem menuItem6 = new MenuItem("Poista tietokanta");
	        menuItem6.setOnAction(e -> poisto = new PoistoGUI(kontrolleri, true, "Haluatko varmasti poistaa kaiken tietokannasta?"));
	        menuTietokanta.getItems().add(menuItem3);
	        menuTietokanta.getItems().add(menuItem4);
	        menuTietokanta.getItems().add(menuItem5);
	        menuTietokanta.getItems().add(menuItem6);
	        
	        MenuBar menuBar = new MenuBar(menuTiedosto, menuTietokanta);
	        menuBar.getStyleClass().add("menubar");
						
			// --------------- SplitPanen yläosa --------------- //
	        
	        // --- Hboxin vasepuoli --- //
	        
	        naytto = new Visualisointi(800,400);
			
			// --- HBoxin oikeapuoli --- //
			
			// --- Painikkeet --- //
			kaynnistaButton = new Button();
			kaynnistaButton.setText("Käynnistä");
			kaynnistaButton.setFont(Font.font("Futura", FontWeight.EXTRA_BOLD, 16));
			kaynnistaButton.setMinWidth(150);
			kaynnistaButton.setDefaultButton(true);
			kaynnistaButton.setOnAction(e -> {
				if(tarkistaKaikki()) {
            		kontrolleri.kaynnistaSimulointi();
        	        menuTietokanta.setDisable(false);
        	        kaynnistaButton.setDisable(true);
        	        kaynnistetty = true;
        	        tallennettu = false;
            	}else {
            		System.err.println("VIRHE!");
            	}
			});

			nopeutaButton = new Button();
			nopeutaButton.setText("+");
			nopeutaButton.setFont(Font.font("Futura", FontWeight.EXTRA_BOLD, 16));
			nopeutaButton.setMinWidth(70);
			nopeutaButton.setOnAction(e -> kontrolleri.nopeuta());
			
			hidastaButton = new Button();
			hidastaButton.setText("-");
			hidastaButton.setFont(Font.font("Futura", FontWeight.EXTRA_BOLD, 16));
			hidastaButton.setMinWidth(70);
			hidastaButton.setOnAction(e -> kontrolleri.hidasta());
			
			HBox nopeusPainikkeet = new HBox();
			nopeusPainikkeet.setSpacing(10);
			nopeusPainikkeet.getChildren().addAll(nopeutaButton, hidastaButton);
	        
			// --- Syötöt --- //
			syotto = new Label("Syöttö");
			syotto.setFont(Font.font("Futura", FontWeight.BOLD, 20));
			syotto.setPrefWidth(150);
			syotto.getStyleClass().add("label-bottom-border");
			
	        aika = new TextField("50000");
	        aika.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        aika.setPrefWidth(150);
	        
	        aikaText = new Label("Aika (ms)");
	        aikaText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        aikaText.setAlignment(Pos.BASELINE_LEFT);
	        aikaText.setPrefWidth(150);

	        viive = new TextField("2");
	        viive.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        viive.setPrefWidth(150);
	        
	        viiveText = new Label("Viive (ms)");
	        viiveText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        viiveText.setAlignment(Pos.BASELINE_LEFT);
	        viiveText.setPrefWidth(150);
	        
	        huoneidenMaara = new TextField("2");
	        huoneidenMaara.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        huoneidenMaara.setPrefWidth(150);
	        
	        huoneetText = new Label("Huoneet (1-3)");
	        huoneetText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        huoneetText.setAlignment(Pos.BASELINE_LEFT);
	        huoneetText.setPrefWidth(150);
	        
	        saapumisJakauma = new TextField("11");
	        saapumisJakauma.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        saapumisJakauma.setPrefWidth(150);
	        
	        saapumisText = new Label("Saapumis jakauma");
	        saapumisText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        saapumisText.setAlignment(Pos.BASELINE_LEFT);
	        saapumisText.setPrefWidth(160);
	        
	        // --- Huoneiden jakaumat --- //
	        jakaumat = new Label("Jakaumat");
	        jakaumat.setFont(Font.font("Futura", FontWeight.BOLD, 20));
	        jakaumat.setPrefWidth(150);
	        jakaumat.getStyleClass().add("label-bottom-border");
	        
	        vastOtJakauma = new TextField("10");
	        vastOtJakauma.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        vastOtJakauma.setPrefWidth(150);
	        
	        vastOtText = new Label("Vastaanotto");
	        vastOtText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        vastOtText.setAlignment(Pos.BASELINE_LEFT);
	        vastOtText.setPrefWidth(150);
	        
	        rontgenJakauma = new TextField("120");
	        rontgenJakauma.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        rontgenJakauma.setPrefWidth(150);
	        
	        rontgenText = new Label("Röntgensali");
	        rontgenText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        rontgenText.setAlignment(Pos.BASELINE_LEFT);
	        rontgenText.setPrefWidth(150);
	        
	        kassaJakauma = new TextField("10");
	        kassaJakauma.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        kassaJakauma.setPrefWidth(150);
	        
	        kassaText = new Label("Kassa");
	        kassaText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        kassaText.setAlignment(Pos.BASELINE_LEFT);
	        kassaText.setPrefWidth(150);
	        
	        laakarienJakaumat = new TextField("20");
	        laakarienJakaumat.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        laakarienJakaumat.setPrefWidth(150);
	        
	        laakariText = new Label("Lääkärinhuoneet");
	        laakariText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        laakariText.setAlignment(Pos.BASELINE_LEFT);
	        laakariText.setPrefWidth(150);

	        // --- Simulointiaika --- //
	        simulointiAika = new TextField("---");
	        simulointiAika.setFont(Font.font("Futura", FontWeight.NORMAL, 14));
	        simulointiAika.setPrefWidth(150);
	        
	        simulointiAikaText = new Label("Simulointiaika:");
	        simulointiAikaText.setFont(Font.font("Futura", FontWeight.NORMAL, 16));
	        simulointiAikaText.setAlignment(Pos.BASELINE_RIGHT);
	        simulointiAikaText.setPrefWidth(150);
	        
	        // --- Täytetään GridPane --- //
	        
	        GridPane grid = new GridPane();
	        grid.setAlignment(Pos.CENTER);
	        grid.setVgap(18);
	        grid.setHgap(10);

	        grid.add(syotto, 0, 0);
	        grid.add(aika, 0, 1);
	        grid.add(aikaText, 1, 1);
	        grid.add(viive, 0, 2);
	        grid.add(viiveText, 1, 2);
	        grid.add(huoneidenMaara, 0, 3);
	        grid.add(huoneetText, 1, 3);
	        grid.add(saapumisJakauma, 0, 4);
	        grid.add(saapumisText, 1, 4);
	        
	        grid.add(kaynnistaButton, 0, 6);
	        grid.add(nopeusPainikkeet, 0, 7);
	        
	        grid.add(simulointiAikaText, 1, 6);
	        grid.add(simulointiAika, 2, 6);
	        
	        grid.add(jakaumat, 2, 0);
	        grid.add(vastOtJakauma, 2, 1);
	        grid.add(vastOtText, 3, 1);
	        grid.add(rontgenJakauma, 2, 2);
	        grid.add(rontgenText, 3, 2);
	        grid.add(kassaJakauma, 2, 3);
	        grid.add(kassaText, 3, 3);
	        grid.add(laakarienJakaumat, 2, 4);
	        grid.add(laakariText, 3, 4);

	        // --- Täytetään Hbox --- //
	        
	        HBox hBox = new HBox();
	        hBox.getStyleClass().add("hbox");
	        hBox.setPadding(new Insets(15, 12, 15, 12));
	        hBox.setSpacing(10);
	        hBox.getChildren().addAll(naytto, grid);
	        
	        // --------------- SplitPanen alaosa --------------- //
	        
	        tableView = new TableView();
	        Label placeHolderi = new Label("Simuloinnin tulokset tulevat tähän");
	        tableView.setPlaceholder(placeHolderi);
	        
	        TableColumn<String, Tulokset> column1 = new TableColumn<>("Palvelupisteen nimi");
	        TableColumn<String, Tulokset> column2 = new TableColumn<>("Asiakkaita");
	        TableColumn<String, Tulokset> column3 = new TableColumn<>("Aktiiviaika");
	        TableColumn<String, Tulokset> column4 = new TableColumn<>("Keskimääräinen palveluaika");
	        TableColumn<String, Tulokset> column5 = new TableColumn<>("Käyttöaste");
	        TableColumn<String, Tulokset> column6 = new TableColumn<>("Jonon max pituus");
	        TableColumn<String, Tulokset> column7= new TableColumn<>("Jonon keskim. pituus");
	        
	        column1.setCellValueFactory(new PropertyValueFactory<>("palvPisteNimi"));
	        column2.setCellValueFactory(new PropertyValueFactory<>("asLkm"));
	        column3.setCellValueFactory(new PropertyValueFactory<>("akAika"));
	        column4.setCellValueFactory(new PropertyValueFactory<>("kmPalvAika"));
	        column5.setCellValueFactory(new PropertyValueFactory<>("kaAste"));
	        column6.setCellValueFactory(new PropertyValueFactory<>("jonoMax"));
	        column7.setCellValueFactory(new PropertyValueFactory<>("jonoKeskim"));
	        
	        column1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        column2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        column3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        column4.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        column5.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        column6.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        column7.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1428));
	        
	        // --- Täytetään tableView --- //
	        
	        tableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7);
	        
	        // --- Täytetään SplitPane --- //
	        
	        SplitPane splitPane = new SplitPane();
	        splitPane.setOrientation(Orientation.VERTICAL);
	        splitPane.setPrefSize(1400, 700);
	        splitPane.getItems().addAll(hBox, tableView);
	        
	        // --- Asetetaan root --- //
	        
	        BorderPane root = new BorderPane();
	        root.setTop(menuBar);
	        root.setCenter(splitPane);

	        // --------------- Asetetaan stage --------------- //
	        
	        Scene scene = new Scene(root);
	        scene.getStylesheets().add("css/styles.css");
	        primaryStage.setScene(scene);
	        primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Aktivoi simuloinnin käynnistysnapin
	 */
	@Override
	public void aktivoiNappi() {
		kaynnistaButton.setDisable(false);
	}

	// Käyttöliittymän rajapintametodit (kutsutaan kontrollerista)

	@Override
	public double getAika(){
		return Double.parseDouble(aika.getText());
	}

	@Override
	public long getViive(){
		return Long.parseLong(viive.getText());
	}
	
	@Override
	public int getHuoneet() {
		return Integer.parseInt(huoneidenMaara.getText());
	}
	
	@Override
	public int getSaapumisjakauma() {
		return Integer.parseInt(saapumisJakauma.getText());
	}
	
	@Override
	public int getVastaanottoJakauma() {
		return Integer.parseInt(vastOtJakauma.getText());
	}
	
	@Override
	public int getRontgenJakauma() {
		return Integer.parseInt(rontgenJakauma.getText());
	}
	
	@Override
	public int getKassaJakauma() {
		return Integer.parseInt(kassaJakauma.getText());
	}
	
	@Override
	public int getLaakarienJakaumat() {
		return Integer.parseInt(laakarienJakaumat.getText());
	}
	
	/**
	 * Tarkistaa kaikkien tekstikenttien arvot, jotta simulaattori ei kaatuisi väärien arvojen vuoksi
	 * @return true, jos kaikki tekstikenttien tulokset ovat valideja, muutoin false ja näytä syy miksi
	 */
	private boolean tarkistaKaikki() {
		try {
			if(Double.parseDouble(aika.getText()) < 1) {
				naytaAlert("Ajan pitää olla enemmän kuin 0!");
				return false;
			}else if(Long.parseLong(viive.getText()) < 0) {
				naytaAlert("Viiveen ei saa olla negatiivinen!");
				return false;
			}else if(Integer.parseInt(huoneidenMaara.getText()) < 1 || Integer.parseInt(huoneidenMaara.getText()) > 3) {
				naytaAlert("Huoneita voi olla vain 1-3!");
				return false;
			}else if(Integer.parseInt(saapumisJakauma.getText()) < 1) {
				naytaAlert("Saapumisjakauman pitää olla enemmän kuin 0!");
				return false;
			}else if(Integer.parseInt(vastOtJakauma.getText()) < 1) {
				naytaAlert("Vastaanoton jakauman pitää olla enemmän kuin 0!");
				return false;
			}else if(Integer.parseInt(rontgenJakauma.getText()) < 1) {
				naytaAlert("Röntgensalin jakauman pitää olla enemmän kuin 0!");
				return false;
			}else if(Integer.parseInt(kassaJakauma.getText()) < 1) {
				naytaAlert("Kassan jakauman pitää olla enemmän kuin 0!");
				return false;
			}else if(Integer.parseInt(laakarienJakaumat.getText()) < 1) {
				naytaAlert("Lääkärinhuoneiden jakaumien pitää olla enemmän kuin 0!");
				return false;
			}else {
				return true;
			}
		}catch(Exception e) {
			naytaAlert("Syötä kaikki tiedot numeerisessa muodossa!");
			return false;
		}
	}
	
	/**
	 * Näyttää näytölle alertin, jos joku tekstikenttien arvo on väärä
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
	
	/**
	 * Asettaa simulointiaika kenttään nykyisen ajan, joka on simulaattorissa
	 * @param aika on aika, joka simulaattorissa on tällähetkellä
	 */
	@Override
	public void setSimulointiAika(double aika) {
		this.simulointiAika.setText(String.valueOf(aika));
	}
	
	/**
	 * Asettaa näkyville tulokset, mitä simulaatori sai
	 * @param tulos on yhden palvelupisteen tulos
	 */
	@Override
	public void setTulokset(Tulokset tulos) {
		this.tableView.getItems().add(tulos);
	}
	
	/**
	 * Tyhjentää tulokset
	 */
	@Override
	public void tyhjennaTulokset() {
		this.tableView.getItems().clear();
	}

	/**
	 * Palauttaa visualisointinäytön, jotta moottori voi piirtää
	 * näytölle jonoja visuaalisesti.
	 * @return näyttö olio
	 */
	@Override
	public Visualisointi getVisualisointi() {
		 return naytto;
	}
	
	/**
	 * Palauttaa arvon, onko simulointi käynnistetty. Tätä tietoa tarvitaan
	 * silloin kun käyttäjä haluaa tarkastella tietokantaa, sillä tietokantaan
	 * ei ole yhteyttä ennenkuin moottori on käynnistetty vähintään kerran
	 * @return true, jos simulointi on käynnistetty kerran, muutoin false
	 */
	@Override
	public boolean getKaynnistetty() {
		if(kaynnistetty) { return true; }
		else{ return false; }
	}
	
	// JavaFX-sovelluksen (käyttöliittymän) käynnistäminen

	public static void main(String[] args) {
		launch(args);
	}	
}
