package EventPlanner;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnalysisController{
    @FXML
    Slider satisfactionSlider;

    @FXML
    Label averageSatisfaction;
    @FXML
    Label overallScore;

    @FXML
    TextArea feedbackBox;

    @FXML
    VBox feedbackList;

    @FXML
    LineChart<?, ?> graph;

    int id = 0;

    @FXML
    protected void onClickSubmit() throws SQLException {
        Connection connection = null;
        PreparedStatement insertAnalytic = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        //Creates connection to the SQL database

        insertAnalytic = connection.prepareStatement("INSERT INTO Analytics (satisfaction, Feedback, event_id) VALUES (?,?,?)");
        //Makes the operation in SQL which adds values

        if (satisfactionSlider.getValue() == 1 ) { //Checks to see if satisfaction as it it's lowest point
            feedbackBox.setText("Please enter why this event was not satisfactory before submitting");
        } else if (feedbackBox.getText().contains("@") || feedbackBox.getText().contains("$")){ //Checks to see if feedback contains innapprotiate text
            feedbackBox.setText("Please enter appropriate text");
        } else {
            insertAnalytic.setDouble(1, satisfactionSlider.getValue());//adds the satisfaction rate into the statement
            insertAnalytic.setDouble(3, id);
            DashboardController.dashboard.events.get(id).analytics.satisfation.add(satisfactionSlider.getValue());
            if (feedbackBox.getText() != null){ //Checking if the user inputted a value
                DashboardController.dashboard.events.get(id).analytics.feedback.add(feedbackBox.getText());
                insertAnalytic.setString(2, feedbackBox.getText());//adds the feedback to the arraylist
            } else {
                insertAnalytic.setString(2, "");
            }
        }

        insertAnalytic.executeUpdate();//adds the feedback and satisfaction into the database
    }

    @FXML
    protected void OnClickFinish() throws IOException {
        Stage stage = (Stage) satisfactionSlider.getScene().getWindow();
        stage.close();
    }

    public void setUpAnalysis(){
        for (int i = 0; i < DashboardController.dashboard.events.get(id).analytics.feedback.size(); i++) {
            if (!DashboardController.dashboard.events.get(id).analytics.feedback.get(i).equals("")){
                Label feedback = new Label(" " + DashboardController.dashboard.events.get(id).analytics.feedback.get(i));
                Pane pane = new Pane();
                pane.setPrefWidth(200);
                pane.setPrefHeight(100);
                pane.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-background-color: white");
                feedback.setTextFill(Color.BLACK);
                pane.getChildren().addAll(feedback);
                feedbackList.getChildren().add(pane);
            }
        }
        if (!DashboardController.dashboard.events.get(id).analytics.satisfation.isEmpty()){
            averageSatisfaction.setText(String.valueOf(Math.round(DashboardController.dashboard.events.get(id).analytics.getAverageSatisfaction())));
        }
    }

    /**
     * Takes in the average satisfaction for every event and plots it along a graph to show any trend in the satisfaction rate
     * as the year progresses, also the overall average satisfaction for the year.
     * This is from creating a graph and filling the graph with the satisfaction data points and finding the average of all
     * the satisfaction rates for every event for an overall satisfaction rate
     */
    public void setUpOverallAnalysis(){
        //Creates the data chart as a variable and sets the accumulator variable
        XYChart.Series data = new XYChart.Series();
        double overall = 0;

        //Loops through each event and finds its average satisfaction, then adds that value to the accumulator and as a new
        //data point in the data chart
        for (int i = 0; i < DashboardController.numEvent; i++) {
            double satifaction = DashboardController.dashboard.events.get(i).getAnalytics().getAverageSatisfaction();
            overall += satifaction;
            data.getData().add(new XYChart.Data(String.valueOf(i), satifaction));
        }

        //sets the overall score as the average of the accumulator(overall / number of events) and fills the GUI graph
        //with the data from the data chart
        overallScore.setText(String.valueOf(Math.round(overall / DashboardController.numEvent)));
        graph.getData().add(data);

    }
}
