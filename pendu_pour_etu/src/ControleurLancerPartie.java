import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton rejouer ou Lancer une partie
 */
public class ControleurLancerPartie implements EventHandler<ActionEvent> {
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
     * @param p vue du jeu
     */
    public ControleurLancerPartie(MotMystere modelePendu, Pendu vuePendu) {
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        boolean partieEnCours = false;
        
        // Vérifier s'il y a une partie en cours
        // Une partie est en cours si le jeu n'est ni gagné ni perdu et qu'il y a eu des essais
        if (this.modelePendu.getNbEssais() > 0 && 
            !this.modelePendu.gagne() && 
            !this.modelePendu.perdu()) {
            partieEnCours = true;
        }
        
        if (partieEnCours) {
            // Il y a une partie en cours, demander confirmation
            Optional<ButtonType> reponse = this.vuePendu.popUpPartieEnCours().showAndWait();
            
            // Si la réponse est oui, lancer une nouvelle partie
            if (reponse.isPresent() && reponse.get().equals(ButtonType.YES)) {
                this.lancerNouvellePartie();
            }
            // Si la réponse est non, on ne fait rien (la partie continue)
        } else {
            // Aucune partie en cours, lancer directement une nouvelle partie
            this.lancerNouvellePartie();
        }
    }
    
    /**
     * Lance effectivement une nouvelle partie
     */
    private void lancerNouvellePartie() {
        // Passer en mode jeu
        this.vuePendu.modeJeu();
        
        // Lancer la partie
        this.vuePendu.lancePartie();
        
        System.out.println("Nouvelle partie lancée !");
    }
}