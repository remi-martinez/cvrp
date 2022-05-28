package cvrp;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class App extends Application {

    private static Stage stage;

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
        launch();
    }

    public static void setupScene(Scene scene) {
        if(App.stage != null)
            App.stage.setScene(scene);
    }

    public static Stage getStage() {
        return stage;
    }
}