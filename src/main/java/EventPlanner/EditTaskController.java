package EventPlanner;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditTaskController {
    @FXML
    TextField editName;

    @FXML
    ChoiceBox<String> editStatus, editWho;

    int eventIndex;
    String name;

    public void setUpEditTask(String name, int eventIndex){
        editStatus.getItems().addAll("false", "true");
        editName.setText(name);
        editWho.getItems().addAll(DashboardController.dashboard.ClassCouncil);
        this.eventIndex = eventIndex;
        this.name = name;
    }

    @FXML
    protected void onClickEditTaskDone() throws SQLException {
        Connection connection = null;
        PreparedStatement editTask = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        try {
            editTask = connection.prepareStatement("UPDATE Tasks SET Task_Name = ?, Who = ?, Status = ? WHERE (Task_Name = ? AND event_id = ?)");
            editTask.setString(1, editName.getText());
            editTask.setString(2, String.valueOf(editWho.getValue()));
            editTask.setString(3, String.valueOf(editStatus.getValue()));
            editTask.setString(4, name);
            editTask.setInt(5, eventIndex + 1);
            editTask.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onClickClearTask(){

    }
}
