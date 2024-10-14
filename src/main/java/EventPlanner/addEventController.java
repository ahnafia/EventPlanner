package EventPlanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addEventController {
    @FXML
    TextField namefield, descriptionfield, locationfield, categoryfield, assemblyName;

    @FXML
    DatePicker date, assemblyDate;

    @FXML
    Button done, clear, assembly, gradeActivity;

    @FXML
    protected void onClickAssembly() throws IOException {
        DashboardController.openScene("AddAssembly.fxml");
        Stage stage = (Stage) assembly.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickGradeActivity() throws IOException {
        DashboardController.openScene("addActivity.fxml");
        Stage stage = (Stage) gradeActivity.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onClickDoneActivity() throws SQLException, IOException {
        Connection connection = null;
        PreparedStatement insertActivity = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        //Creates connection to the database

        if (namefield.getText() != null &&
        descriptionfield.getText() != null && locationfield.getText() != null &&  categoryfield.getText() != null
                && date.getValue() != null){
            insertActivity = connection.prepareStatement("INSERT INTO EVENTS (Event_Name, date, eventType, description, location) VALUES (?,?,?,?,?)");
            insertActivity.setString(1, namefield.getText());
            insertActivity.setString(2, String.valueOf(date.getValue()));
            insertActivity.setString(3, "GradeEvent");
            insertActivity.setString(4, descriptionfield.getText());
            insertActivity.setString(5, locationfield.getText());
            insertActivity.executeUpdate();
            //Checks to see if values are not null, then adds them to their respective columns

            DashboardController.dashboard.events.add(new Event(String.valueOf(date.getValue()), namefield.getText(),
                    locationfield.getText(), descriptionfield.getText()));
            //Adds event object to the arraylist

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GradeEvent.fxml"));
            AnchorPane anchorPane = loader.load();
            EventController eventController = loader.getController();
            eventController.setUpEntryGradeEvent(String.valueOf(date.getValue()), namefield.getText(),
                    locationfield.getText(), descriptionfield.getText()
            , DashboardController.numEvent - 1);
            DashboardController.numEvent++;
            //Opens GradeEvent and runs method which sets up all the information on the window

            Stage stage = new Stage();
            stage.setScene(new Scene(anchorPane));
            stage.show();
        }
    }

    @FXML
    protected void onClickDoneAssembly() throws SQLException, IOException {
        Connection connection = null;
        PreparedStatement insertAssembly = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");

        if (assemblyName.getText() != null && assemblyDate.getValue() != null){
            insertAssembly = connection.prepareStatement("INSERT INTO EVENTS (Event_Name, date, eventType) VALUES (?,?,?)");
            insertAssembly.setString(1, assemblyName.getText());
            insertAssembly.setString(2, String.valueOf(assemblyDate.getValue()));
            insertAssembly.setString(3, "Assembly");
            insertAssembly.executeUpdate();

            DashboardController.dashboard.events.add(new Assembly(assemblyName.getText(), String.valueOf(assemblyDate.getValue())));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Assembly.fxml"));
            AnchorPane anchorPane = loader.load();
            DashboardController.dashboard.events.add(new Assembly(assemblyName.getText(), String.valueOf(assemblyDate.getValue())));
            EventController eventController = loader.getController();
            eventController.setUpEntryAssembly(String.valueOf(assemblyDate.getValue()), assemblyName.getText(),
                    DashboardController.numEvent);
            DashboardController.numEvent++;

            Stage stage = new Stage();
            stage.setScene(new Scene(anchorPane));
            stage.show();
        }
    }

    @FXML
    protected void onClickClear(){

    }


}
