package hospital.Controller;

import hospital.Main;
import hospital.Model.Person;
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

public class PersonConroller implements Initializable {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPatronymicName;

    @FXML
    private TextField tfWard;

    @FXML
    private TextField tfDiagnosis;

    @FXML
    private TableView<Person> tvPerson;

    @FXML
    private TableColumn<Person, Integer> colId;

    @FXML
    private TableColumn<Person, String> colFrstName;

    @FXML
    private TableColumn<Person, String> colLastName;

    @FXML
    private TableColumn<Person, String> colPatronimicName;

    @FXML
    private TableColumn<Person, Integer> colWardId;

    @FXML
    private TableColumn<Person, Integer> colDiagnosisId;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

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
            loadWardsAndNumPeople("People");
        } else if (actionEvent.getSource() == btnFreeBeds) {

            // Не делай, как я, используй ENUM
            loadFreeBeds("People");
        }
    }

    // Switch between views
    @FXML
    public void switchViews(javafx.event.ActionEvent actionEvent) throws IOException {
        if(actionEvent.getSource() == miDiagnosis) {
            loadDiagnosis();
        } else if (actionEvent.getSource() == miWards) {
            loadWards();
        }
    }


    @FXML
    public void loadDiagnosis() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Diagnosis.fxml"));
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
        showPerson();
    }

    @FXML
    public void handleMouseAction() {
        Person person = tvPerson.getSelectionModel().getSelectedItem();

        tfId.setText(String.valueOf(person.getId()));
        tfFirstName.setText(person.getFirstName());
        tfLastName.setText(person.getLastName());
        tfPatronymicName.setText(person.getPatronymicName());
        tfWard.setText(String.valueOf(person.getWardId()));
        tfDiagnosis.setText(String.valueOf(person.getDiagnosisId()));

    }

    public void showPerson() {
        ObservableList<Person> list = getPersonList();

        colId.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
        colFrstName.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPatronimicName.setCellValueFactory(new PropertyValueFactory<>("patronymicName"));
        colWardId.setCellValueFactory(new PropertyValueFactory<>("wardId"));
        colDiagnosisId.setCellValueFactory(new PropertyValueFactory<>("diagnosisId"));

        tvPerson.setItems(list);
//        cbViews.getItems().addAll("People", "Wards", "Diagnoses");
    }

    public ObservableList<Person> getPersonList() {
        ObservableList<Person> wardsObservableList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "select * from people";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            Person person;

            while(rs.next()) {
                person = new Person(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("patronymic_name"), rs.getInt("ward_id"), rs.getInt("diagnosis_id"));
                wardsObservableList.add(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wardsObservableList;
    }

    // Connect to the DB
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
            String query = "insert into people (id, first_name, last_name, patronymic_name, ward_id, diagnosis_id) values ("
                    + tfId.getText() + ", " + "\'" + tfFirstName.getText() +"\', "
                    + "\'" + tfFirstName.getText() + "\', "
                    + "\'" + tfPatronymicName.getText() + "\', "
                    + tfWard.getText() + ", " + tfDiagnosis.getText() + ")";
            executeQuery(query);
            showPerson();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.showAndWait();
        }
    }


    // Update a record
    private void updateRecord() {
        String query = "update people set first_name=\'" + tfFirstName.getText() + "\', "
                + "last_name=\'" + tfLastName.getText() + "\', "
                + "patronymic_name=\'" + tfPatronymicName.getText() + "\', "
                + "ward_id=" + tfWard.getText() + ", "
                + "diagnosis_id=" + tfDiagnosis.getText()
                + " where id=" + tfId.getText();
        System.out.println(query);
        executeQuery(query);
        showPerson();
    }

    // Delete a record
    private void deleteRecord() {
        String query = "delete from people where id=" + tfId.getText();
        executeQuery(query);
        showPerson();
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
