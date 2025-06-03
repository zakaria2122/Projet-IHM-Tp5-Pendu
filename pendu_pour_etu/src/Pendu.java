import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;

/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */
    private Button boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */
    private Button bJouer;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono
     * ...)
     */
@Override
public void init() {
    this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
    this.lesImages = new ArrayList<Image>();
    this.chargerImages("./img");

    this.niveaux = Arrays.asList("Facile", "Moyen", "Difficile");

    this.dessin = new ImageView();

    this.motCrypte = new Text();
    this.motCrypte.setFont(new Font("Arial", 25));

    this.pg = new ProgressBar();

    this.clavier = new Clavier(STYLESHEET_CASPIAN, null);

    this.leNiveau = new Text("Niveau : Facile");

    this.chrono = new Chronometre();

    this.panelCentral = new BorderPane();

    

    // Initialisation du bouton Jouer
    // this.bJouer = new Button("Jouer");
}

    /**
     * @return le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene() {
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
private Pane titre() {
    Label titre = new Label("Jeu du Pendu");
    titre.setFont(new Font("Arial", 36));
    titre.setTextFill(Color.DARKBLUE);
    titre.setAlignment(Pos.TOP_LEFT);
    titre.setPrefWidth(800);
    HBox ensembleBouton = new HBox();
    BorderPane banniere = new BorderPane();
    banniere.setPadding(new Insets(20));
    banniere.setStyle("-fx-background-color: lightgray;");

    // Initialisation du bouton Paramètres
    ImageView imgParam = new ImageView(new Image("file:/home/iut45/Etudiants/o22403771/Documents/2emeSemestres/IHM/Projet-IHM-Tp5-Pendu/pendu_pour_etu/img/parametres.png"));

   imgParam.setFitWidth(30);
   imgParam.setFitHeight(30);
    this.boutonParametres = new Button();
    this.boutonParametres.setGraphic(imgParam);

    // Initialisation du bouton Maison

    ImageView maisonImageView = new ImageView(new Image("file:pendu_pour_etu/img/home.png"));
    maisonImageView.setFitWidth(30);
    maisonImageView.setFitHeight(30);
    this.boutonMaison = new Button();
    this.boutonMaison.setGraphic(maisonImageView);

    ensembleBouton.getChildren().addAll(this.boutonParametres, this.boutonMaison);

    banniere.setLeft(titre);
    banniere.setRight(ensembleBouton);

    return banniere;
}


    // /**
    // * @return le panel du chronomètre
    // */
    private TitledPane leChrono() {
        // A implementer
        chrono = new Chronometre();
        TitledPane res = new TitledPane("Chronometre", chrono);
        return res;
    }

    // /**
    // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
    // * de progression et le clavier
    // */
    private Pane fenetreJeu() {
        // A implementer
        Pane res = new Pane();
        return res;
    }

    // /**
    // * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de
    // jeu
    // */
    private Pane fenetreAccueil() {
        VBox accueil = new VBox(20);
        accueil.setAlignment(Pos.CENTER);
        accueil.setPadding(new Insets(20));

        Label titre = new Label("Bienvenue dans le Pendu !");
        titre.setFont(new Font("Arial", 24));

        ComboBox<String> choixNiveau = new ComboBox<>();
        choixNiveau.getItems().addAll("Facile", "Moyen", "Difficile");
        choixNiveau.setValue("Facile");

        Button boutonJouer = new Button("Jouer");
        boutonJouer.setOnAction(e -> this.modeJeu());

        Button boutonRegles = new Button("Règles");
        boutonRegles.setOnAction(e -> this.popUpReglesDuJeu().showAndWait());

        accueil.getChildren().addAll(titre, choixNiveau, boutonJouer, boutonRegles);
        return accueil;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * 
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire) {
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil() {
        // A implementer
    }

    public void modeJeu() {
        // A implementer
        this.lancePartie();
        // this.panelCentral.se
    }

    public void modeParametres() {
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie() {
        // A implementer
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        // A implementer
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * 
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono() {
        // A implémenter
        return null; // A enlever
    }

    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    public Alert popUpReglesDuJeu() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessageGagne() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    public Alert popUpMessagePerdu() {
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * 
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * 
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }
}
