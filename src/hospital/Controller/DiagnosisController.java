package hospital.Controller;

import hospital.Main;
import hospital.Model.Diagnosis;
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

public class DiagnosisController implements Initializable {
    @FXML
    private TextField tfId;

    @FXML
    private TextField tfName;

    @FXML
    private TableView<Diagnosis> tvDiagnosis;

    @FXML
    private TableColumn<Diagnosis, Integer> colId;

    @FXML
    private TableColumn<Diagnosis, String> colName;

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
            loadWardsAndNumPeople("Diagnosis");
        } else if (actionEvent.getSource() == btnFreeBeds) {

            // Не делай, как я, используй ENUM
            loadFreeBeds("Diagnosis");
        }
    }

    @FXML
    public void handleMouseAction() {
        Diagnosis diagnosis = tvDiagnosis.getSelectionModel().getSelectedItem();

        tfId.setText(String.valueOf(diagnosis.getId()));
        tfName.setText(diagnosis.getName());
    }

    // Switch between views
    @FXML
    public void switchViews(javafx.event.ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource() == miPeople) {
            loadPeople();
        } else if (actionEvent.getSource() == miWards) {
            loadWards();
        }
    }

    @FXML
    public void loadPeople() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadWards() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Wards.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadWardsAndNumPeople(String s) throws IOException {
        WardsAndNumPeopleConroller.setFromWhere(s);
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/WardsAndNumPeople.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadFreeBeds(String s) throws IOException {
        FreeBedsController.setFromWhere(s);
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/FreeBeds.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showDiagnosis();
    }

    public void showDiagnosis() {
        ObservableList<Diagnosis> list = getDiagnosisList();

        colId.setCellValueFactory(new PropertyValueFactory<Diagnosis, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Diagnosis, String>("name"));

        tvDiagnosis.setItems(list);
    }

    public ObservableList<Diagnosis> getDiagnosisList() {
        ObservableList<Diagnosis> wardsObservableList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "select * from diagnosis";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            Diagnosis diagnosis;

            while(rs.next()) {
                diagnosis = new Diagnosis(rs.getInt("id"), rs.getString("name"));
                wardsObservableList.add(diagnosis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wardsObservableList;
    }

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


    // Insert a record
    public void insertRecord() {

        try {
            String query = "insert into diagnosis (id, name) values (" + tfId.getText() + ", " + "\'" + tfName.getText() +"\')";
            executeQuery(query);
            showDiagnosis();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.showAndWait();
        }
    }

    // Update a record
    private void updateRecord() {
        String query = "update diagnosis set name=\'" + tfName.getText() + "\' where id=" + tfId.getText();
        executeQuery(query);
        showDiagnosis();
    }

    // Delete a record
    private void deleteRecord() {
        String query = "delete from diagnosis where id=" + tfId.getText();
        executeQuery(query);
        showDiagnosis();
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
