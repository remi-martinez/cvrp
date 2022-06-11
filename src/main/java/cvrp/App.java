package cvrp;

import cvrp.model.Algorithm;
import cvrp.model.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class App extends Application {

    private static Stage stage;

    // Mettre à false pour démarrer une interface incroyable pensée par nos ingénieurs les plus compétents et beau au passage.
    private static final boolean AUTOSTART_SIMULATIONS = false;

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlFile = new File("src/main/resources/cvrp/cvrp.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(fxmlFile);
        Scene scene = new Scene(root);
        stage.setTitle("Capacited Vehicle Routing Problem");
        stage.setScene(scene);
        App.stage = stage;
        stage.show();
    }

    /**
     * App.AUTOSTART_SIMULATIONS = Si ce paramètre est à False le programme va lancer la génération de l'algorithme choisi
     *                             avec ses paramètres puis exporter les résultats dans un fichier CSV.
     *                             ATTENTION pour éviter de remplacer un fichier déjà fait, une erreur sera déclenchée si le fichier
     *                             existe déjà avec ces paramètres.
     *                             Pour forcer la regéneration d'un fichier --> supprimer le fichier correspondant aux anciens résultats.
     *                             Les fichiers sont au format Algo_nbIterations_parametres.csv
     */
    public static void main(String[] args) {
        if (App.AUTOSTART_SIMULATIONS) {
            // Exemple: Tabu avec 10 000 itérations et une liste à 2
            Simulation.prepareSimu(Algorithm.TABU, 10000, 0, 0,2);

            // Exemple : Recuit simulé avec 10 000 itérations, une variation à 0.8 et une température à 250
            Simulation.prepareSimu(Algorithm.SIMULATED_ANNEALING, 10000, .8f, 250,0);
            return;
        }

        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}