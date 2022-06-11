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
    private static final boolean AUTOSTART_SIMULATIONS = true; // Mettre à false pour démarrer une interface tah les oufs

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
            Simulation.prepareSimu(Algorithm.TABU, 10000, 0, 0,20);
            Simulation.prepareSimu(Algorithm.TABU, 10000, 0, 0,30);
            return;
        }

        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}