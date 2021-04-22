package hospital.Controller;


import hospital.SecurityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField tfUser;

    @FXML
    private PasswordField pfPass;

    @FXML
    private Button btnLetMeIn;

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void handleLoginButton() throws IOException {
        SecurityManager sm = new SecurityManager();

        if (sm.logInSuccessful(tfUser.getText(), pfPass.getText())) {
            loadWards();
        } else {
            Alert loginFailed = new Alert(Alert.AlertType.ERROR);
            loginFailed.setContentText("Login failed");
            loginFailed.showAndWait();
        }
    }

    @FXML
    public void loadWards() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("hospital/View/Wards.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
