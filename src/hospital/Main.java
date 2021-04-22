package hospital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {
    private static String url;
    private static String user;
    private static String pass;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loadConnectionData();
        Parent root = FXMLLoader.load(getClass().getResource("View/LoginScreen.fxml"));
        primaryStage.setTitle("Hospital DB UI");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void loadConnectionData() {
        try (InputStream input = new FileInputStream("src\\resources\\config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            url=prop.getProperty("url");
            user=prop.getProperty("user");
            pass=prop.getProperty("pass");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }
}

