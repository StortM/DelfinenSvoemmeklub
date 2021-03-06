import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class UserInterface extends Application {
    private Stage stage;
    private Scene previous;

    //opretter medlemsliste
    MedlemsListe medlemmer;
    ObservableList<Medlem> ol;

    //Setting size of window
    private Rectangle2D primScreenBounds;
    private double stagesizex;
    private double stagesizey;

    public void start(Stage primaryStage) throws Exception{
        medlemmer = new MedlemsListe();
        ol = FXCollections.observableArrayList();
        this.stage = primaryStage;
        primScreenBounds = Screen.getPrimary().getVisualBounds();
        stagesizex = primScreenBounds.getWidth() / 2;
        stagesizey = primScreenBounds.getHeight() / 1.5;
        stage.setTitle("Svømmeklubben System");

        sceneManager("login");
    }

    public void sceneManager(String side){
        try {
            switch (side) {
                case "login":
                case "logud":
                    loginSide();
                    break;

                case "formand":
                    formandStartSide();
                    break;
                case "kasser":
                    kasserStartSide();
                    break;

               case "visMedlemmerKasser":
                    visMedlemmer("Kasser");
                    break;
                case "visMedlemmerFormand":
                    visMedlemmer("Formand");
                    break;

                case "opretMedlem":
                    opretMedlemForm();
                    break;
                case "back":
                    showStage(previous);
            }
        }catch(Exception e){
            System.out.print("det fucked: " + e);
        }
    }

    public void updateStage(Parent root){

        if(previous != stage.getScene()) {
            previous = stage.getScene();
        }

        Scene scene = new Scene(root, stagesizex, stagesizey);
        showStage(scene);
    }
    public void showStage(Scene scene){
        stage.setScene(scene); // ændre scene størrelse
        stage.show();

    }

    public void loginSide() throws IOException {
        String passwordFormand = "test";
        String passwordKasserer = "test2";


        // Lav knapper og style dem
        Button formand = new Button("Formand");
        Button kasserer = new Button("Kasserer");
        formand.setMinSize(150, formand.getHeight());
        kasserer.setMinSize(150, kasserer.getHeight());
        formand.setFont(Font.font("", FontWeight.THIN, 20));
        kasserer.setFont(Font.font("", FontWeight.THIN, 20));

        // Lav "wrapper" grid pane som v-bokse pakkes ind i
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: ALICEBLUE; ");

        // Lav vbox til buttons (venstre side af programmet) samt style det og tilføj knapper
        VBox vBoxButtons = new VBox();
        vBoxButtons.setMaxSize(stagesizex / 2, stagesizey);
        vBoxButtons.setMinSize(stagesizex / 2, stagesizey);
        vBoxButtons.setAlignment(Pos.CENTER);
        vBoxButtons.setSpacing(150);
        VBox.setMargin(formand, new Insets(20, 20, 20, 20));
        VBox.setMargin(kasserer, new Insets(20, 20, 20, 20));
        vBoxButtons.getChildren().addAll(formand, kasserer);

        // Lav vbox til logo (højre side af programmet)
        VBox vBoxLogo = new VBox();
        vBoxLogo.setMaxSize(stagesizex / 2, stagesizey);

        // Indlæs billede via URL og brug imageView til at vise billedet. Style billedet.
        InputStream logo = new URL("https://i.imgur.com/jb8srK2.png").openStream();
        Image image = new Image(logo);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(stagesizex / 2);
        imageView.setFitHeight(stagesizey);
        imageView.setPreserveRatio(false);

        // Tilføj billede til højre vbox
        vBoxLogo.getChildren().add(imageView);

        // TIlføj vboxe til wrapper grid
        grid.add(vBoxButtons, 0, 0);
        grid.add(vBoxLogo, 1, 0);
        grid.setGridLinesVisible(false);


        /* https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html#resultProperty--
        Once a Dialog is instantiated and fully configured, the next step is to show it. More often than not,
        dialogs are shown in a modal and blocking fashion.
        'Modal' means that the dialog prevents user interaction with the owning application whilst it is showing,
        and 'blocking' means that code execution stops at the point in which the dialog is shown.
        This means that you can show a dialog, await the user response,
        and then continue running the code that directly follows the show call,
        giving developers the ability to immediately deal with the user input from the dialog (if relevant).

        JavaFX dialogs are modal by default (you can change this via the initModality(javafx.stage.Modality) API).
        To specify whether you want blocking or non-blocking dialogs,
        developers simply choose to call showAndWait() or show() (respectively).
        By default most developers should choose to use showAndWait(), given the ease of coding in these situations.
        Shown below is three code snippets, showing three equally valid ways of showing a dialog:
         */

        // Dialog box til login
        Dialog<String> dialogLogin = new Dialog<>();
        dialogLogin.setTitle("Login");

        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuller", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogLogin.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

        HBox dialogHBox = new HBox();
        dialogHBox.setSpacing(10);
        Label passwordLabel = new Label("Kodeord: ");
        PasswordField passwordField = new PasswordField();
        dialogHBox.getChildren().addAll(passwordLabel, passwordField);
        HBox.setMargin(passwordField, new Insets(10, 10, 0, 0));
        HBox.setMargin(passwordLabel, new Insets(10, 10, 0, 10));

        Platform.runLater(() -> passwordField.requestFocus());
        Node loginButton = dialogLogin.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialogLogin.getDialogPane().setContent(dialogHBox);

        // Når tryk på logind, valider om kodeord passer til password variabler.
        // Hvis validering er OK, ændre scene til kasser/formand brugerside.
        dialogLogin.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                String tempPassword = passwordField.getText();
                passwordField.clear();
                return tempPassword;
            } else if (dialogButton == cancelButtonType) {
                passwordField.clear();
            }
            return null;
        });

        /* Freaky shit going on here
        ActionEvent for begge knapper som viser dialogboksen
        Ved tryk på knap vises en login dialog boks
        Det password der indtastes i passwordField valideres imod en String variabel som indeholder koden
        for henholdsvis formand og kasserer.
         */
        formand.setOnAction(e -> {
            Optional<String> result = dialogLogin.showAndWait();


            result.ifPresent(password -> {
                if (password.equals(passwordFormand)) {
                    sceneManager("formand");
                } else {
                    dialogBox("Fejl", "Forkert kodeord. Prøv igen.");
                }
            });
        });

        kasserer.setOnAction(e -> {
            Optional<String> result = dialogLogin.showAndWait();

            result.ifPresent(password -> {
                if (password.equals(passwordKasserer)) {
                    sceneManager("kasser");
                } else {
                    dialogBox("Fejl", "Forkert kodeord. Prøv igen.");
                }
            });

        });

        // Instansier scenen, sæt primarystage til at vise den og start stage
        updateStage(grid);
    }

    public void kasserStartSide() {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);
        Button visMedlemKasser = new Button("Vis Medlemmer");
        Button logUdKasser = new Button("Log Ud");
        //gui kontrol
        visMedlemKasser.setOnAction(e -> { sceneManager("visMedlemmerKasser");});
        logUdKasser.setOnAction(e -> { sceneManager("logud"); });

        visMedlemKasser.setMinSize(150, visMedlemKasser.getHeight());
        visMedlemKasser.setFont(Font.font("", FontWeight.THIN, 20));
        logUdKasser.setMinSize(150, logUdKasser.getHeight());
        logUdKasser.setFont(Font.font("", FontWeight.THIN, 20));

        grid.setVgap(125);
        grid.setHgap(-10);
        grid.setMinSize(720, 580);
        grid.setAlignment(Pos.CENTER);

        grid.add(visMedlemKasser, 0, 0);
        grid.add(logUdKasser, 0, 1);
        grid.setStyle("-fx-background-color: ALICEBLUE;");

        updateStage(grid);
    }

    public void formandStartSide() {
        GridPane grid1 = new GridPane();
        grid1.setGridLinesVisible(false);
        Button opretMedlem = new Button("Opret Medlem");
        Button visMedlemFormand = new Button("Vis Medlemmer");
        Button logUd = new Button("Log Ud");

        opretMedlem.setMinSize(150, opretMedlem.getHeight());
        opretMedlem.setFont(Font.font("", FontWeight.THIN, 20));
        visMedlemFormand.setMinSize(150, visMedlemFormand.getHeight());
        visMedlemFormand.setFont(Font.font("", FontWeight.THIN, 20));
        logUd.setMinSize(150, logUd.getHeight());
        logUd.setFont(Font.font("", FontWeight.THIN, 20));

        // gui kontrol
        opretMedlem.setOnAction(e -> { sceneManager("opretMedlem");});
        visMedlemFormand.setOnAction(e -> { sceneManager("visMedlemmerFormand");});
        logUd.setOnAction(e -> { sceneManager("logud"); });

        grid1.setVgap(125);
        grid1.setHgap(-10);
        grid1.setMinSize(720, 580);
        grid1.setAlignment(Pos.CENTER);

        grid1.add(opretMedlem, 0, 0);
        grid1.add(visMedlemFormand, 2, 0);
        grid1.add(logUd, 1, 1);
        grid1.setStyle("-fx-background-color: ALICEBLUE;");
        updateStage(grid1);
    }

    public void dialogBox(String title, String message) {
        // Dialogbox der kan bruges til fejlbeskeder, success beskeder m.m.
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle(title);

        Label label = new Label(message);
        Button button = new Button("OK");
        VBox vb = new VBox();

        label.setPadding(new Insets(10, 20, 20, 20));
        button.setMinSize(75, button.getHeight());


        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(label, button);
        dialog.getDialogPane().setContent(vb);

        button.setOnAction(e -> {
            dialog.close();
            dialog.setResult(Boolean.TRUE);
        });

        // ButtonType bt = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        // dialog.getDialogPane().getButtonTypes().add(bt);
        dialog.showAndWait();
    }

    public void opretMedlemForm() throws Exception{
        opretMedlemForm(null);
    }
    public void opretMedlemForm(Medlem medlem) throws Exception {
        //Labels og fields

        Text nameLabel = new Text("Navn");
        TextField nameText = new TextField();
        nameText.setPromptText("Navn");

        Text ageLabel = new Text("Fødselsdato");
        DatePicker datepicker = new DatePicker();
        datepicker.setPromptText("Dato");
        datepicker.setPrefSize(270, 20);

        Text vejNavnLabel = new Text("Vejnavn");
        TextField vejNavnText = new TextField();
        vejNavnText.setPromptText("Vejnavn");

        Text nummerLabel = new Text("Husnummer");
        TextField nummerText = new TextField();
        nummerText.setPromptText("Husnummer");

        Text postNummerLabel = new Text("Postnummer");
        TextField postNummerText = new TextField();
        postNummerText.setPromptText("Postnummer");

        Text emailLabel = new Text("E-Mail");
        TextField emailText = new TextField();
        emailText.setPromptText("Dinmail@mail.dk");

        //Køn label og en gruppe for denne
        Text genderLabel = new Text("Køn");

        ToggleGroup groupGender = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Mand");
        maleRadio.setToggleGroup(groupGender);
        RadioButton femaleRadio = new RadioButton("Kvinde");
        femaleRadio.setToggleGroup(groupGender);
        RadioButton otherRadio = new RadioButton("Andet");
        otherRadio.setToggleGroup(groupGender);

        //hbox til køn
        HBox hboxGender = new HBox();
        hboxGender.setSpacing(10);
        hboxGender.getChildren().addAll(maleRadio, femaleRadio, otherRadio);

        Text medlemstypeLabel = new Text("Medlemstype");

        //Choice box til drop down
        ChoiceBox medlemsTypeBox = new ChoiceBox();
        medlemsTypeBox.getItems().addAll("Aktiv", "Passiv");
        medlemsTypeBox.setPrefWidth(270);


        Text aktivitetsTypeLabel = new Text("Aktivitetstype");

        //Choice box til drop down
        ChoiceBox aktivitetsTypeBox = new ChoiceBox();
        aktivitetsTypeBox.getItems().addAll("Konkurrence", "Motionist");

        aktivitetsTypeBox.setPrefWidth(270);

        final boolean redigere;
        final int index;

        if(medlem != null) {
            redigere = true;
            index = medlemmer.getListe().indexOf(medlem);

            nameText.setText(medlem.getNavn());
            datepicker.setValue(medlem.getFodselsdato());
            vejNavnText.setText(medlem.getVejNavn());
            nummerText.setText(medlem.getHusNr());
            postNummerText.setText(medlem.getPostNr());
            emailText.setText(medlem.getEmail());

            switch(medlem.getGender()){
                case "Mand":
                    maleRadio.setSelected(true);
                    break;
                case "Kvinde":
                    femaleRadio.setSelected(true);
                    break;
                case "Andet":
                    otherRadio.setSelected(true);
                    break;}
            medlemsTypeBox.setValue(medlem.getAktivitetstype());
            aktivitetsTypeBox.setValue(medlem.getMedlemstype());
        }else{
            redigere = false;
            index = 0;
        }

         //knapper
        Button buttonGem = new Button("Gem");
        Button buttonAnnuller = new Button("Annuller");
        buttonGem.setPrefSize(180, 20);
        buttonAnnuller.setPrefSize(80, 20);
        buttonGem.setDisable(true);

        // Disabler gem button indtil alle fields er udfyldt
        BooleanBinding booleanBind = nameText.textProperty().isEmpty().
                or(vejNavnText.textProperty().isEmpty()).
                or(emailText.textProperty().isEmpty()).
                or(nummerText.textProperty().isEmpty()).
                or(aktivitetsTypeBox.valueProperty().isNull()).
                or(groupGender.selectedToggleProperty().isNull()).
                or(datepicker.valueProperty().isNull());
        buttonGem.disableProperty().bind(booleanBind);


        //hbox til knapper
        HBox hboxKnap = new HBox();
        hboxKnap.setSpacing(10);
        hboxKnap.getChildren().addAll(buttonGem, buttonAnnuller);
        hboxKnap.setAlignment(Pos.BOTTOM_RIGHT);

        //Date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        //Make button do stuff
        buttonGem.setOnAction((event ->{

                    String navn = nameText.getText();
                    LocalDate dato = datepicker.getValue();
                    String gender = ((RadioButton) groupGender.getSelectedToggle()).getText();
                    String vejNavn = (vejNavnText.getText());
                    String husNr = nummerText.getText();
                    String postNr = postNummerText.getText();
                    String mail = emailText.getText();
                    String aktivitet = aktivitetsTypeBox.getValue().toString();
                    String medlemtype = medlemsTypeBox.getValue().toString();

                if (medlemmer.verificerOpretMedlemInput(navn, dato, gender, vejNavn, husNr, postNr, mail, aktivitet, medlemtype)) {
                    if(!redigere) {
                        medlemmer.opretMedlem(navn, dato, gender, vejNavn, husNr, postNr, mail, aktivitet, medlemtype);
                        dialogBox("", "Medlem oprettet succesfuldt!");
                    }else{
                        medlemmer.redigerMedlem(index, navn, dato, gender, vejNavn, husNr, postNr, mail, aktivitet, medlemtype, medlem.getBetalingsHistorik());
                        dialogBox("", "Medlem redigeret succesfuldt!");
                    }

                    sceneManager("formand");
                }
                // Nothing
        }
        ));

        //Make method go to formand side
        buttonAnnuller.setOnAction((event -> {
            sceneManager("back");
        }));

        //laver gridpane
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(stagesizex / 2, stagesizey);

        //padding
        gridPane.setPadding(new Insets(40));

        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(20);
        gridPane.setHgap(10);

        //Setting the Grid alignment
        gridPane.setAlignment(Pos.TOP_LEFT);

        //nodes i grid
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameText, 1, 0);

        gridPane.add(ageLabel, 0, 1);
        gridPane.add(datepicker, 1, 1);

        gridPane.add(vejNavnLabel, 0, 2);
        gridPane.add(vejNavnText, 1, 2);

        gridPane.add(nummerLabel, 0, 3);
        gridPane.add(nummerText, 1, 3);

        gridPane.add(postNummerLabel, 0, 4);
        gridPane.add(postNummerText, 1, 4);

        gridPane.add(emailLabel, 0, 5);
        gridPane.add(emailText, 1, 5);

        gridPane.add(genderLabel, 0, 6);
        gridPane.add(hboxGender, 1, 6);

        gridPane.add(medlemstypeLabel, 0, 7);
        gridPane.add(medlemsTypeBox, 1, 7);

        gridPane.add(aktivitetsTypeLabel, 0, 8);
        gridPane.add(aktivitetsTypeBox, 1, 8);

        gridPane.add(hboxKnap, 1, 10);

        gridPane.setGridLinesVisible(false);
        gridPane.setMaxSize(stagesizex / 2, stagesizey);

        //imporeterer billede
        InputStream logo = new URL("https://i.imgur.com/jb8srK2.png%22").openStream();
        Image image = new Image(logo);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(stagesizex / 2);
        imageView.setFitHeight(stagesizey);
        imageView.setPreserveRatio(false);

        //laver ekstra gridpane
        GridPane bigGrid = new GridPane();
        bigGrid.add(gridPane, 0, 0);
        bigGrid.add(imageView, 1, 0);

        //bigGrid.getStylesheets().add("sample/CSS.css");
        updateStage(bigGrid);
        }

    public void visMedlemmer(String bruger) throws Exception{
        VBox root = new VBox();
        String[] columns;
        HBox options;

        TableView tb = new TableView();
        tb.setId("table");
        TextField tf = new TextField();
        tf.setPromptText("S\u00f8g");
        tf.setId("Search");
        //opdater

        if(bruger.equals("Formand")) {
            options = FXMLLoader.load(getClass().getResource("Formand.fxml"));
            columns = new String[] {"Navn", "Fodselsdato", "Adresse", "Medlemstype", "Aktivitetstype"};

            Button rediger = (Button) options.lookup("#redigerInfo");
            Button slet = (Button) options.lookup("#slet");

            rediger.setOnAction((event ->{
                if (tb.getSelectionModel().isEmpty()) {
                    dialogBox("Fejl", "Du har ikke valgt et medlem.");
                } else {
                    Medlem m = ol.get(tb.getSelectionModel().getSelectedIndex());
                    try {
                        opretMedlemForm(m);
                    }catch(Exception e){
                        dialogBox("Exception", e.toString());
                    }
                }
            }));
            slet.setOnAction(event ->{
                if (tb.getSelectionModel().isEmpty()) {
                    dialogBox("Fejl", "Du har ikke valgt et medlem.");
                } else {
                    try {
                        medlemmer.sletMedlem(medlemmer.getListe().indexOf(ol.get(tb.getSelectionModel().getSelectedIndex())));
                        dialogBox("", "Medlem slettet succesfuldt!");
                        sceneManager("back");
                    }catch(Exception e){
                        dialogBox("Exception", e.toString());
                    }
                }
            });
            Button tilbage = (Button) options.lookup("#tilbageButton");
            tilbage.setOnAction((event ->{
                sceneManager("formand");
            }));
        }
        //Lav en udvidelse af eventet, så den finder en liste med restancemedlemmer
        else {
            options = FXMLLoader.load(getClass().getResource("Kasser.fxml"));
            columns = new String[]{"Navn", "Pris", "Adresse", "Email", "Aktivitetstype", "Udloebsdato"};
            RadioButton restance = (RadioButton) options.lookup("#restanceMedlem");
            Button bankInfo = (Button) options.lookup("#bankInfo");
            Button sendEmail = (Button) options.lookup("#sendRegning");
            sendEmail.setOnAction((event -> {
                if(tb.getSelectionModel().isEmpty()) {
                    dialogBox("Fejl", "Du har ikke valgt et medlem.");
                } else {
                    Medlem person = ol.get(tb.getSelectionModel().getSelectedIndex());
                    dialogBox("","Email sendt til "+person.getNavn());
                    SendEmail blah = new SendEmail(person.getEmail());

                }

            }));
            restance.setOnAction((event -> {
                if(restance.isSelected()){generateTable(columns, root, medlemmer.restance());}
                else{generateTable(columns,root,medlemmer.getListe());}
            }));
            bankInfo.setOnAction(event ->{
                if(tb.getSelectionModel().isEmpty()) {
                    dialogBox("Fejl", "Du har ikke valgt et medlem.");
                } else {
                    try {
                        Medlem m = ol.get(tb.getSelectionModel().getSelectedIndex());
                        popUpBank(m);

                    }catch(Exception e){
                        dialogBox("Exception", e.toString());
                    }
                }
                    });

            Button tilbage = (Button) options.lookup("#tilbageButton");
            tilbage.setOnAction((event ->{
                sceneManager("kasser");
            }));

        }

        root.getChildren().add(tf);
        root.getChildren().add(tb);
        root.getChildren().add(options);

        tf.setOnAction((event -> {
            generateTable(columns, root, medlemmer.filtrerListe(tf.getText()));
        }));

        generateTable(columns, root, medlemmer.getListe());
        updateStage(root);
    }

    public void popUpBank(Medlem m) throws Exception{
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Bank Info");
        ObservableList<Betaling> obs = FXCollections.observableArrayList();

        VBox top = FXMLLoader.load(getClass().getResource("BankInfo.fxml"));
        TableView table = (TableView) top.lookup("#bankTable");

        String[] cols = {"Beloeb", "BetalingsDato", "BankNummer"};

        obs.setAll(m.getBetalingsHistorik());
        table.setItems(obs);

        for(String s: cols){
            TableColumn<String, Betaling> col = new TableColumn<String, Betaling>(s);
            col.setCellValueFactory(new PropertyValueFactory<>(s));
            if(s.equals("Beloeb")) {
                col.setText("Bel\u00F8b");
            } else if (s.equals("BetalingsDato")) {
                col.setText("Betalings Dato");
            } else {
                col.setText("Bank Nummer");
            }
            table.getColumns().add(col);
        }
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button luk = (Button) top.lookup("#lukdialog");
        luk.setOnAction(e -> {
            dialog.setResult(Boolean.TRUE);
        });

        Button addBetaling = (Button) top.lookup("#addBetaling");

        TextField beløbText = (TextField) top.lookup("#addBeløb");
        DatePicker datoText = (DatePicker) top.lookup("#addDato");
        TextField bankText = (TextField) top.lookup("#addBank");

        //auto udfylder information for kasseren, der så kan vælge hvad der skal ændres
        beløbText.setText(Double.toString(m.getPris()));
        datoText.setValue(LocalDate.now());
        bankText.setText(m.getBetalingsHistorik().get(m.getBetalingsHistorik().size()-1).getBankNummer());

        addBetaling.setDisable(true);

        BooleanBinding booleanBind = beløbText.textProperty().isEmpty().
                or(datoText.valueProperty().isNull()).
                or(bankText.textProperty().isEmpty());
        addBetaling.disableProperty().bind(booleanBind);
;

        addBetaling.setOnAction((n -> {
            m.opretBetaling(Double.parseDouble(beløbText.getText()), datoText.getValue(), bankText.getText());
            obs.setAll(m.getBetalingsHistorik());
                }));


        dialog.getDialogPane().setContent(top);
        dialog.showAndWait();
    }

    public void generateTable(String[] columns, Parent root, ArrayList<Medlem> al){
        TableView table = (TableView)root.lookup("#table");
        table.getColumns().clear();

        ol.setAll(al);
        table.setItems(ol);
       table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(String s: columns) {
            TableColumn<String, Medlem> col = new TableColumn<String, Medlem>(s);
            col.setCellValueFactory(new PropertyValueFactory<>(s));
            if(s.equals("Fodselsdato")){
                col.setText("F\u00F8dselsdato");
            }
            if(s.equals("Udloebsdato")){
                col.setText("Udl\u00F8bsdato");
            }
            table.getColumns().add(col);
        }
    }

    public void stop(){
        medlemmer.opdaterListe();
    }
}