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
    
    // ==================== ATTRIBUTS DU MODÈLE ====================
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

    // ==================== COMPOSANTS D'AFFICHAGE ====================
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

    // ==================== BOUTONS DE CONTRÔLE ====================
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Info / Point d'interrogation
     */
    private Button boutonInfo;
    /**
     * le bouton Accueil / Maison
     */
    private Button boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */
    private Button bJouer;

    // ==================== COMPOSANTS D'ACCUEIL ====================
    /**
     * le choix de niveau dans la fenêtre d'accueil
     */
    private ComboBox<String> choixNiveau;

    // ==================== INITIALISATION ====================
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

    // ==================== CONSTRUCTION DE LA SCÈNE ====================
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
        titre.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 50));
        titre.setTextFill(Color.DARKBLUE);
        titre.setAlignment(Pos.TOP_LEFT);
        titre.setPadding(new Insets(10));
        
        HBox ensembleBouton = new HBox();
        BorderPane banniere = new BorderPane();
        banniere.setPadding(new Insets(20));
        banniere.setStyle("-fx-background-color: lightgray;");

        // Initialisation du bouton Info
        ImageView boutonInfo = new ImageView(new Image("file:pendu_pour_etu/img/info.png"));
        boutonInfo.setFitWidth(50);
        boutonInfo.setFitHeight(50);
        this.boutonInfo = new Button();
        this.boutonInfo.setGraphic(boutonInfo);
        this.boutonInfo.setOnAction(e -> this.popUpReglesDuJeu().showAndWait());

        // Initialisation du bouton Paramètres
        ImageView imgParam = new ImageView(new Image("file:pendu_pour_etu/img/parametres.png"));
        imgParam.setFitWidth(50);
        imgParam.setFitHeight(50);
        this.boutonParametres = new Button();
        this.boutonParametres.setGraphic(imgParam);
        this.boutonParametres.setOnAction(e -> this.modeParametres());

        // Initialisation du bouton Maison
        ImageView maisonImageView = new ImageView(new Image("file:pendu_pour_etu/img/home.png"));
        maisonImageView.setFitWidth(50);
        maisonImageView.setFitHeight(50);
        this.boutonMaison = new Button();
        this.boutonMaison.setGraphic(maisonImageView);
        this.boutonMaison.setOnAction(new RetourAccueil(this.modelePendu, this));

        ensembleBouton.getChildren().addAll(this.boutonInfo, this.boutonParametres, this.boutonMaison);

        banniere.setLeft(titre);
        banniere.setRight(ensembleBouton);

        return banniere;
    }

    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono() {
        TitledPane panelChrono = new TitledPane();
        panelChrono.setText("Temps écoulé");
        panelChrono.setContent(this.chrono);
        panelChrono.setCollapsible(false);
        return panelChrono;
    }

    // ==================== FENÊTRES PRINCIPALES ====================
    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     *         de progression et le clavier
     */
    private Pane fenetreJeu() {
        BorderPane jeu = new BorderPane();
        jeu.setPadding(new Insets(20));

        // Panel du haut avec le mot crypté
        HBox haut = new HBox();
        haut.setAlignment(Pos.TOP_LEFT);
        haut.getChildren().add(this.motCrypte);
        haut.setPadding(new Insets(10, 0, 10, 50));

        // Panel central principal
        HBox imgGauche = new HBox(30);
        imgGauche.setAlignment(Pos.TOP_LEFT);

        //Panel de droite pour les infos
        VBox droite = new VBox(15);
        droite.setAlignment(Pos.TOP_LEFT);
        droite.setPadding(new Insets(20));

        // ========== PARTIE GAUCHE : IMAGE + BARRE ==========
        VBox imageEtBarre = new VBox(15);
        imageEtBarre.setAlignment(Pos.CENTER);
        imageEtBarre.setPrefWidth(400); // CORRECTION: 400 au lieu de 30 !

        // Image du pendu dans un cadre (comme sur le screenshot)
        imageEtBarre.getChildren().add(this.dessin);

        // AMÉLIORATION: Configuration de la barre de progression
        this.pg.setPrefWidth(300);  // Largeur fixe pour la barre
        this.pg.setPrefHeight(20);  // Hauteur pour qu'elle soit visible
        this.pg.setStyle("-fx-accent: blue;"); // Couleur bleue comme sur le screenshot

        // Barre de progression sous l'image
        imageEtBarre.getChildren().add(this.pg);

        // ========== PARTIE DROITE : INFOS ==========
        VBox infosJeu = new VBox(15);
        infosJeu.setAlignment(Pos.TOP_LEFT);

        // Niveau
        infosJeu.getChildren().add(this.leNiveau);

        // Chronomètre
        infosJeu.getChildren().add(this.leChrono());

        // Bouton nouvelle partie
        this.bJouer = new Button("Nouvelle Partie");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
        infosJeu.getChildren().add(this.bJouer);

        imgGauche.getChildren().addAll(imageEtBarre);
        droite.getChildren().addAll(infosJeu);

        // Panel du bas avec le clavier
        VBox bas = new VBox(10);
        bas.setAlignment(Pos.CENTER); // AMÉLIORATION: Centré pour le clavier
        bas.setPadding(new Insets(20, 0, 0, 0)); // AMÉLIORATION: Padding simplifié
        bas.getChildren().add(this.clavier);

        jeu.setTop(haut);
        jeu.setCenter(imgGauche);
        jeu.setRight(droite);
        jeu.setBottom(bas);

        return jeu;
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de
     *         jeu
     */
    private Pane fenetreAccueil() {
        VBox accueil = new VBox(20);
        accueil.setAlignment(Pos.TOP_LEFT);
        accueil.setPadding(new Insets(20));
    
        // Titre principal
        Label titre = new Label("Pendu Zakaria");
        titre.setFont(new Font("Arial", 26));
    
        // Création du TitledPane pour les paramètres
        TitledPane parametreJeu = new TitledPane();
        parametreJeu.setText("Niveau de Difficulté");
        parametreJeu.setCollapsible(true);
        parametreJeu.setExpanded(true);
    
        // Conteneur pour les RadioButtons
        VBox parametreContenu = new VBox(10);
        parametreContenu.setPadding(new Insets(15));
    
        // Création du ToggleGroup (pour qu'un seul soit sélectionné)
        ToggleGroup groupeNiveau = new ToggleGroup();
    
        // Création du contrôleur de niveau
        ControleurNiveau controleurNiveau = new ControleurNiveau(this.modelePendu);
    
        // Création des RadioButtons
        RadioButton radioFacile = new RadioButton("Facile");
        radioFacile.setToggleGroup(groupeNiveau);
        radioFacile.setSelected(true); // Sélectionné par défaut
        radioFacile.setOnAction(controleurNiveau);
    
        RadioButton radioMoyen = new RadioButton("Moyen");
        radioMoyen.setToggleGroup(groupeNiveau);
        radioMoyen.setOnAction(controleurNiveau);
    
        RadioButton radioDifficile = new RadioButton("Difficile");
        radioDifficile.setToggleGroup(groupeNiveau);
        radioDifficile.setOnAction(controleurNiveau);
    
        // Ajout d'info-bulles (recommandation WIMP)
        radioFacile.setTooltip(new Tooltip("Mots simples"));
        radioMoyen.setTooltip(new Tooltip("Mots moyens"));
        radioDifficile.setTooltip(new Tooltip("Mots difficiles"));
    
        // Ajout des RadioButtons au conteneur
        parametreContenu.getChildren().addAll(radioFacile, radioMoyen, radioDifficile);
    
        // Définir le contenu du TitledPane
        parametreJeu.setContent(parametreContenu);
    
        // Boutons
        Button boutonJouer = new Button("Lancer une Partie");
        boutonJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));
    
        boutonJouer.setTooltip(new Tooltip("Commencer le jeu"));
    
        // Conteneur pour les boutons
        HBox boutons = new HBox(15);
        boutons.setAlignment(Pos.CENTER);
        boutons.getChildren().addAll(boutonJouer);
    
        // Assemblage final
        accueil.getChildren().addAll(titre, parametreJeu, boutons);
    
        return accueil;
    }

    // ==================== GESTION DES IMAGES ====================
    /**
     * charge les images à afficher en fonction des erreurs
     * 
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire) {
        repertoire = "/home/Zakaria/Documents/Ihm/Projet-IHM-Tp5-Pendu/pendu_pour_etu/img";
        for (int i = 0; i < this.modelePendu.getNbErreursMax() + 1; i++) {
            File file = new File(repertoire + "/pendu" + i + ".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    // ==================== MODES DE JEU ====================
    public void modeAccueil() {
        this.panelCentral.setCenter(this.fenetreAccueil());
        this.chrono.stop(); // Arrêter le chronomètre si une partie était en cours
        this.chrono.resetTime(); // Réinitialiser le chronomètre
        this.motCrypte.setText(""); // Réinitialiser le mot crypté
        this.dessin.setImage(this.lesImages.get(0)); // Réinitialiser l'image du pendu
    }

    public void modeJeu(){
        this.panelCentral.setCenter(this.fenetreJeu());

        // Activer le bouton Home et griser le bouton Paramètres en mode jeu
        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);

        // Démarrer le chronomètre
        this.chrono.start();
    }

    public void modeParametres(){
        // À implémenter selon les besoins
        // Pour l'instant, on reste en mode accueil
        this.modeAccueil();
    }

    // ==================== LOGIQUE DE JEU ====================
    /** lance une partie */
    public void lancePartie() {
        this.modelePendu.setMotATrouver();

        // Configuration du temps limite selon le niveau
        int niveau = this.modelePendu.getNiveau();
        switch (niveau) {
            case MotMystere.FACILE:
                this.chrono.setTempsLimite(7); // 7 minutes
                break;
            case MotMystere.MOYEN:
                this.chrono.setTempsLimite(4); // 4 minutes
                break;
            case MotMystere.DIFFICILE:
                this.chrono.setTempsLimite(2); // 2 minutes
                break;
        }

        this.chrono.start();
        this.majAffichage();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage() {
        // Mise à jour du mot crypté
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        // Vérifier si le temps est écoulé
        if (this.chrono.isTempsEcoule()) {
            this.chrono.stop();
            this.popUpMessagePerdu().showAndWait();
            return;
        }

        // Mise à jour de l'image du pendu
        int nbErreurs = this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants();
        if (nbErreurs < this.lesImages.size()) {
            this.dessin.setImage(this.lesImages.get(nbErreurs));
        }

        // Mise à jour de la barre de progression
        double progression = (double) this.modelePendu.getNbErreursRestants() / this.modelePendu.getNbErreursMax();
        this.pg.setProgress(progression);

        // Mise à jour du niveau affiché
        String[] niveauxTexte = { "Facile", "Moyen", "Difficile", "Expert" };
        this.leNiveau.setText("Niveau : " + niveauxTexte[this.modelePendu.getNiveau()]);

        // Désactiver les lettres déjà essayées
        this.clavier.desactiveTouches(this.modelePendu.getLettresEssayees());
    }

    // ==================== ACCESSEURS ====================
    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * 
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono() {
        return this.chrono;
    }

    // ==================== POPUPS ====================
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
                "- Facile : première et dernière lettres révélées\n" +
                "- Moyen : première lettre révélée\n" +
                "- Difficile : aucune lettre révélée");
        return alert;
    }

    public Alert popUpMessageGagne() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Félicitations !");
        alert.setHeaderText("Vous avez gagné !");
        alert.setContentText("Bravo ! Vous avez trouvé le mot : " + this.modelePendu.getMotATrouve() +
                "\nTemps : " + this.chrono.getText());
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

    // ==================== POINT D'ENTRÉE ====================
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