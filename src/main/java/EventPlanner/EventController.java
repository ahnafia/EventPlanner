package EventPlanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EventController implements Initializable {
    @FXML
    Label date, title, gradeEventLocation, gradeEventTitle, gradeEventDate, gradeEventDescription;

    @FXML
    TextField taskFieldAssembly, taskField, activityField, categoryField, resourcesField, timeField;

    @FXML
    ChoiceBox<String> memberListAssembly, memberList;

    @FXML
    TableView tasksviewAssembly, taskView, timespan;

    @FXML
    TableColumn taskAssembly, whoAssembly, statusAssembly, editAssembly;

    @FXML
    TableColumn task, who, status, edit, activity, resources, category;

    int id = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        PreparedStatement checkEvents = null;
        ResultSet events = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
            checkEvents = connection.prepareStatement("SELECT * FROM Events");
            events = checkEvents.executeQuery();

            while (events.next()){
                if (events.getString("Event_Name").equals(title)){
                    id = Integer.parseInt(events.getString("event_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            updateTasks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTasks() throws SQLException {
        DashboardController.dashboard.events.get(id).tasks.clear();
        Connection connection = null;
        PreparedStatement checkTasks = null;
        ResultSet tasks = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        checkTasks = connection.prepareStatement("SELECT * FROM Tasks");
        tasks = checkTasks.executeQuery();

        while (tasks.next()){
            if (tasks.getInt("event_id") == id){
                DashboardController.dashboard.events.get(id).tasks.add(new Task(tasks.getString("Task_Name"),
                        tasks.getString("who"), Boolean.parseBoolean(tasks.getString("status")),
                        tasks.getInt("event_id")));
            }
        }
    }

    public void updateAssemblyTimeSlots() throws SQLException {
        Connection connection = null;
        PreparedStatement checkTimeSlots = null;
        ResultSet timeSlots = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        checkTimeSlots = connection.prepareStatement("SELECT * FROM assembly_timeslots");
        timeSlots = checkTimeSlots.executeQuery();

        ((Assembly) (DashboardController.dashboard.events.get(id))).timeslots.clear();
            while (timeSlots.next()){
                if (timeSlots.getInt("assembly_id") == id + 1){
                    ((Assembly) (DashboardController.dashboard.events.get(id))).addToTimeSlot(
                            new Activity(timeSlots.getString("activity"),
                                    timeSlots.getString("category"), timeSlots.getString("resources"),
                                    timeSlots.getInt("time"), timeSlots.getInt("assembly_id")));
                }
            }
    }

    public void setUpTimeSlots() throws SQLException {
        timespan.getItems().clear();
        updateAssemblyTimeSlots();
        ObservableList<Activity> ol = FXCollections.observableArrayList(((Assembly) (DashboardController.dashboard.events.get(id))).timeslots);
        activity.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        category.setCellValueFactory(new PropertyValueFactory<Task, String>("category"));
        resources.setCellValueFactory(new PropertyValueFactory<Task, String>("resources"));;
        timespan.setItems(ol);
    }




    public void setUpTaskAssembly(){
        try {
            tasksviewAssembly.getItems().clear();
            updateTasks();
            ObservableList<Task> ol = FXCollections.observableArrayList(DashboardController.dashboard.events.get(id).tasks);
            taskAssembly.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
            whoAssembly.setCellValueFactory(new PropertyValueFactory<Task, String>("who"));
            statusAssembly.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
            Callback<TableColumn<Task, String>, TableCell<Task, String>> factor = (TableColumn<Task, String> param) -> {
                TableCell<Task, String> cellValue = new TableCell<Task, String>(){
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if (empty){
                            setGraphic(null);
                        } else {
                            Button edit = new Button("edit");
                            edit.setOnAction(event -> {
                                int index = getIndex();
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("editTask.fxml"));
                                AnchorPane anchorPane = null;
                                try {
                                    anchorPane = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                EditTaskController editTaskController = loader.getController();
                                editTaskController.setUpEditTask(DashboardController.dashboard.events.get(id).tasks.get(index).getName(), id);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(anchorPane));
                                stage.show();
                            });
                            setGraphic(edit);
                        }
                    }
                };
                return cellValue;
            };
            editAssembly.setCellFactory(factor);
            tasksviewAssembly.setItems(ol);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUpTasks(){
        try {
            taskView.getItems().clear();
            updateTasks();
            ObservableList<Task> ol = FXCollections.observableArrayList(DashboardController.dashboard.events.get(id).tasks);
            task.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
            who.setCellValueFactory(new PropertyValueFactory<Task, String>("who"));
            status.setCellValueFactory(new PropertyValueFactory<Task, String>("status"));
            Callback<TableColumn<Task, String>, TableCell<Task, String>> factor = (TableColumn<Task, String> param) -> {
                TableCell<Task, String> cellValue = new TableCell<Task, String>(){
                    public void updateItem(String item, boolean empty){
                        super.updateItem(item, empty);
                        if (empty){
                            setGraphic(null);
                        } else {
                            Button edit = new Button("edit");
                            edit.setOnAction(event -> {
                                int index = getIndex();
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("editTask.fxml"));
                                AnchorPane anchorPane = null;
                                try {
                                    anchorPane = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                EditTaskController editTaskController = loader.getController();
                                editTaskController.setUpEditTask(DashboardController.dashboard.events.get(id).tasks.get(index).getName(), id);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(anchorPane));
                                stage.show();
                            });
                            setGraphic(edit);
                        }
                    }
                };
                return cellValue;
            };
            edit.setCellFactory(factor);
            taskView.setItems(ol);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void setUpEntryAssembly(String dateData, String titleData, int id) throws SQLException {
        memberListAssembly.getItems().addAll(DashboardController.dashboard.ClassCouncil);
        date.setText(dateData);
        title.setText(titleData);
        this.id = id;
        setUpTaskAssembly();
        setUpTimeSlots();
    }

    public void setUpEntryGradeEvent(String dateData, String titleData, String locationData, String description, int id){
        memberList.getItems().addAll(DashboardController.dashboard.ClassCouncil);
        gradeEventLocation.setText(locationData);
        gradeEventTitle.setText(titleData);
        gradeEventDate.setText(dateData);
        gradeEventDescription.setText(description);
        this.id = id;
        setUpTasks();
    }

    @FXML
    protected void onClickAddTaskAssembly() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        if (taskFieldAssembly.getText() != null && memberListAssembly.getValue() != null){
            PreparedStatement addTask = null;
            addTask = connection.prepareStatement("INSERT INTO Tasks (Task_Name, Who, Status, event_id) VALUES (?, ?, ?, ?)");
            addTask.setString(1, taskFieldAssembly.getText());
            addTask.setString(2, memberListAssembly.getValue());
            addTask.setString(3, "false");
            addTask.setInt(4, id);
            addTask.executeUpdate();

            setUpTaskAssembly();

        }
    }

    @FXML
    protected void onClickAddTimeslot() throws IOException, NullPointerException{
        DashboardController.openScene("addTimeSlot.fxml");
    }

    @FXML
    protected void onClickAddActivity() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        if (activityField.getText() != null && categoryField.getText() != null && resourcesField.getText() != null
                && timeField.getText() != null){
            PreparedStatement addTimeSlot = null;
            try {
                addTimeSlot = connection.prepareStatement("INSERT INTO assembly_timeslots (activity, category, resources, assembly_id, time) VALUES (?, ?, ?, ?, ?)");
                addTimeSlot.setString(1, activityField.getText());
                addTimeSlot.setString(2, categoryField.getText());
                addTimeSlot.setString(3, resourcesField.getText());
                addTimeSlot.setInt(4, id + 1);
                addTimeSlot.setInt(5, Integer.parseInt(timeField.getText()));
                addTimeSlot.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException numberFormatException){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please use numbers for the time");
                alert.show();
            }
        }
        Stage stage = (Stage) activityField.getScene().getWindow();
        stage.close();
        setUpTimeSlots();
    }

    @FXML
    protected void onClickAddTask() throws SQLException {
        Connection connection = null;
        PreparedStatement checkEvents = null;
        ResultSet events = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");


        if (taskField.getText() != null && memberList.getValue() != null){
            PreparedStatement addTask = null;
            addTask = connection.prepareStatement("INSERT INTO Tasks (Task_Name, Who, Status, event_id) VALUES (?, ?, ?, ?)");
            addTask.setString(1, taskField.getText());
            addTask.setString(2, memberList.getValue());
            addTask.setString(3, "false");
            addTask.setInt(4, id);
            addTask.executeUpdate();
            setUpTasks();
    }
    }





}
