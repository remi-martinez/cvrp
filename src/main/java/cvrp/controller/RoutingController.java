package cvrp.controller;

import cvrp.App;
import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RoutingController implements Initializable {

    private final static int POINT_RADIUS = 5;
    private final static int ARROW_HEAD_SIZE = 10;

    @FXML
    private Label fileLabel;

    @FXML
    private AnchorPane graphPane;

    @FXML
    private CheckBox arrowCheckbox;

    @FXML
    private Label graphZoneLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {


//        this.selectTourne.getItems().addAll(NomAlgoTourne.values());
    }

    @FXML
    public void filePickerClicked() {
        FileChooser fileChooser = new FileChooser();

        // Extensions
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        // Default directory
//        System.out.println(System.getProperty("java.class.path") + "../../../files");
//        String defaultDirectoryString = System.getProperty("java.class.path"); // + "../../../files";
//        File defaultDirectory = new File(defaultDirectoryString);
//        if(!defaultDirectory.canRead()) {
//            defaultDirectory = new File("c:/");
//        }
//        fileChooser.setInitialDirectory(defaultDirectory);

        // File chooser
        fileChooser.setTitle("Choisir un fichier de données");
        File file = fileChooser.showOpenDialog(App.getStage());

        if (file != null) {
            fileLabel.setText(file.getName());
            fileLabel.setTextFill(Color.BLACK);
        }
    }

    @FXML
    public void testClicked() {
        this.addLine(80,80,50,50, Color.ORANGERED);
        this.addPoint(50,50, Color.BLUEVIOLET);
        this.addPoint(80,80, Color.BLUEVIOLET);
    }

    @FXML
    public void showAboutWindow() {
        try {
            URL fxmlFile = new File("src/main/resources/cvrp/cvrp/about.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(fxmlFile);
            Stage stage = new Stage();
            stage.setTitle("A propos");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addPoint(int x, int y, Color color) {
        Circle circle = new Circle();
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setRadius(POINT_RADIUS);
        circle.setStroke(color);
        circle.setFill(color);


        Tooltip tooltip = new Tooltip();
        tooltip.setText("x : " + x + ", y : " + y);
        Tooltip.install(circle, tooltip);

        graphPane.getChildren().add(circle);
    }

    public void addLine(int x1, int y1, int x2, int y2, Color color) {
        Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        line.setStroke(color);

        StackPane arrow = new StackPane();
        String strColor = color.toString().substring(2,8);
        arrow.setStyle(String.format("-fx-background-color:#%s;-fx-border-width:1px;-fx-shape: \"M0,-4L4,0L0,4Z\";-fx-border-color:#%s", strColor, strColor));

        arrow.setLayoutX(x2 - ARROW_HEAD_SIZE);
        arrow.setLayoutY(y2 - ARROW_HEAD_SIZE/2.0);
        arrow.setPrefSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.setMaxSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.setMinSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.visibleProperty().bind(arrowCheckbox.selectedProperty());

        double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
        double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
        double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
        double dt = lineLength - (POINT_RADIUS / 2.0) - (ARROW_HEAD_SIZE / 2.0);

        double t = dt / lineLength;
        double tX =  ((1 - t) * line.getStartX()) + (t * line.getEndX());
        double tY = ((1 - t) * line.getStartY()) + (t * line.getEndY());

        arrow.setLayoutX(tX - ARROW_HEAD_SIZE / 2.0);
        arrow.setLayoutY(tY - ARROW_HEAD_SIZE / 2.0);

        double angle = Math.toDegrees(Math.atan2(line.getEndY() - line.getStartY(), line.getEndX() - line.getStartX()));
        if (angle < 0) {
            angle += 360;
        }
        arrow.setRotate(angle);

        graphPane.getChildren().add(arrow);
        graphPane.getChildren().add(line);
    }
}
