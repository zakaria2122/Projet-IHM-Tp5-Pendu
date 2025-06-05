import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Contrôleur du chronomètre
 */
public class ControleurChronometre implements EventHandler<ActionEvent> {
    private long tempsPrec;
    private long tempsEcoule;
    private Chronometre chrono;
    private boolean tempsEcouleNotifie = false;

    public ControleurChronometre (Chronometre chrono){
        this.tempsPrec = System.currentTimeMillis();
        this.tempsEcoule = 0;
        this.chrono = chrono;
        this.tempsEcouleNotifie = false;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        long instantT = System.currentTimeMillis();
        long diff = instantT - this.tempsPrec;
        this.tempsEcoule += diff;
        this.tempsPrec = instantT;
        this.chrono.setTime(this.tempsEcoule);
    }

    public void reset(){
        this.tempsEcoule = 0;
        this.tempsPrec = System.currentTimeMillis();
        this.tempsEcouleNotifie = false;
    }

    public long getTempsEcoule() {
        return this.tempsEcoule;
    }

    /**
     * Appelé quand le temps limite est atteint
     */
    public void tempsEcoule() {
        if (!this.tempsEcouleNotifie) {
            this.tempsEcouleNotifie = true;
            // Ici on pourrait notifier la vue principale que le temps est écoulé
            System.out.println("Temps écoulé !");
        }
    }
}