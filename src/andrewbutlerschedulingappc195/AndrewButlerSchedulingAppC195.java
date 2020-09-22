/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195;

import andrewbutlerschedulingappc195.Utils.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author andrb
 */
public class AndrewButlerSchedulingAppC195 extends Application {
    
    @Override
    public void start(Stage stage) throws IOException{
        ResourceBundle rb = ResourceBundle.getBundle("Language/LoginLang", Locale.getDefault());
        //ResourceBundle rb = ResourceBundle.getBundle("Language/LoginLang", Locale.JAPANESE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewController/Login.fxml"));
        loader.setResources(rb);
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
    
}
