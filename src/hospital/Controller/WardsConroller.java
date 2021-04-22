package hospital.Controller;

import hospital.Controller.FreeBedsController;
import hospital.Controller.WardsAndNumPeopleConroller;
import hospital.Main;
import hospital.Model.Wards;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class WardsConroller implements Initializable {
    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfMaxCount;

    @FXML
    private TableView<Wards> tvWards;

    @FXML
    private TableColumn<Wards, Integer> colId;

    @FXML
    private TableColumn<Wards, String> colName;

    @FXML
    private TableColumn<Wards, Integer> colMaxCount;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button btnWardsAndNumPeople;

    @FXML
    private Button btnFreeBeds;

    @FXML
    private Menu mTables;

    @FXML
    private MenuItem miPeople;

    @FXML
    private MenuItem miWards;

    @FXML
    private MenuItem miDiagnosis;

    @FXML
    public void handleButtonAction (javafx.event.ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource() == btnAdd) {
            insertRecord();
        } else if (actionEvent.getSource() == btnUpdate) {
            updateRecord();
        } else if (actionEvent.getSource() == btnDelete) {
            deleteRecord();
        } else if (actionEvent.getSource() == btnWardsAndNumPeople) {

            // Не делай, как я, используй ENUM
            loadWardsAndNumPeople("Wards");
        } else if (actionEvent.getSource() == btnFreeBeds) {

            // Не делай, как я, используй ENUM
            loadFreeBeds("Wards");
        }
    }

    @FXML
    public void handleMouseAction() {
        Wards ward = tvWards.getSelectionModel().getSelectedItem();

        tfId.setText(String.valueOf(ward.getId()));
        tfName.setText(ward.getName());
        tfMaxCount.setText(String.valueOf(ward.getMaxCount()));
    }

    // Switch between views
    @FXML
    public void switchViews(javafx.event.ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource() == miPeople) {
            loadPeople();
        } else if (actionEvent.getSource() == miDiagnosis) {
            loadDiagnosis();
        }
    }

    @FXML
    public void loadDiagnosis() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Diagnosis.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadPeople() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadWardsAndNumPeople(String s) throws IOException {
        WardsAndNumPeopleConroller.setFromWhere(s);
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/WardsAndNumPeople.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showWards();
    }

    @FXML
    public void loadFreeBeds(String s) throws IOException {
        FreeBedsController.setFromWhere(s);
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/FreeBeds.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    // get connection to the database
    public Connection getConnection() {
        Connection conn;

        try {
            conn = DriverManager.getConnection(Main.getUrl(), Main.getUser(), Main.getPass());
            return conn;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }


    public ObservableList<Wards> getWardsList() {
        ObservableList<Wards> wardsObservableList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "select * from wards";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            Wards wards;

            while(rs.next()) {
                wards = new Wards(rs.getInt("id"), rs.getString("name"), rs.getInt("max_count"));
                wardsObservableList.add(wards);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wardsObservableList;
    }

    public void showWards() {
        ObservableList<Wards> list = getWardsList();

        colId.setCellValueFactory(new PropertyValueFactory<Wards, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Wards, String>("name"));
        colMaxCount.setCellValueFactory(new PropertyValueFactory<Wards, Integer>("maxCount"));

        tvWards.setItems(list);
    }

    // Insert a record
    public void insertRecord() {

        try {
            String query = "insert into wards (id, name, max_count) values (" + tfId.getText() + ", " + "\'" + tfName.getText() +"\', " + tfMaxCount.getText() +")";
            executeQuery(query);
            showWards();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.showAndWait();
        }
    }

    // Update a record
    private void updateRecord() {
        String query = "update wards set name=\'" + tfName.getText() + "\', " + "max_count=" + tfMaxCount.getText() + "where id=" + tfId.getText();
        executeQuery(query);
        showWards();
    }


    // Delete a record
    private void deleteRecord() {
        String query = "delete from wards where id=" + tfId.getText();
        executeQuery(query);
        showWards();
    }

    private void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }
}
