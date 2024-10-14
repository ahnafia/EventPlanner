package EventPlanner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

public class endOfYearSummaryController implements Initializable {

    @FXML
    HBox assembliesBox, gradeEventBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int length = DashboardController.dashboard.events.size();
        gradeEventBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < length; i++) {
            if (DashboardController.dashboard.events.get(i).description != null){
                Label description = new Label(DashboardController.dashboard.events.get(i).eventName + "\n"
                        + DashboardController.dashboard.events.get(i).description + "\n \n" + "Satisfaction: " +
                        Math.round(DashboardController.dashboard.events.get(i).analytics.getAverageSatisfaction()));

                Pane pane = new Pane();
                pane.setPrefWidth(200);
                pane.setPrefHeight(100);
                pane.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-background-color: White");

                pane.getChildren().addAll(description);
                gradeEventBox.getChildren().add(pane);

            }
        }
    }
}
