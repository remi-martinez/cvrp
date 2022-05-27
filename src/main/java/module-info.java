module cvrp {
    requires javafx.controls;
    requires javafx.fxml;

    opens cvrp.controller to javafx.fxml;
    exports cvrp;
}