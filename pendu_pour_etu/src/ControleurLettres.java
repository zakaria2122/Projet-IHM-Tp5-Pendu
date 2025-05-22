import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Controleur du clavier
 */
public class ControleurLettres implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     */
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    ControleurLettres(MotMystere modelePendu, Pendu vuePendu){
        // A implémenter
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * Actions à effectuer lors du clic sur une touche du clavier
     * Il faut donc: Essayer la lettre, mettre à jour l'affichage et vérifier si la partie est finie
     * @param actionEvent l'événement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // A implémenter

        Button boutonCliquer = (Button) actionEvent.getSource();
        String lettreChoix = boutonCliquer.getText();
        char lettre = lettreChoix.charAt(0);

        this.modelePendu.essaiLettre(lettre);
        
        this.vuePendu.majAffichage();

        if (this.modelePendu.gagne()) {
            this.vuePendu.getChrono().stop();
            this.vuePendu.popUpMessageGagne();
        } else if (this.modelePendu.perdu()) {
            this.vuePendu.getChrono().stop();
            this.vuePendu.popUpMessagePerdu();
        }
    }
}
