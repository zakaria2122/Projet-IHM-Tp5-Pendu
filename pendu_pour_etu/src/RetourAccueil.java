import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
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
        // Vérifier s'il y a une partie en cours
        // Une partie est en cours si le jeu n'est ni gagné ni perdu et qu'il y a eu des essais
        if (this.modelePendu.getNbEssais() > 0 && !this.modelePendu.gagne() && !this.modelePendu.perdu()) {
            // Utiliser le popup déjà existant dans la classe Pendu
            Alert confirmation = this.vuePendu.popUpPartieEnCours();
            
            // Afficher la boîte de dialogue et attendre la réponse
            Optional<ButtonType> resultat = confirmation.showAndWait();
            
            // Si l'utilisateur confirme (clique sur "YES")
            if (resultat.isPresent() && resultat.get() == ButtonType.YES) {
                // Retourner à l'accueil
                this.vuePendu.modeAccueil();
            }
            // Si l'utilisateur clique sur "NO", on ne fait rien (reste sur la partie)
        } else {
            // Aucune partie en cours, retourner directement à l'accueil
            this.vuePendu.modeAccueil();
        }
    }
}