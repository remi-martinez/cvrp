package cvrp.controller;

import cvrp.App;
import cvrp.model.*;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class RoutingController implements Initializable {

    private static int GRAPH_GROWTH = 5;
    private final static int POINT_RADIUS = 5;
    private final static int ARROW_HEAD_SIZE = 10;
    private final static List<Color> AVAILABLE_COLORS = Arrays.asList(Color.RED, Color.ORANGE,
            Color.GREEN, Color.DARKGREEN, Color.BLUE, Color.CORNFLOWERBLUE,Color.CRIMSON, Color.VIOLET);

    @FXML private AnchorPane graphPane;
    @FXML private Label statNbClients;
    @FXML private Label statNbVehicles;
    @FXML private Label statFitness;
    @FXML private Label graphZoneLabel;
    @FXML private Label fileLabel;

    @FXML private AnchorPane loadingPane;
    @FXML private Label loadingPercentage;
    @FXML private ProgressBar loadingProgressBar;

    @FXML private Slider zoomSlider;
    @FXML private Label zoomPercentage;
    @FXML private Label mouseCoordinates;

    // For AnchorPane dragging
    private double dragXOffset = 0;
    private double dragYOffset = 0;

    @FXML private CheckBox arrowCheckbox;
    @FXML private CheckBox colorCheckbox;
    @FXML private TextField graphGrowthTxt;

    @FXML private ComboBox algoTypeSelect;
    @FXML private Button startSimulationBtn;

    @FXML private CheckBox chkbox2Opt;
    @FXML private CheckBox chkboxExchange;
    @FXML private CheckBox chkboxRelocate;

    private Graph currentGraph;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoomPercentage.textProperty().bind(Bindings.createStringBinding(() -> Math.round(zoomSlider.getValue()) + "%", zoomSlider.valueProperty()));
        zoomSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            setZoomLevel((double) newValue / 100d);
        });
//        startSimulationBtn.disableProperty().bind(Bindings.createBooleanBinding(() -> currentGraph == null)); // FIXME


        this.algoTypeSelect.getItems().addAll(Algorithm.values());
        this.algoTypeSelect.getSelectionModel().selectFirst();



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
        this.graphPane.getChildren().clear();
        this.updateGraphStats();
        this.drawGraph(graph);
    }

    public void updateGraphStats() {
        if(currentGraph == null) return;

        this.statNbClients.setText(currentGraph.getClientList().size() + "");
        this.statNbVehicles.setText(currentGraph.getVehicles().size() + "");
        this.statFitness.setText(new DecimalFormat("#0.00").format(currentGraph.getFitness()));
    }

    @FXML
    public void resetGraph() {
        centerGraph();
        this.currentGraph = null;
        this.fileLabel.setText("Aucun fichier sélectionné");
        this.fileLabel.setTextFill(Color.DARKGRAY);
        this.statNbClients.setText("0");
        this.statNbVehicles.setText("0");
        this.statFitness.setText("n/a");
        graphPane.getChildren().clear();
    }

    @FXML
    public void startSimulation() {
        loadingPane.setVisible(true);

        Object selectedItem = algoTypeSelect.getSelectionModel().getSelectedItem();

        if (Algorithm.RANDOM.equals(selectedItem)) {
            drawGraph(Generation.graphGeneration(currentGraph, true));
        }
    }

    public void drawGraph(Graph graph) {
        this.graphPane.getChildren().clear();
        this.currentGraph = graph;
        this.updateGraphStats();

        int colorIndex = 0;

        for (Vehicle v : graph.getVehicles()) {
            this.setLoading(0.5f);
            ArrayList<Client> listClient = v.getVisit();
            Color visitColor = AVAILABLE_COLORS.get(colorIndex % AVAILABLE_COLORS.size());
            Client previous = graph.getWarehouse();
            this.addPoint(previous.getPosX() * GRAPH_GROWTH, previous.getPosY() * GRAPH_GROWTH, Color.PINK).setRadius(10);

            colorIndex++;
            for (Client current : listClient) {
                this.addPoint(current.getPosX() * GRAPH_GROWTH, current.getPosY() * GRAPH_GROWTH, visitColor);

                this.addLine(previous.getPosX() * GRAPH_GROWTH,
                        previous.getPosY() * GRAPH_GROWTH,
                        current.getPosX() * GRAPH_GROWTH,
                        current.getPosY() * GRAPH_GROWTH,
                        visitColor);
                previous = current;
            }
            Circle newPoint = this.addPoint(previous.getPosX() * GRAPH_GROWTH, previous.getPosY() * GRAPH_GROWTH, visitColor);
            this.addLine(previous.getPosX() * GRAPH_GROWTH,
                    previous.getPosY() * GRAPH_GROWTH,
                    graph.getWarehouse().getPosX() * GRAPH_GROWTH,
                    graph.getWarehouse().getPosY() * GRAPH_GROWTH,
                    visitColor);
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

    @FXML
    public void centerGraph() {
        graphPane.setTranslateX(0);
        graphPane.setTranslateY(0);
        setZoomLevel(1);
    }

    public void setZoomLevel(double value) {
        if (value <= 3 && value >= 0.1) {
            zoomSlider.setValue(100d * value);
            graphPane.setScaleX(value);
            graphPane.setScaleY(value);
        }
    }

    @FXML
    public void graphGrowthPlus() {
        if(currentGraph == null || GRAPH_GROWTH >= 10) return;
        centerGraph();
        GRAPH_GROWTH++;
        drawGraph(currentGraph);
        graphGrowthTxt.setText(Integer.toString(GRAPH_GROWTH));
    }

    @FXML
    public void graphGrowthMinus() {
        if(currentGraph == null || GRAPH_GROWTH <= 1) return;
        centerGraph();
        GRAPH_GROWTH--;
        drawGraph(currentGraph);
        graphGrowthTxt.setText(Integer.toString(GRAPH_GROWTH));
    }

    @FXML
    public void showAboutWindow() {
        try {
            URL fxmlFile = new File("src/main/resources/cvrp/about.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(fxmlFile);
            Stage stage = new Stage();
            stage.setTitle("A propos");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Circle addPoint(int x, int y, Color color) {
        Circle circle = new Circle();
        circle.setLayoutX(x);
        circle.setLayoutY(y);
        circle.setRadius(POINT_RADIUS);
        circle.fillProperty().bind(getColorBinding(color));


        Tooltip tooltip = new Tooltip();
        tooltip.setText("x : " + x/GRAPH_GROWTH + ", y : " + y/GRAPH_GROWTH);
        Tooltip.install(circle, tooltip);

        graphPane.getChildren().add(circle);
        return circle;
    }

    public Line addLine(int x1, int y1, int x2, int y2, Color color) {
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
        return line;
    }

    public ObjectBinding<Color> getColorBinding(Color color) {
        return Bindings.createObjectBinding(() ->
                colorCheckbox.isSelected() ? color : Color.BLACK, colorCheckbox.selectedProperty());
    }

    public void setLoading(double value) {
        loadingPercentage.setText(String.format("%.0f%%", value));
        loadingProgressBar.setProgress(value);
    }

    @FXML
    public void updateMouseCoordinates(MouseEvent mouseEvent) {
        mouseCoordinates.setText("x : " + (int)(mouseEvent.getX()/GRAPH_GROWTH) +
                " / y : " + (int)(mouseEvent.getY()/GRAPH_GROWTH));
    }
}
