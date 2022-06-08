module cvrp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;

    opens cvrp.controller to javafx.fxml;
    exports cvrp;
    opens cvrp.model to javafx.fxml;
}