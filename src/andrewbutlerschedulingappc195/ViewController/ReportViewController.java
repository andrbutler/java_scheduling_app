/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.ViewController;

import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrb
 */
public class ReportViewController implements Initializable {

    @FXML
    private TextArea reportTextArea;
    @FXML
    private Button appointmentReportButton;
    @FXML
    private Button scheduleReportButton;
    @FXML
    private Button customersReportButton;
    @FXML
    private Button exitButton;
    private String report;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportTextArea.setEditable(false);
    }

    @FXML
    private void appointmentReportHandler(ActionEvent event) {
        report = "";
        try {
            String sql = "select date_format(start, \"%M-%Y\") month, count(*) number from appointment group by month";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();
            boolean active;
            while (rs.next()) {
                report += rs.getString("month") + "\t" + rs.getInt("number") + "\n";
            }
            rs.close();
            statement.close();
            reportTextArea.setText(report);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void scheduleReportHandler(ActionEvent event) {
        report = "";
        String currentUser = "";
        try {
            String sql = "select u.userName, a.title, a.start, a.end, c.customerName from "
                    + "user u inner join appointment a on u.userId = a.userId "
                    + "inner join customer c on a.customerId = c.customerId";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();
            boolean active;
            while (rs.next()) {
                if (!(rs.getString("u.userName").equals(currentUser))) {
                    currentUser = rs.getString("u.userName");
                    report += currentUser + "\n";
                }
                report += "\t\t" + rs.getString("a.title") + " \t"
                        + rs.getString("c.customerName") + " \t"
                        + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(rs.getTimestamp("start"))
                        + " to "
                        + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(rs.getTimestamp("end"))
                        + "\n";

            }
            rs.close();
            statement.close();
            reportTextArea.setText(report);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void customersReportHandler(ActionEvent event) {
        report = "";
        String currentCity = "";
        try {
            String sql = "select cu.customerName, ci.city, co.country from "
                    + "customer cu inner join address a on cu.addressId = a.addressId "
                    + "inner join city ci on a.cityId = ci.cityId "
                    + "inner join country co on ci.countryId = co.countryId";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();
            boolean active;
            while (rs.next()) {
                if (!((rs.getString("ci.city") + ", " + rs.getString("co.country")).equals(currentCity))) {
                    
                    currentCity = rs.getString("ci.city") + ", " + rs.getString("co.country");
                    report += currentCity + "\n";
                }
                report += "\t\t" + rs.getString("cu.CustomerName") + " \n";

            }
            rs.close();
            statement.close();
            reportTextArea.setText(report);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void exitButtonHandler(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Parent calendarViewParent = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
            Scene calendarViewScene = new Scene(calendarViewParent);

            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

            window.setScene(calendarViewScene);
            window.setTitle("Appointments");
            window.show();
        }
    }

}
