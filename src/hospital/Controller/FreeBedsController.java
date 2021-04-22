package hospital.Controller;

import hospital.Main;
import hospital.Model.FreeBeds;
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

public class FreeBedsController implements Initializable {

    private static String fromWhere;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<FreeBeds> tvFreeBeds;

    @FXML
    private TableColumn<FreeBeds, Integer> colWardId;

    @FXML
    private TableColumn<FreeBeds, Integer> colNumFreeBeds;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnBack;

    @FXML
    private TextField tfLowerLimit;

    @FXML
    private TextField tfUpperLimit;

    @FXML
    private Button btnGetFreeBeds;

    @FXML
    public void handleGetFreeBedsButton() {
        showFreeBeds();
    }

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

    public static void setFromWhere(String fromWhere) {
        FreeBedsController.fromWhere = fromWhere;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfLowerLimit.setText(String.valueOf(0));
        tfUpperLimit.setText(String.valueOf(0));
        showFreeBeds();
    }

    public void showFreeBeds() {
        ObservableList<FreeBeds> list = getFreeBeds();

        colWardId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNumFreeBeds.setCellValueFactory(new PropertyValueFactory<>("numFreeBeds"));

        tvFreeBeds.setItems(list);
    }

    public ObservableList<FreeBeds> getFreeBeds() {
        ObservableList<FreeBeds> freeBedsObservableList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "select * from routine_3(" + tfLowerLimit.getText() + ", " + tfUpperLimit.getText() + ")";
        Statement st;
        ResultSet rs;

        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);

            FreeBeds freeBeds;

            while(rs.next()) {
                freeBeds = new FreeBeds(rs.getInt("w_id"), rs.getInt("free_beds"));
                freeBedsObservableList.add(freeBeds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return freeBedsObservableList;
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
            List<FreeBeds> list = getFreeBeds();

            try {
                File file = new File("wards_and_free_beds.txt");
                FileWriter fw = new FileWriter("wards_and_free_beds.txt");

                for (FreeBeds fb : list) {
                    fw.write("Ward: " + fb.getId() + ". Occupied beds: " + fb.getNumFreeBeds() + "\n");
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
