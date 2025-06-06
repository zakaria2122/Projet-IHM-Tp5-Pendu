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

    // Modèle contenant la logique du jeu
    private MotMystere modelePendu;
    // Liste des images du pendu
    private ArrayList<Image> lesImages;
    // Liste des niveaux disponibles
    public List<String> niveaux;

    // Éléments graphiques principaux
    private ImageView dessin; // Image du pendu
    private Text motCrypte; // Mot avec lettres cachées
    private ProgressBar pg; // Barre de progression
    private Clavier clavier; // Clavier virtuel
    private Text leNiveau; // Affichage du niveau
    private Chronometre chrono; // Chronomètre
    private BorderPane panelCentral; // Panel qui change selon le mode

    // Boutons de navigation
    private Button boutonParametres;
    private Button boutonInfo;
    private Button boutonMaison;
    private Button bJouer;

    // Sélecteur de niveau
    private ComboBox<String> choixNiveau;

    @Override
    public void init() {
        // Initialise le modèle du jeu
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        
        // Charge les images
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");

        // Définit les niveaux
        this.niveaux = Arrays.asList("Facile", "Moyen", "Difficile");

        // Configure l'image du pendu
        this.dessin = new ImageView();
        this.dessin.setImage(this.lesImages.get(0));
        this.dessin.setFitWidth(400);
        this.dessin.setFitHeight(500);
        this.dessin.setPreserveRatio(true);

        // Configure le texte du mot
        this.motCrypte = new Text();
        this.motCrypte.setFont(new Font("Arial", 50));

        // Configure la barre de progression
        this.pg = new ProgressBar();
        this.pg.setPrefWidth(300);

        // Initialise le clavier
        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ", new ControleurLettres(this.modelePendu, this));

        // Configure l'affichage du niveau           
        this.leNiveau = new Text("Niveau : Facile"); 
        this.leNiveau.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 35));

        // Initialise le chronomètre
        this.chrono = new Chronometre();

        // Initialise le panel central
        this.panelCentral = new BorderPane();

        // Configure la liste déroulante des niveaux
        this.choixNiveau = new ComboBox<>();
        this.choixNiveau.getItems().addAll("Facile", "Moyen", "Difficile");
        this.choixNiveau.setValue("Facile");   
    }

    // Crée la scène principale
    private Scene laScene() {
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre()); // Titre en haut
        fenetre.setCenter(this.panelCentral); // Contenu central
        return new Scene(fenetre, 800, 900);
    }

    // Crée la barre de titre avec boutons
    private Pane titre() {
        // Titre du jeu
        Label titre = new Label("Jeu du Pendu");
        titre.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 35));
        titre.setTextFill(Color.DARKBLUE);
        titre.setAlignment(Pos.TOP_LEFT);
        titre.setPadding(new Insets(5));

        // Container pour les boutons
        HBox ensembleBouton = new HBox();
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(10));
        banniere.setStyle("-fx-background-color: lightgray;");
        banniere.setPrefHeight(60);

        // Bouton info
        ImageView boutonInfo = new ImageView(new Image("file:pendu_pour_etu/img/info.png"));
        boutonInfo.setFitWidth(40);
        boutonInfo.setFitHeight(40);
        this.boutonInfo = new Button();
        this.boutonInfo.setGraphic(boutonInfo);
        this.boutonInfo.setOnAction(new ControleurInfos(this));

        // Bouton paramètres
        ImageView imgParam = new ImageView(new Image("file:pendu_pour_etu/img/parametres.png"));
        imgParam.setFitWidth(40);
        imgParam.setFitHeight(40);
        this.boutonParametres = new Button();
        this.boutonParametres.setGraphic(imgParam);
        this.boutonParametres.setOnAction(new ControleurParametre(this));

        // Bouton accueil
        ImageView maisonImageView = new ImageView(new Image("file:pendu_pour_etu/img/home.png"));
        maisonImageView.setFitWidth(40);
        maisonImageView.setFitHeight(40);
        this.boutonMaison = new Button();
        this.boutonMaison.setGraphic(maisonImageView);
        this.boutonMaison.setOnAction(new RetourAccueil(this.modelePendu, this));

        // Ajoute tous les boutons
        ensembleBouton.getChildren().addAll(this.boutonMaison, this.boutonParametres, this.boutonInfo);
        ensembleBouton.setSpacing(5);

        // Place titre à gauche, boutons à droite
        banniere.setLeft(titre);
        banniere.setRight(ensembleBouton);

        return banniere;
    }

    // Crée le panel du chronomètre
    private TitledPane leChrono() {
        TitledPane panelChrono = new TitledPane();
        panelChrono.setText("Temps restant");
        panelChrono.setContent(this.chrono);
        panelChrono.setCollapsible(false);
        return panelChrono;
    }

    // Crée l'interface de jeu
    private Pane fenetreJeu() {
        BorderPane jeu = new BorderPane();
        jeu.setPadding(new Insets(10));

        // Zone du haut : mot à deviner
        HBox haut = new HBox();
        haut.setAlignment(Pos.TOP_LEFT);         
        haut.getChildren().add(this.motCrypte);   
        haut.setPadding(new Insets(10, 0, 10, 50));

        // Zone gauche : image + barre de progression
        VBox centreGauche = new VBox(10);   
        centreGauche.setAlignment(Pos.CENTER);
        centreGauche.setPrefWidth(400);       
        centreGauche.getChildren().add(this.dessin);

        // Configuration de la barre de progression
        this.pg.setPrefWidth(300); 
        this.pg.setPrefHeight(25); 
        this.pg.setProgress(0.8);  
        centreGauche.getChildren().add(this.pg);

        // Zone droite : infos + boutons
        VBox droite = new VBox(30);       
        droite.setPadding(new Insets(20));

        // Augmente la taille de la zone droite                       
        leNiveau.setTabSize(400);
        leNiveau.setTabSize(500);

        leChrono().setPrefHeight(200);

 

        // Ajoute les éléments dans la zone droite
        droite.getChildren().add(this.leNiveau);  
        droite.getChildren().add(this.leChrono()); 

        // Bouton nouvelle partie
        this.bJouer = new Button("Nouvelle Partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        droite.getChildren().add(this.bJouer);

        // Centre de l'écran
        HBox centre = new HBox(30);
        centre.setAlignment(Pos.TOP_LEFT);
        centre.getChildren().addAll(centreGauche);

        // Zone du bas : clavier
        VBox basClavier = new VBox(5);
        basClavier.setAlignment(Pos.CENTER);
        basClavier.setPadding(new Insets(10, 0, 10, 0));

        this.clavier.setSpacing(10);
        this.clavier.setPadding(new Insets(10));
        basClavier.getChildren().add(this.clavier);

        // Assemble toutes les zones
        jeu.setTop(haut);
        jeu.setCenter(centre);
        jeu.setBottom(basClavier);
        jeu.setRight(droite);
        return jeu;
    }

    // Crée l'interface d'accueil
    private Pane fenetreAccueil() {
        VBox accueil = new VBox(20);
        accueil.setAlignment(Pos.TOP_LEFT);
        accueil.setPadding(new Insets(20));

        // Panel de choix du niveau
        TitledPane parametreJeu = new TitledPane();
        parametreJeu.setText("Niveau de Difficulté");
        parametreJeu.setCollapsible(true);
        parametreJeu.setExpanded(true);

        VBox parametreContenu = new VBox(10);
        parametreContenu.setPadding(new Insets(15));

        // Groupe de boutons radio pour les niveaux
        ToggleGroup groupeNiveau = new ToggleGroup();
        ControleurNiveau controleurNiveau = new ControleurNiveau(this.modelePendu);

        // Bouton Facile
        RadioButton radioFacile = new RadioButton("Facile");
        radioFacile.setToggleGroup(groupeNiveau);
        radioFacile.setSelected(true);
        radioFacile.setOnAction(controleurNiveau);

        // Bouton Moyen
        RadioButton radioMoyen = new RadioButton("Moyen");
        radioMoyen.setToggleGroup(groupeNiveau);
        radioMoyen.setOnAction(controleurNiveau);

        // Bouton Difficile
        RadioButton radioDifficile = new RadioButton("Difficile");
        radioDifficile.setToggleGroup(groupeNiveau);
        radioDifficile.setOnAction(controleurNiveau);

        // Ajoute les boutons radio
        parametreContenu.getChildren().addAll(radioFacile, radioMoyen, radioDifficile);
        parametreJeu.setContent(parametreContenu);

        // Bouton pour lancer le jeu
        Button boutonJouer = new Button("Lancer une Partie");
        boutonJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        boutonJouer.setTooltip(new Tooltip("Commencer le jeu"));

        HBox boutons = new HBox(15);
        boutons.setAlignment(Pos.TOP_LEFT);
        boutons.getChildren().addAll(boutonJouer);

        // Assemble l'accueil
        accueil.getChildren().addAll(boutons, parametreJeu);

        return accueil;
    }

    // Charge les images du pendu
    private void chargerImages(String repertoire) {
        repertoire = "pendu_pour_etu/img";
        // Charge une image pour chaque étape du pendu
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    // Passe en mode accueil
    public void modeAccueil() {
        this.panelCentral.setCenter(this.fenetreAccueil());
        this.chrono.stop(); // Arrête le chrono
        this.chrono.resetTime(); // Remet à zéro
        this.motCrypte.setText(""); // Efface le mot
        this.dessin.setImage(this.lesImages.get(0)); // Image initiale
    }

    // Passe en mode jeu
    public void modeJeu() {
        this.panelCentral.setCenter(this.fenetreJeu());

        // Gère l'état des boutons
        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);

        this.chrono.start(); // Lance le chrono
    }

    // Passe en mode paramètres
    public void modeParametres() {
        this.modeAccueil();
    }

    // Lance une nouvelle partie
    public void lancePartie() {
        this.modelePendu.setMotATrouver(); // Choisit un nouveau mot

        // Configure le temps selon le niveau
        int niveau = this.modelePendu.getNiveau();
        switch (niveau) {
            case MotMystere.FACILE:
                this.chrono.setTempsLimite(4); // 4 minutes
                break;
            case MotMystere.MOYEN:
                this.chrono.setTempsLimite(3); // 3 minutes
                break;
            case MotMystere.DIFFICILE:
                this.chrono.setTempsLimite(2); // 2 minutes
                break;
        }

        this.chrono.start(); // Lance le chrono
        this.majAffichage(); // Met à jour l'affichage
    }

    // Met à jour l'affichage du jeu
    public void majAffichage() {
        // Affiche le mot avec lettres trouvées/cachées
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        
        // Vérifie si le temps est écoulé
        if (this.chrono.isTempsEcoule()) {
            this.chrono.stop();
            this.popUpMessagePerdu().showAndWait();
            return;
        }

        // Met à jour l'image selon les erreurs
        int nbErreurs = this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants();
        if (nbErreurs < this.lesImages.size()) {
            this.dessin.setImage(this.lesImages.get(nbErreurs));
        }

        // Met à jour la barre de progression
        double progression = (double) this.modelePendu.getNbErreursRestants() / this.modelePendu.getNbErreursMax();
        this.pg.setProgress(progression);

        // Met à jour l'affichage du niveau
        String[] niveauxTexte = { "Facile", "Moyen", "Difficile", "Expert" };
        this.leNiveau.setText("Niveau : " + niveauxTexte[this.modelePendu.getNiveau()]);

        // Désactive les lettres déjà utilisées
        this.clavier.desactiveTouches(this.modelePendu.getLettresEssayees());
    }

    // Getter pour le chronomètre
    public Chronometre getChrono() {
        return this.chrono;
    }

    // Popup de confirmation d'interruption
    public Alert popUpPartieEnCours() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }

    // Popup des règles du jeu
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

    // Popup de victoire
    public Alert popUpMessageGagne() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Félicitations !");
        alert.setHeaderText("Vous avez gagné !");
        alert.setContentText("Bravo ! Vous avez trouvé le mot : " + this.modelePendu.getMotATrouve() +
                "\nTemps : " + this.chrono.getTempsEcouleSecondes() + "secondes.");
        return alert;
    }

    // Popup de défaite
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

    // Lance l'application
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil(); // Commence en mode accueil
        stage.show();
    }

    // Point d'entrée du programme
    public static void main(String[] args) {
        launch(args);
    }
}