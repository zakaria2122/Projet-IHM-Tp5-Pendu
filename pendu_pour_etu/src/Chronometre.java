import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;

/**
 * Permet de gérer un Text associé à une Timeline pour afficher un temps écoulé
 */
public class Chronometre extends Text {
    /**
     * timeline qui va gérer le temps
     */
    private Timeline timeline;
    /**
     * la fenêtre de temps
     */
    private KeyFrame keyFrame;
    /**
     * le contrôleur associé au chronomètre
     */
    private ControleurChronometre actionTemps;
    /**
     * temps limite en millisecondes (0 = pas de limite)
     */
    private long tempsLimite;
    /**
     * mode décompte (true) ou compte normal (false)
     */
    private boolean modeDecompte;

    /**
     * Constructeur permettant de créer le chronomètre
     * avec un label initialisé à "0:00"
     */
    public Chronometre() {
        super("0:00");
        this.setFont(Font.font("Arial", 20));
        this.setTextAlignment(TextAlignment.CENTER);
        this.tempsLimite = 0;
        this.modeDecompte = false;

        this.actionTemps = new ControleurChronometre(this);
        this.keyFrame = new KeyFrame(Duration.millis(100), this.actionTemps);
        this.timeline = new Timeline(this.keyFrame);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Configure le chronomètre avec une limite de temps
     * 
     * @param tempsLimiteMinutes temps limite en minutes (0 = pas de limite)
     */
    public void setTempsLimite(int tempsLimiteMinutes) {
        if (tempsLimiteMinutes > 0) {
            // Conversion avec Duration
            this.tempsLimite = (long) Duration.minutes(tempsLimiteMinutes).toMillis();
            this.modeDecompte = true;

            this.setText(String.format("%d:%02d", tempsLimiteMinutes, 0));
            this.setFill(Color.BLACK);
        } else {
            this.tempsLimite = 0;
            this.modeDecompte = false;
            this.setText("0:00");
            this.setFill(Color.BLACK);
        }
    }

    /**
     * Permet au controleur de mettre à jour le text
     * 
     * @param tempsMillisec la durée à afficher (temps écoulé depuis le début)
     */
    public void setTime(long tempsMillisec) {
        if (this.modeDecompte) {
            // En mode décompte, on affiche le temps restant
            long tempsRestant = this.tempsLimite - tempsMillisec;

            if (tempsRestant <= 0) {
                this.setText("0:00");
                this.setFill(Color.RED);
                this.stop();
                // Notifier que le temps est écoulé
                this.actionTemps.tempsEcoule();
                return;
            }

            long secondesRestantes = tempsRestant / 1000;
            long minutesRestantes = secondesRestantes / 60;
            secondesRestantes = secondesRestantes % 60;

            this.setText(String.format("%d:%02d", minutesRestantes, secondesRestantes));

            // Changer la couleur selon le temps restant
            if (tempsRestant <= 30000) { // moins de 30 secondes
                this.setFill(Color.RED);
            } else if (tempsRestant <= 60000) { // moins de 1 minute
                this.setFill(Color.ORANGE);
            } else {
                this.setFill(Color.BLACK);
            }
        } else {
            // Mode normal : compte le temps écoulé
            long secondes = tempsMillisec / 1000;
            long minutes = secondes / 60;
            secondes = secondes % 60;
            this.setText(String.format("%d:%02d", minutes, secondes));
            this.setFill(Color.BLACK);
        }
    }

    /**
     * Vérifie si le temps est écoulé
     */
    public boolean isTempsEcoule() {
        return this.modeDecompte && this.actionTemps.getTempsEcoule() >= this.tempsLimite;
    }

    public void start() {
        this.actionTemps.reset();
        if (this.timeline.getStatus() != Animation.Status.RUNNING) {
            this.timeline.play();
        }
    }

    public void stop() {
        if (this.timeline.getStatus() == Animation.Status.RUNNING) {
            this.timeline.stop();
        }
    }

    public void resetTime() {
        this.stop();
        this.actionTemps.reset();

        if (this.modeDecompte && this.tempsLimite > 0) {
        
            long minutes = this.tempsLimite / (60 * 1000);
            this.setText(String.format("%d:%02d", minutes, 0));
        } else {
          
            
            this.setText("0:00");
        }
        this.setFill(Color.BLACK);
    }

    public long getTempsEcouleSecondes() {
        if (this.actionTemps != null) {
            return this.actionTemps.getTempsEcoule() / 1000;
        }
        return 0;
    }
}