package EventPlanner;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.Desktop;

public class DashboardController implements Initializable {
    @FXML
    Button addNewEvent, editMember;

    @FXML
    VBox eventVbox;

    @FXML
    TableView<String> ccview;

    @FXML
    TableColumn<String, String> namesCol;

    static Dashboard dashboard = new Dashboard();
    static int numEvent = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        checkClassCouncil();
        checkEvents();
        checkAnalytics();
        try {
            updateTasks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickRefresh(){
        eventVbox.getChildren().clear();
        checkClassCouncil();
        checkEvents();
        checkAnalytics();
        try {
            updateTasks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


        public void checkClassCouncil(){
        Connection connection = null;
        PreparedStatement ClassCouncilExists = null;
        ResultSet nameList = null;


        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
            ClassCouncilExists = connection.prepareStatement("SELECT Names FROM ClassCouncil");
            nameList = ClassCouncilExists.executeQuery();

            dashboard.ClassCouncil.clear();
            while (nameList.next()){
                dashboard.ClassCouncil.add(nameList.getString("Names"));
            }

            if (dashboard.ClassCouncil.isEmpty()){
                editMember.fire();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ObservableList<String> names = FXCollections.observableArrayList(dashboard.ClassCouncil);
        ccview.setItems(names);
        namesCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    }

    private void checkEvents(){
        Connection connection = null;
        PreparedStatement getEvents = null;
        ResultSet eventList = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
            getEvents= connection.prepareStatement("SELECT * FROM Events");
            eventList = getEvents.executeQuery();

            while (eventList.next()){
                String name = eventList.getString("Event_Name");
                String date = eventList.getString("date");
                String description = "";
                String location = eventList.getString("location");
                if (eventList.getString("description") == null){
                    dashboard.events.add(new Assembly(name, date));
                     numEvent++;
                } else {
                    description = eventList.getString("description");
                    dashboard.events.add(new Event(name, date, description, location));
                    numEvent++;
                }

                Pane pane = new Pane();
                pane.setPrefWidth(200);
                pane.setPrefHeight(100);
                pane.setStyle("-fx-border-color: black; -fx-padding: 15px; -fx-background-color: white");

                Label label = new Label( name + "\n" +  description);
                label.setLayoutX(10);
                label.setLayoutY(10);

                Button button = new Button("Edit");
                button.setPrefSize(40, 20);
                button.setLayoutX(10);
                button.setLayoutY(60);
                button.setOnAction(event -> {
                    int index = eventVbox.getChildren().indexOf(pane);
                            if (dashboard.events.get(index).eventType.equals("Assembly")){
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Assembly.fxml"));
                                AnchorPane anchorPane = null;
                                try {
                                    anchorPane = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                EventController eventController = loader.getController();
                                try {
                                    eventController.setUpEntryAssembly(dashboard.events.get(index).getDate(), dashboard.events.get(index).getEventName(), index);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                Stage stage = new Stage();
                                stage.setScene(new Scene(anchorPane));
                                stage.show();
                            } else {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("GradeEvent.fxml"));
                                AnchorPane anchorPane = null;
                                try {
                                    anchorPane = loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                EventController eventController = loader.getController();
                                eventController.setUpEntryGradeEvent(dashboard.events.get(index).getDate(), dashboard.events.get(index).getEventName(),
                                        dashboard.events.get(index).location, dashboard.events.get(index).description, index);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(anchorPane));
                                stage.show();
                            }
                        }
                );
                Button button1 =  new Button("Analytics");
                button1.setPrefSize(70, 30);
                button1.setLayoutX(150);
                button1.setLayoutY(60);
                button1.setOnAction(event -> {
                    int index = eventVbox.getChildren().indexOf(pane);
                    if (!DashboardController.dashboard.events.get(index).analytics.satisfation.isEmpty()) {
                        String totalFeedback = DashboardController.dashboard.events.get(index).getAnalytics().getFeedback();
                        double averageSatisfaction = DashboardController.dashboard.events.get(index).getAnalytics().getAverageSatisfaction();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Analysis.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        AnalysisController analysisController = loader.getController();
                        analysisController.id = index;
                        analysisController.setUpAnalysis();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(anchorPane));
                        stage.show();

                    } else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("feedbackCollection.fxml"));
                        AnchorPane anchorPane = null;
                        try {
                            anchorPane = loader.load();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        AnalysisController analysisController = loader.getController();
                        analysisController.id = index;
                        analysisController.setUpAnalysis();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(anchorPane));
                        stage.show();
                    }


                });


                pane.getChildren().addAll(label, button, button1);
                eventVbox.getChildren().add(pane);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkAnalytics(){
        Connection connection = null;
        PreparedStatement analyticsCheck = null;
        ResultSet analyticsList = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
            analyticsCheck = connection.prepareStatement("SELECT * FROM Analytics");
            analyticsList = analyticsCheck.executeQuery();

            while (analyticsList.next()){
                dashboard.events.get(analyticsList.getInt("event_id")).
                        analytics.satisfation.add(analyticsList.getDouble("satisfaction"));
                dashboard.events.get(analyticsList.getInt("event_id")).
                        analytics.feedback.add(analyticsList.getString("Feedback"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTasks() throws SQLException {
        Connection connection = null;
        PreparedStatement checkTasks = null;
        ResultSet tasks = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eventplanner", "root", "Tanu@1976");
        checkTasks = connection.prepareStatement("SELECT * FROM Tasks");
        tasks = checkTasks.executeQuery();

        int i;
        if (!DashboardController.dashboard.events.isEmpty()){
            while (tasks.next()){
                i = tasks.getInt("event_id");
                DashboardController.dashboard.events.get(i).tasks.add(new Task(tasks.getString("Task_Name"),
                        tasks.getString("who"), Boolean.parseBoolean(tasks.getString("status")),
                        tasks.getInt("event_id")));
            }
        }
    }



    public static void openScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(DashboardController.class.getResource(fxmlFile));
        AnchorPane anchorPane = loader.load();
        Stage stage = new Stage();
        stage.setX(1000);
        stage.setY(-100);
        stage.setScene(new Scene(anchorPane));
        stage.show();
    }

    @FXML
    protected void OnClickEditMembers() throws IOException {
        openScene("ClassCouncilMembers.fxml");
    }

    @FXML
    protected void onClickAddNewEvent() throws IOException {
        openScene("addEventChoice.fxml");
    }

    @FXML
    protected void onClickOpenOverallAnalytics() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("overallAnalytics.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AnalysisController analysisController = loader.getController();
        analysisController.setUpOverallAnalysis();
        Stage stage = new Stage();
        stage.setScene(new Scene(anchorPane));
        stage.show();
    }

    /**
     * Opens a file showing all the pending tasks for all events
     * @throws IOException
     */
    @FXML
    protected void onClickOpenPendingTasks() throws IOException {
        File file = new File("pendingtasks.txt"); //Creates a file

        PrintWriter printWriter = new PrintWriter(file);
        ArrayList<String> pendingTasks = dashboard.AllPendingTasks(); //Uses AllPendingTasks()
        for (int i = 0; i < pendingTasks.size(); i++) {
            printWriter.println(pendingTasks.get(i)); //Adds new line of a pending task to the file
        }
        printWriter.close();

        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()){
                desktop.open(file); //Opens file
            }
        }
    }

    @FXML
    protected void onClickOpenWorkD() throws IOException {
        String[] distribution = dashboard.WorkDistribution();

        File file = new File("workdistribution.txt");

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println("Work distribution");
        printWriter.println();
        printWriter.println("Member name: Tasks given");
        printWriter.println();
        for (int i = 0; i < distribution.length; i++) {
            printWriter.println(distribution[i]);
        }
        printWriter.close();

        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()){
                desktop.open(file);
            }
        }
    }

    @FXML
    protected void onClickOpenSummary() throws IOException {
        openScene("endOfYearSummary.fxml");
    }
}
