import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton Accueil
 */
public class RetourAccueil implements EventHandler<ActionEvent> {
    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     **/
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    public RetourAccueil(MotMystere modelePendu, Pendu vuePendu) {
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * L'action consiste à retourner sur la page d'accueil. Il faut vérifier qu'il n'y avait pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        boolean partieEnCours = false;
        
        // Vérifier s'il y a une partie en cours
        // Une partie est en cours si le jeu n'est ni gagné ni perdu et qu'il y a eu des essais
        if (this.modelePendu.getNbEssais() > 0 && !this.modelePendu.gagne() && !this.modelePendu.perdu()) {
            partieEnCours = true;
        }
        
        boolean retournerAccueil = true;
        
        if (partieEnCours) {
            Optional<ButtonType> reponse = this.vuePendu.popUpPartieEnCours().showAndWait();
            
            // Si la réponse est "Non", on ne retourne pas à l'accueil
            if (reponse.isPresent() && reponse.get().equals(ButtonType.NO)) {
                retournerAccueil = false;
            }
        }
        
        if (retournerAccueil) {
            // Arrêter le chronomètre s'il est en cours
            if (this.vuePendu.getChrono() != null) {
                this.vuePendu.getChrono().stop();
            }
            
            // Retourner au mode accueil
            this.vuePendu.modeAccueil();
        }
    }
}