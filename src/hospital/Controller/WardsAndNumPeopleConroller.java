package hospital.Controller;

import hospital.Main;
import hospital.Model.WardsAndNumPeople;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

public class WardsAndNumPeopleConroller implements Initializable {

    private static String fromWhere;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<WardsAndNumPeople> tvWardsAndNumPeople;

    @FXML
    private TableColumn<WardsAndNumPeople, Integer> colWardId;

    @FXML
    private TableColumn<WardsAndNumPeople, Integer> colNumberOfPeople;

    @FXML
    private Button btnSave;

    @FXML
    public void handleBackButton() throws IOException {
        switch (fromWhere) {
            case ("Wards"):
                loadWards();
                break;
            case ("People"):
                loadPeople();
                break;
            case ("Diagnosis"):
                loadDiagnosis();
                break;
        }
    }

    @FXML
    public void handleSaveButton() throws IOException {
        saveFile();
    }

    @FXML
    public void loadWards() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Wards.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadPeople() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Person.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    public void loadDiagnosis() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Diagnosis.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    public static String getFromWhere() {
        return fromWhere;
    }

    public static void setFromWhere(String fromWhere) {
        WardsAndNumPeopleConroller.fromWhere = fromWhere;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showWardsAndNumPeople();
    }

    public void showWardsAndNumPeople() {
        ObservableList<WardsAndNumPeople> list = getWardsAndNumPeople();

        colWardId.setCellValueFactory(new PropertyValueFactory<WardsAndNumPeople, Integer>("wardId"));
        colNumberOfPeople.setCellValueFactory(new PropertyValueFactory<WardsAndNumPeople, Integer>("numberOfPeople"));

        tvWardsAndNumPeople.setItems(list);
    }

    public ObservableList<WardsAndNumPeople> getWardsAndNumPeople() {
        ObservableList<WardsAndNumPeople> WardsAndNumPeopleObservableList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "select * from routine_1()";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            WardsAndNumPeople wardsAndNumPeople;

            while(rs.next()) {
                wardsAndNumPeople = new WardsAndNumPeople(rs.getInt("ward"), rs.getInt("number_of_patients"));
                WardsAndNumPeopleObservableList.add(wardsAndNumPeople);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return WardsAndNumPeopleObservableList;
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

    public void saveFile() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to save the file?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            List<WardsAndNumPeople> list = getWardsAndNumPeople();

            try {
                File file = new File("wards_and_number_of_patients.txt");
                FileWriter fw = new FileWriter("wards_and_number_of_patients.txt");

                for (WardsAndNumPeople wanp : list) {
                    fw.write("Ward: " + wanp.getWardId() + ". Occupied beds: " + wanp.getNumberOfPeople() + "\n");
                }

                fw.close();
                Alert fileSaved = new Alert(Alert.AlertType.INFORMATION);
                fileSaved.setContentText("File successfully saved");
                fileSaved.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                Alert al = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
