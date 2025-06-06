import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Contrôleur du chronomètre
 * Cette classe gère la logique de chronométrage et met à jour l'affichage du temps
 */
public class ControleurChronometre implements EventHandler<ActionEvent> {
    // Stocke le timestamp de la dernière mise à jour pour calculer les différences de temps
    private long tempsPrec;
    
    // Accumule le temps total écoulé depuis le début ou le dernier reset
    private long tempsEcoule;
    
    // Référence vers l'objet Chronometre pour mettre à jour l'affichage
    private Chronometre chrono;
    
    // Flag pour éviter les notifications multiples quand le temps limite est atteint
    private boolean tempsEcouleNotifie = false;

    /**
     * Constructeur du contrôleur
     * @param chrono L'objet Chronometre à contrôler
     */
    public ControleurChronometre (Chronometre chrono){
        // Initialise le timestamp de référence avec l'heure actuelle
        this.tempsPrec = System.currentTimeMillis();
        
        // Commence avec un temps écoulé de zéro
        this.tempsEcoule = 0;
        
        // Stocke la référence vers le chronomètre
        this.chrono = chrono;
        
        // Initialise le flag de notification
        this.tempsEcouleNotifie = false;
    }

    /**
     * Méthode appelée à chaque événement (généralement par un Timer)
     * Met à jour le temps écoulé et l'affichage du chronomètre
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // Récupère le timestamp actuel
        long instantT = System.currentTimeMillis();
        
        // Calcule le temps écoulé depuis la dernière mise à jour
        long diff = instantT - this.tempsPrec;
        
        // Ajoute cette différence au temps total écoulé
        this.tempsEcoule += diff;
        
        // Met à jour le timestamp de référence pour le prochain calcul
        this.tempsPrec = instantT;
        
        // Met à jour l'affichage du chronomètre avec le nouveau temps
        this.chrono.setTime(this.tempsEcoule);
    }

    /**
     * Remet le chronomètre à zéro
     * Réinitialise tous les compteurs et flags
     */
    public void reset(){
        // Remet le temps écoulé à zéro
        this.tempsEcoule = 0;
        
        // Réinitialise le timestamp de référence
        this.tempsPrec = System.currentTimeMillis();
        
        // Remet le flag de notification à false pour permettre une nouvelle notification
        this.tempsEcouleNotifie = false;
    }

    /**
     * Getter pour récupérer le temps écoulé
     * @return Le temps écoulé en millisecondes
     */
    public long getTempsEcoule() {
        return this.tempsEcoule;
    }

    /**
     * Appelé quand le temps limite est atteint
     * Utilise un système de flag pour éviter les notifications répétées
     */
    public void tempsEcoule() {
        // Vérifie si la notification n'a pas déjà été envoyée
        if (!this.tempsEcouleNotifie) {
            // Marque comme notifié pour éviter les répétitions
            this.tempsEcouleNotifie = true;
            
            // Ici on pourrait notifier la vue principale que le temps est écoulé
            // Pour l'instant, simple affichage console
            System.out.println("Temps écoulé !");
        }
    }
}