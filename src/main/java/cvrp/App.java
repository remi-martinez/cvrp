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
    private static final boolean AUTOSTART_SIMULATIONS = true; // Mettre à false pour démarrer normalement le programme

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

    public static void main(String[] args) {
        if (App.AUTOSTART_SIMULATIONS) {
            Simulation.prepareSimu(Algorithm.SIMULATED_ANNEALING, 10000,.9f , 0);
            Simulation.prepareSimu(Algorithm.SIMULATED_ANNEALING, 10000,.8f , 0);
            Simulation.prepareSimu(Algorithm.SIMULATED_ANNEALING, 10000,.7f , 0);
            Simulation.prepareSimu(Algorithm.SIMULATED_ANNEALING, 10000,.6f , 0);
            Simulation.prepareSimu(Algorithm.SIMULATED_ANNEALING, 10000,.5f , 0);
            return;
        }

        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}