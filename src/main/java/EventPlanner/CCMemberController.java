package EventPlanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CCMemberController implements Initializable {
    @FXML
    TextField member1, member2, member3, member4, member5, member6;

    @FXML
    Label warning;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warning.setVisible(false);
    }

    @FXML
    protected void OnClickDone() throws SQLException, IOException {
        Connection connection = null;
        PreparedStatement insertNames = null;

        if (member1.getText().equals("") && member2.getText().equals("") && member3.getText().equals("")
                && member4.getText().equals("") && member5.getText().equals("") &&
                member6.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("please fill this out");
            alert.show();
        } else {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
            insertNames = connection.prepareStatement("INSERT INTO ClassCouncil (names) VALUES (?)");
            insertNames.setString(1, member1.getText());
            insertNames.executeUpdate();
            insertNames.setString(1, member2.getText());
            insertNames.executeUpdate();
            insertNames.setString(1, member3.getText());
            insertNames.executeUpdate();
            insertNames.setString(1, member4.getText());
            insertNames.executeUpdate();
            insertNames.setString(1, member5.getText());
            insertNames.executeUpdate();
            insertNames.setString(1, member6.getText());
            insertNames.executeUpdate();
            Stage stage2 = (Stage) member1.getScene().getWindow();
            stage2.close();
        }
    }
}
