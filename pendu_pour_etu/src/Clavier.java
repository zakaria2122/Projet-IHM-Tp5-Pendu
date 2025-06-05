import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Clavier pour le jeu du pendu
 */
public class Clavier extends VBox {

    private List<Button> boutons;

    /**
     * Constructeur du clavier
     */
    public Clavier(String lettres, EventHandler<ActionEvent> controleur) {

        
        // J'initialise ma liste de boutons
        this.boutons = new ArrayList<>();
        
        // J'espaces les lignes de 10 pixels
        this.setSpacing(10);

        // Je crée 4 lignes de boutons
        HBox premiereRangee = new HBox();
        HBox deuxiemeRangee = new HBox();
        HBox troisiemeRangee = new HBox();
        HBox quatriemeRangee = new HBox();

        // Première rangée : lettres A à H (8 lettres)
        for (int i = 0; i < 8; i++) {
            char lettre = lettres.charAt(i);
            Button nouveauBouton = new Button();
            nouveauBouton.setText(String.valueOf(lettre));
            nouveauBouton.setPrefSize(50, 30);
            nouveauBouton.setOnAction(controleur);
            
            premiereRangee.getChildren().add(nouveauBouton);
            this.boutons.add(nouveauBouton);
        }
        premiereRangee.setSpacing(5);

        // Deuxième rangée : lettres I à P (8 lettres)
        for (int i = 8; i < 16; i++) {
            char lettre = lettres.charAt(i);
            Button nouveauBouton = new Button();
            nouveauBouton.setText(String.valueOf(lettre));
            nouveauBouton.setPrefSize(50, 30);
            nouveauBouton.setOnAction(controleur);
            
            deuxiemeRangee.getChildren().add(nouveauBouton);
            this.boutons.add(nouveauBouton);
        }
        deuxiemeRangee.setSpacing(5);

        // Troisième rangée : lettres Q à X (8 lettres)
        for (int i = 16; i < 24; i++) {
            char lettre = lettres.charAt(i);
            Button nouveauBouton = new Button();
            nouveauBouton.setText(String.valueOf(lettre));
            nouveauBouton.setPrefSize(50, 30);
            nouveauBouton.setOnAction(controleur);
            
            troisiemeRangee.getChildren().add(nouveauBouton);
            this.boutons.add(nouveauBouton);
        }
        troisiemeRangee.setSpacing(5);

        // Quatrième rangée : lettres Y et Z (2 lettres)
        for (int i = 24; i < lettres.length(); i++) {
            char lettre = lettres.charAt(i);
            Button nouveauBouton = new Button();
            nouveauBouton.setText(String.valueOf(lettre));
            nouveauBouton.setPrefSize(50, 30);
            nouveauBouton.setOnAction(controleur);
            
            quatriemeRangee.getChildren().add(nouveauBouton);
            this.boutons.add(nouveauBouton);
        }
        quatriemeRangee.setSpacing(5);

        // J'ajoute toutes mes rangées au clavier
        this.getChildren().add(premiereRangee);
        this.getChildren().add(deuxiemeRangee);
        this.getChildren().add(troisiemeRangee);
        this.getChildren().add(quatriemeRangee);
    }

    /**
     * Désactive les touches utilisées
     */
    public void desactiveTouches(Set<String> lettresUtilisees) {
        // Je parcours tous mes boutons
        for (int i = 0; i < this.boutons.size(); i++) {
            Button monBouton = this.boutons.get(i);
            String laLettre = monBouton.getText();
            
            // Si la lettre a été utilisée, je désactive le bouton
            if (lettresUtilisees.contains(laLettre)) {
                monBouton.setDisable(true);
            } else {
                monBouton.setDisable(false);
            }
        }
    }
}