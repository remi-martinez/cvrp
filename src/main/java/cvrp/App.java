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
import java.util.List;

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
            Simulation.prepareSimu(Algorithm.TABU, List.of(Algorithm.RANDOM, Algorithm.FILL_TRUCK),10000, 1);
            return;
        }

        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}