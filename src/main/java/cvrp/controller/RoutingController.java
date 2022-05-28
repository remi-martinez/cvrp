package cvrp.controller;

import cvrp.App;
import cvrp.model.Client;
import cvrp.model.Graph;
import cvrp.model.Vehicle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RoutingController implements Initializable {

    private final static int POINT_RADIUS = 5;
    private final static int GRAPH_GROWTH = 5;
    private final static int ARROW_HEAD_SIZE = 10;
    private final static List<Color> AVAILABLE_COLORS = Arrays.asList(Color.RED, Color.CRIMSON, Color.ORANGE,
            Color.GREEN, Color.DARKGREEN, Color.BLUE, Color.CORNFLOWERBLUE, Color.VIOLET);

    @FXML private AnchorPane graphPane;
    @FXML private Label statNbClients;
    @FXML private Label statNbVehicles;
    @FXML private Label graphZoneLabel;
    @FXML private Label fileLabel;

    @FXML private AnchorPane loadingPane;
    @FXML private Label loadingPercentage;
    @FXML private ProgressBar loadingProgressBar;

    @FXML private Slider zoomSlider;
    @FXML private Label zoomPercentage;

    // For AnchorPane dragging
    private double dragXOffset = 0;
    private double dragYOffset = 0;

    @FXML private CheckBox arrowCheckbox;
    @FXML private CheckBox colorCheckbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoomPercentage.textProperty().bind(Bindings.createStringBinding(() -> Math.round(zoomSlider.getValue()) + "%", zoomSlider.valueProperty()));
        zoomSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            setZoomLevel((double) newValue / 100d);
        });

//        this.selectTourne.getItems().addAll(NomAlgoTourne.values());
    }

    @FXML
    public void filePickerClicked() {
        FileChooser fileChooser = new FileChooser();

        // Extensions
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        // Default directory
        String defaultDirectoryString = new File("").getAbsolutePath() + "\\files";
        File defaultDirectory = new File(defaultDirectoryString);
        if (!defaultDirectory.canRead()) {
            defaultDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(defaultDirectory);

        // File chooser
        try {
            fileChooser.setTitle("Choisir un fichier de données");
            File file = fileChooser.showOpenDialog(App.getStage());
            if (file != null) {
                fileLabel.setText(file.getName());
                fileLabel.setTextFill(Color.BLACK);
                this.loadGraph(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGraph(File file) throws IOException {
        Graph graph = new Graph(file);
        this.graphZoneLabel.setVisible(false);
        this.statNbClients.setText(graph.getClientList().size() + "");
        this.statNbVehicles.setText(graph.getVehicles().size() + "");
        this.graphPane.getChildren().clear();
        this.drawGraph(graph);

    }

    @FXML
    public void startSimulation() {
        loadingPane.setVisible(true);
        graphPane.getChildren().remove(graphPane.lookup("Line"));
    }

    public void drawGraph(Graph graph) {
        Random rand = new Random();

        for (Vehicle v : graph.getVehicles()) {
            Iterator<Client> it = v.getVisit().iterator();
            Color visitColor = AVAILABLE_COLORS.get(rand.nextInt(AVAILABLE_COLORS.size()));
            Client previous = null;
            if (it.hasNext()) {
                previous = it.next();
            }
            while (it.hasNext()) {
                Client current = it.next();
                this.addPoint(previous.getPosX() * GRAPH_GROWTH, previous.getPosY() * GRAPH_GROWTH, visitColor);
                this.addLine(previous.getPosX() * GRAPH_GROWTH,
                        previous.getPosY() * GRAPH_GROWTH,
                        current.getPosX() * GRAPH_GROWTH,
                        current.getPosY() * GRAPH_GROWTH,
                        visitColor);
                previous = current;
            }

        }

    }

    @FXML
    public void testClicked() {
        this.addLine(80, 80, 50, 50, Color.ORANGERED);
        this.addPoint(50, 50, Color.BLUEVIOLET);
        this.addPoint(80, 80, Color.BLUEVIOLET);

        // Test other stuff here...
    }

    @FXML
    public void handleGraphPaneScroll(ScrollEvent scrollEvent) {
        double zoomRatio = scrollEvent.getDeltaY() > 0 ? 1.10 : 0.90;
        setZoomLevel(graphPane.getScaleX() * zoomRatio);
    }

    @FXML
    public void handleGraphPanePressed(MouseEvent mouseEvent) {
        dragXOffset = mouseEvent.getX();
        dragYOffset = mouseEvent.getY();
    }

    @FXML
    public void handleGraphPaneDragged(MouseEvent mouseEvent) {
        graphPane.setManaged(false);
        graphPane.setTranslateX(mouseEvent.getX() + graphPane.getTranslateX() - dragXOffset);
        graphPane.setTranslateY(mouseEvent.getY() + graphPane.getTranslateY() - dragYOffset);
        mouseEvent.consume();
    }

    public void setZoomLevel(double value) {
        if (value <= 3 && value >= 0.1) {
            zoomSlider.setValue(100d * value);
            graphPane.setScaleX(value);
            graphPane.setScaleY(value);
        }
    }

    @FXML
    public void showAboutWindow() {
        try {
            URL fxmlFile = new File("src/main/resources/cvrp/about.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(fxmlFile);
            Stage stage = new Stage();
            stage.setTitle("A propos");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPoint(int x, int y, Color color) {
        Circle circle = new Circle();
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setRadius(POINT_RADIUS);
        circle.fillProperty().bind(getColorBinding(color));


        Tooltip tooltip = new Tooltip();
        tooltip.setText("x : " + x/GRAPH_GROWTH + ", y : " + y/GRAPH_GROWTH);
        Tooltip.install(circle, tooltip);

        graphPane.getChildren().add(circle);
    }

    public void addLine(int x1, int y1, int x2, int y2, Color color) {
        Line line = new Line();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);
        line.strokeProperty().bind(getColorBinding(color));

        StackPane arrow = new StackPane();
        String strColor = color.toString().substring(2, 8);
        arrow.styleProperty().bind(Bindings.createStringBinding(() ->
                "-fx-border-width:1px;-fx-shape: \"M0,-4L4,0L0,4Z\";" +
                        String.format("-fx-background-color:#%s;-fx-border-color:#%s",
                                colorCheckbox.isSelected() ? strColor : "000000",
                                colorCheckbox.isSelected() ? strColor : "000000"), colorCheckbox.selectedProperty()
        ));

        arrow.setLayoutX(x2 - ARROW_HEAD_SIZE);
        arrow.setLayoutY(y2 - ARROW_HEAD_SIZE / 2.0);
        arrow.setPrefSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.setMaxSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.setMinSize(ARROW_HEAD_SIZE, ARROW_HEAD_SIZE);
        arrow.visibleProperty().bind(arrowCheckbox.selectedProperty());

        double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
        double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
        double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
        double dt = lineLength - (POINT_RADIUS / 2.0) - (ARROW_HEAD_SIZE / 2.0);

        double t = dt / lineLength;
        double tX = ((1 - t) * line.getStartX()) + (t * line.getEndX());
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

    public ObjectBinding<Color> getColorBinding(Color color) {
        return Bindings.createObjectBinding(() ->
                colorCheckbox.isSelected() ? color : Color.BLACK, colorCheckbox.selectedProperty());
    }

    public void setLoading(double value) {
        loadingPercentage.setText(String.format("%.0f%%", value));
        loadingProgressBar.setProgress(value);
    }
}
