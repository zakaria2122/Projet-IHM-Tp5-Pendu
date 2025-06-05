import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurParametre implements EventHandler<ActionEvent> {
    
    private Pendu vuePendu;
    
    /**
     * Constructeur du contrôleur
     */
    public ControleurParametre(Pendu vuePendu) {
        this.vuePendu = vuePendu;
    }
    
    /**
     * Gère le clic sur le bouton paramètres/info
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        this.vuePendu.popUpReglesDuJeu().showAndWait();
    }
}