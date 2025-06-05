import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {

    private MotMystere modelePendu;
    private ArrayList<Image> lesImages;
    public List<String> niveaux;

    private ImageView dessin;
    private Text motCrypte;
    private ProgressBar pg;
    private Clavier clavier;
    private Text leNiveau;
    private Chronometre chrono;
    private BorderPane panelCentral;

    private Button boutonParametres;
    private Button boutonInfo;
    private Button boutonMaison;
    private Button bJouer;

    private ComboBox<String> choixNiveau;

    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");

        this.niveaux = Arrays.asList("Facile", "Moyen", "Difficile");

        this.dessin = new ImageView();
        this.dessin.setImage(this.lesImages.get(0));
        this.dessin.setFitWidth(400);
        this.dessin.setFitHeight(500);
        this.dessin.setPreserveRatio(true);

        this.motCrypte = new Text();
        this.motCrypte.setFont(new Font("Arial", 50));

        this.pg = new ProgressBar();
        this.pg.setPrefWidth(300);

        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ", new ControleurLettres(this.modelePendu, this));

        this.leNiveau = new Text("Niveau : Facile");
        this.leNiveau.setFont(new Font("Arial", 16));

        this.chrono = new Chronometre();

        this.panelCentral = new BorderPane();

        this.choixNiveau = new ComboBox<>();
        this.choixNiveau.getItems().addAll("Facile", "Moyen", "Difficile");
        this.choixNiveau.setValue("Facile");
    }

    private Scene laScene() {
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

  private Pane titre() {
    Label titre = new Label("Jeu du Pendu");
    titre.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 35)); // Taille réduite
    titre.setTextFill(Color.DARKBLUE);
    titre.setAlignment(Pos.TOP_LEFT);
    titre.setPadding(new Insets(5)); // Padding réduit

    HBox ensembleBouton = new HBox();
    BorderPane banniere = new BorderPane();
    banniere.setPadding(new Insets(10)); // Padding réduit de 20 à 10
    banniere.setStyle("-fx-background-color: lightgray;");
    banniere.setPrefHeight(60); // Hauteur fixe réduite

    ImageView boutonInfo = new ImageView(new Image("file:pendu_pour_etu/img/info.png"));
    boutonInfo.setFitWidth(40); // Taille réduite
    boutonInfo.setFitHeight(40);
    this.boutonInfo = new Button();
    this.boutonInfo.setGraphic(boutonInfo);
    this.boutonInfo.setOnAction(new ControleurInfos(this));

    ImageView imgParam = new ImageView(new Image("file:pendu_pour_etu/img/parametres.png"));
    imgParam.setFitWidth(40); // Taille réduite
    imgParam.setFitHeight(40);
    this.boutonParametres = new Button();
    this.boutonParametres.setGraphic(imgParam);
    this.boutonParametres.setOnAction(new ControleurParametre(this));

    ImageView maisonImageView = new ImageView(new Image("file:pendu_pour_etu/img/home.png"));
    maisonImageView.setFitWidth(40); // Taille réduite
    maisonImageView.setFitHeight(40);
    this.boutonMaison = new Button();
    this.boutonMaison.setGraphic(maisonImageView);
    this.boutonMaison.setOnAction(new RetourAccueil(this.modelePendu, this));

    ensembleBouton.getChildren().addAll(this.boutonMaison, this.boutonParametres, this.boutonInfo);
    ensembleBouton.setSpacing(5); // Espacement réduit entre les boutons

    banniere.setLeft(titre);
    banniere.setRight(ensembleBouton);

    return banniere;
}

    private TitledPane leChrono() {
        TitledPane panelChrono = new TitledPane();
        panelChrono.setText("Temps restant");
        panelChrono.setContent(this.chrono);
        panelChrono.setCollapsible(false);
        return panelChrono;
    }

    private Pane fenetreJeu() {
        BorderPane jeu = new BorderPane();
        jeu.setPadding(new Insets(10));

        HBox haut = new HBox();
        haut.setAlignment(Pos.TOP_LEFT);
        haut.getChildren().add(this.motCrypte);
        haut.setPadding(new Insets(10, 0, 10, 50));

        VBox centreGauche = new VBox(10);
        centreGauche.setAlignment(Pos.CENTER);
        centreGauche.setPrefWidth(400);

        centreGauche.getChildren().add(this.dessin);

        this.pg.setPrefWidth(300);
        this.pg.setPrefHeight(25);
        this.pg.setProgress(0.8);

        centreGauche.getChildren().add(this.pg);

        VBox droite = new VBox(15);
        droite.setAlignment(Pos.TOP_LEFT);
        droite.setPadding(new Insets(20));

        droite.getChildren().add(this.leNiveau);
        droite.getChildren().add(this.leChrono());

        this.bJouer = new Button("Nouvelle Partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        droite.getChildren().add(this.bJouer);

        HBox centre = new HBox(30);
        centre.setAlignment(Pos.TOP_LEFT);
        centre.getChildren().addAll(centreGauche);

        VBox basClavier = new VBox(5);
        basClavier.setAlignment(Pos.CENTER);
        basClavier.setPadding(new Insets(10, 0, 10, 0));

        this.clavier.setSpacing(10);
        this.clavier.setPadding(new Insets(10));

        basClavier.getChildren().add(this.clavier);

        jeu.setTop(haut);
        jeu.setCenter(centre);
        jeu.setBottom(basClavier);
        jeu.setRight(droite);
        return jeu;
    }

    private Pane fenetreAccueil() {
        VBox accueil = new VBox(20);
        accueil.setAlignment(Pos.TOP_LEFT);
        accueil.setPadding(new Insets(20));

        TitledPane parametreJeu = new TitledPane();
        parametreJeu.setText("Niveau de Difficulté");
        parametreJeu.setCollapsible(true);
        parametreJeu.setExpanded(true);

        VBox parametreContenu = new VBox(10);
        parametreContenu.setPadding(new Insets(15));

        ToggleGroup groupeNiveau = new ToggleGroup();

        ControleurNiveau controleurNiveau = new ControleurNiveau(this.modelePendu);

        RadioButton radioFacile = new RadioButton("Facile");
        radioFacile.setToggleGroup(groupeNiveau);
        radioFacile.setSelected(true);
        radioFacile.setOnAction(controleurNiveau);

        RadioButton radioMoyen = new RadioButton("Moyen");
        radioMoyen.setToggleGroup(groupeNiveau);
        radioMoyen.setOnAction(controleurNiveau);

        RadioButton radioDifficile = new RadioButton("Difficile");
        radioDifficile.setToggleGroup(groupeNiveau);
        radioDifficile.setOnAction(controleurNiveau);

        parametreContenu.getChildren().addAll(radioFacile, radioMoyen, radioDifficile);

        parametreJeu.setContent(parametreContenu);

        Button boutonJouer = new Button("Lancer une Partie");
        boutonJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));

        boutonJouer.setTooltip(new Tooltip("Commencer le jeu"));

        HBox boutons = new HBox(15);
        boutons.setAlignment(Pos.TOP_LEFT);
        boutons.getChildren().addAll(boutonJouer);

        accueil.getChildren().addAll(boutons, parametreJeu);

        return accueil;
    }

    private void chargerImages(String repertoire) {
        repertoire = "pendu_pour_etu/img";
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil() {
        this.panelCentral.setCenter(this.fenetreAccueil());
        this.chrono.stop();
        this.chrono.resetTime();
        this.motCrypte.setText("");
        this.dessin.setImage(this.lesImages.get(0));
    }

    public void modeJeu() {
        this.panelCentral.setCenter(this.fenetreJeu());

        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);

        this.chrono.start();
    }

    public void modeParametres() {
        this.modeAccueil();
    }

    public void lancePartie() {
        this.modelePendu.setMotATrouver();

        int niveau = this.modelePendu.getNiveau();
        switch (niveau) {
            case MotMystere.FACILE:
                this.chrono.setTempsLimite(4);
                break;
            case MotMystere.MOYEN:
                this.chrono.setTempsLimite(3);
                break;
            case MotMystere.DIFFICILE:
                this.chrono.setTempsLimite(2);
                break;
        }

        this.chrono.start();
        this.majAffichage();
    }

    public void majAffichage() {
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        if (this.chrono.isTempsEcoule()) {
            this.chrono.stop();
            this.popUpMessagePerdu().showAndWait();
            return;
        }

        int nbErreurs = this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants();
        if (nbErreurs < this.lesImages.size()) {
            this.dessin.setImage(this.lesImages.get(nbErreurs));
        }

        double progression = (double) this.modelePendu.getNbErreursRestants() / this.modelePendu.getNbErreursMax();
        this.pg.setProgress(progression);

        String[] niveauxTexte = { "Facile", "Moyen", "Difficile", "Expert" };
        this.leNiveau.setText("Niveau : " + niveauxTexte[this.modelePendu.getNiveau()]);

        this.clavier.desactiveTouches(this.modelePendu.getLettresEssayees());
    }

    public Chronometre getChrono() {
        return this.chrono;
    }

    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    public Alert popUpReglesDuJeu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Règles du jeu");
        alert.setHeaderText("Comment jouer au Pendu ?");
        alert.setContentText("Le but du jeu est de deviner un mot en proposant des lettres.\n\n" +
                "- Vous avez un nombre limité d'essais\n" +
                "- Chaque mauvaise lettre dessine une partie du pendu\n" +
                "- Trouvez le mot avant que le dessin soit terminé !\n\n" +
                "Niveaux :\n" +
                "- Facile : première et dernière lettres révélées et vous avez jusqu'a 4 minutes \n" +
                "- Moyen : première lettre révélée et vous avez jusqu'a 3 minutes\n" +
                "- Difficile : aucune lettre révélée et vous avez jusqu'a 2 minutes");
        alert.getDialogPane().setPrefSize(450, 350);
        alert.setResizable(true);

        return alert;
    }

    public Alert popUpMessageGagne() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Félicitations !");
        alert.setHeaderText("Vous avez gagné !");
        alert.setContentText("Bravo ! Vous avez trouvé le mot : " + this.modelePendu.getMotATrouve() +
                "\nTemps : " + this.chrono.getTempsEcouleSecondes() + "secondes.");
        return alert;
    }

    public Alert popUpMessagePerdu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dommage !");
        String message;
        if (this.chrono.isTempsEcoule()) {
            alert.setHeaderText("Temps écoulé !");
            message = "Le temps imparti est écoulé !\nLe mot à trouver était : " + this.modelePendu.getMotATrouve();
        } else {
            alert.setHeaderText("Vous avez perdu !");
            message = "Le mot à trouver était : " + this.modelePendu.getMotATrouve() + "\nEssayez encore !";
        }

        alert.setContentText(message);
        return alert;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}