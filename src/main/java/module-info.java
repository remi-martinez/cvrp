module cvrp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;

    opens cvrp.controller to javafx.fxml;
    exports cvrp;
    opens cvrp.model.algorithm to javafx.fxml;
    opens cvrp.model.csv to javafx.fxml;
    opens cvrp.model.entity to javafx.fxml;
    opens cvrp.model.utils to javafx.fxml;
}