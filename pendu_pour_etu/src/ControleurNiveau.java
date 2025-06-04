import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

/**
 * Controleur des radio boutons gérant le niveau
 */
public class ControleurNiveau implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;

    /**
     * @param modelePendu modèle du jeu
     */
    public ControleurNiveau(MotMystere modelePendu) {
        this.modelePendu = modelePendu;
    }

    /**
     * gère le changement de niveau
     * @param actionEvent l'événement généré par le clic sur un RadioButton
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        RadioButton radiobouton = (RadioButton) actionEvent.getSource();
        String nomDuRadiobouton = radiobouton.getText();
        
        // Déterminer le niveau selon le texte du RadioButton
        int nouveauNiveau;
        switch (nomDuRadiobouton.toLowerCase()) {
            case "facile":
                nouveauNiveau = MotMystere.FACILE;
                break;
            case "moyen":
                nouveauNiveau = MotMystere.MOYEN;
                break;
            case "difficile":
                nouveauNiveau = MotMystere.DIFFICILE;
                break;
            case "expert":
                nouveauNiveau = MotMystere.EXPERT;
                break;
            default:
                nouveauNiveau = MotMystere.FACILE; // Par défaut
                break;
        }
        
        // Mettre à jour le niveau dans le modèle
        this.modelePendu.setNiveau(nouveauNiveau);
        
        System.out.println("Niveau changé pour : " + nomDuRadiobouton + " (niveau " + nouveauNiveau + ")");
    }
}