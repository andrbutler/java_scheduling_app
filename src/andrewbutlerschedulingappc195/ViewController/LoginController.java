/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.ViewController;

import andrewbutlerschedulingappc195.Model.User;
import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import andrewbutlerschedulingappc195.Utils.LoginLogger;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrb
 */
public class LoginController implements Initializable {

    @FXML
    private Label userLabel;
    @FXML
    private TextField userText;
    @FXML
    private PasswordField passText;
    @FXML
    private Label title;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    private String userName;
    private String password;
    private String sql;
    private ResourceBundle rb;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        title.setText(rb.getString("title"));
        userLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        okButton.setText(rb.getString("login"));
        cancelButton.setText(rb.getString("cancel"));
    }

    public void okButtonHandler(ActionEvent e) throws SQLException, IOException {
        userName = userText.getText();
        password = passText.getText();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        sql = "select userName, password, userId from user where userName =?";
        DBQuery.setConn(DBConnection.getConn());
        DBQuery.setSql(sql);
        PreparedStatement statement = DBQuery.getPs();
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            LoginLogger.log(userName, false, currentTimestamp);
            ButtonType ok = new ButtonType(rb.getString("ok"), ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(AlertType.ERROR, rb.getString("error"), ok);
            alert.setTitle(rb.getString("errorTitle"));
            alert.setHeaderText(rb.getString("errorTitle"));
            userText.setText("");
            passText.setText("");
            alert.showAndWait();
            rs.close();
            statement.close();
        } else {

            if (rs.getString("password").equals(password)) {
                User.setUserName(userName);
                User.setUserId(rs.getInt("userId"));

                LoginLogger.log(User.getUserName(), true, currentTimestamp);
                rs.close();
                statement.close();
                sql = "select u.userName, a.start, c.customerName from user u inner join appointment a on u.userId = a.userId inner join customer c on a.customerId = c.customerId where userName = ? and a.start >= ? and a.start <= ?";
                DBQuery.setSql(sql);
                statement = DBQuery.getPs();
                statement.setString(1, User.getUserName());
                statement.setTimestamp(2, currentTimestamp);
                statement.setTimestamp(3, new Timestamp(currentTimestamp.getTime() + (15 * 60 * 1000)));
                rs = statement.executeQuery();
                while (rs.next()) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle(rb.getString("reminderTitle"));
                    alert.setHeaderText(rb.getString("reminderTitle"));
                    alert.setContentText("you have an upcoming appointment with: "
                            + rs.getString("c.customerName") + " at: "
                            + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(
                                    rs.getTimestamp("a.start")));
                    alert.showAndWait();

                }
                rs.close();
                statement.close();

                Parent calendarViewParent = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
                Scene calendarViewScene = new Scene(calendarViewParent);

                Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

                window.setScene(calendarViewScene);
                window.setTitle("Appointments");
                window.show();
            } else {
                LoginLogger.log(userName, false, currentTimestamp);
                ButtonType ok = new ButtonType(rb.getString("ok"), ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(AlertType.ERROR, rb.getString("error"), ok);
                alert.setTitle(rb.getString("errorTitle"));
                alert.setHeaderText(rb.getString("errorTitle"));
                userText.setText("");
                passText.setText("");
                alert.showAndWait();
                rs.close();
                statement.close();
            }

        }
    }

    public void closeButtonHandler(ActionEvent e) throws IOException {
        ButtonType ok = new ButtonType(rb.getString("ok"), ButtonBar.ButtonData.YES);
        ButtonType cancel = new ButtonType(rb.getString("cancel"), ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                rb.getString("cancelConfirmation"), ok, cancel);
        alert.setHeaderText(rb.getString("exitTitle"));
        alert.setTitle(rb.getString("exitTitle"));
        alert.showAndWait();
        System.out.println(alert.getResult().getButtonData());
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
            Platform.exit();
        }
    }
}
