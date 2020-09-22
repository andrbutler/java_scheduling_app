/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.ViewController;

import Exceptions.AppointmentOverlapException;
import andrewbutlerschedulingappc195.Model.Customer;
import andrewbutlerschedulingappc195.Model.User;
import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author andrb
 */
public class AddAppointmentViewController implements Initializable {

    private ArrayList<LocalTime> startTimes = new ArrayList();
    private ArrayList<LocalTime> endTimes = new ArrayList();
    private ArrayList<Customer> customers = new ArrayList();
    private String sql;
    private int appointmentId;
    @FXML
    private Button okButtonHandler;
    @FXML
    private Button cancelButtonHandler;
    @FXML
    private TextField titleField;
    @FXML
    private DatePicker startDateSelector;
    @FXML
    private ChoiceBox<LocalTime> startTimeSelector;
    @FXML
    private DatePicker endDateSelector;
    @FXML
    private ChoiceBox<LocalTime> endTimeSelector;
    @FXML
    private ComboBox<Customer> customerSelector;
    @FXML
    private TextField typeField;
    @FXML
    private TextField urlField;
    @FXML
    private TextArea contactField;
    @FXML
    private TextArea LocationField;
    @FXML
    private TextArea descriptionField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentId = 1;
        fetchMaxAppointmentId();
        populateCustomerDropdown();
        //start and end times based around operating hours of main office and converted to local time.
        populateStartTimes();
        populateEndTimes();
        startTimeSelector.getSelectionModel().selectFirst();
        endTimeSelector.getSelectionModel().selectFirst();
        startDateSelector.setValue(LocalDate.now());
        endDateSelector.setValue(LocalDate.now());
    }

    public void fetchCustomers() {
        try {
            String sql = "select * from customer";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                customers.add(new Customer(rs.getInt("customerId"),
                        rs.getString("customerName")));

            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void populateCustomerDropdown() {
        fetchCustomers();
        customerSelector.setItems(FXCollections.observableArrayList(customers));
        customerSelector.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer object) {
                return object.getName();
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }

        });
    }

    public void generateStartTimes() {
        for (int x = 9; x < 17; x++) {
            OffsetTime t = OffsetTime.of(x, 0, 0, 0, ZoneOffset.of("-04:00"));
            LocalTime l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            startTimes.add(l);
            t = OffsetTime.of(x, 15, 0, 0, ZoneOffset.of("-04:00"));
            l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            startTimes.add(l);
            t = OffsetTime.of(x, 30, 0, 0, ZoneOffset.of("-04:00"));
            l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            startTimes.add(l);
            t = OffsetTime.of(x, 45, 0, 0, ZoneOffset.of("-04:00"));
            l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            startTimes.add(l);
        }
    }

    public void populateStartTimes() {
        generateStartTimes();
        startTimeSelector.setItems(FXCollections.observableArrayList(startTimes));
    }

    public void generateEndTimes() {
        for (int x = 9; x < 17; x++) {
            OffsetTime t = OffsetTime.of(x, 15, 0, 0, ZoneOffset.of("-04:00"));
            LocalTime l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            endTimes.add(l);
            t = OffsetTime.of(x, 30, 0, 0, ZoneOffset.of("-04:00"));
            l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            endTimes.add(l);
            t = OffsetTime.of(x, 45, 0, 0, ZoneOffset.of("-04:00"));
            l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            endTimes.add(l);
            t = OffsetTime.of(x + 1, 0, 0, 0, ZoneOffset.of("-04:00"));
            l = t.withOffsetSameInstant(OffsetTime.now().getOffset()).toLocalTime();
            endTimes.add(l);
        }
    }

    public void populateEndTimes() {
        generateEndTimes();
        endTimeSelector.setItems(FXCollections.observableArrayList(endTimes));
    }

    public void fetchMaxAppointmentId() {
        try {
            String sql = "select MAX(appointmentId) m from appointment";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                appointmentId = rs.getInt("m") + 1;

            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    @FXML
    public void cancelButtonHandler(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?", ButtonType.YES, ButtonType.NO);
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
    @FXML
    public void okButtonHandler(ActionEvent e) throws SQLException, IOException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        int customerId;
        LocalDateTime startDateTime = LocalDateTime.of(startDateSelector.getValue(),
                startTimeSelector.getSelectionModel().getSelectedItem());
        LocalDateTime endDateTime = LocalDateTime.of(endDateSelector.getValue(),
                endTimeSelector.getSelectionModel().getSelectedItem());
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);
        if (startTimestamp.before(currentTimestamp)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: appointment cannot begin before current time.");
            alert.showAndWait();

        } else if (startTimestamp.after(endTimestamp)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: appointment end time before appointment start time.");
            alert.showAndWait();

        } else if (customerSelector.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error: No customer selected.");
            alert.showAndWait();

        } else {
            customerId = customerSelector.getSelectionModel().getSelectedItem().getId();
            String type = typeField.getText();
            String title = titleField.getText();
            String url = typeField.getText();
            String location = typeField.getText();
            String contact = contactField.getText();
            String description = typeField.getText();
            try {
                DBQuery.setConn(DBConnection.getConn());

                sql = "select u.userId, a.start, a.end, c.customerName from user u inner join appointment a on u.userId = a.userId inner join customer c on a.customerId = c.customerId where u.userId = ? and ((a.start >= ? and a.start < ?) or (a.end <= ? and a.end > ?))";
                DBQuery.setSql(sql);
                PreparedStatement statement = DBQuery.getPs();
                statement.setInt(1, User.getUserId());
                statement.setTimestamp(2, startTimestamp);
                statement.setTimestamp(3, endTimestamp);
                statement.setTimestamp(4, endTimestamp);
                statement.setTimestamp(5, startTimestamp);
                ResultSet rs = statement.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    statement.close();
                    sql = "insert into appointment "
                            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    DBQuery.setSql(sql);
                    statement = DBQuery.getPs();
                    statement.setInt(1, appointmentId);
                    statement.setInt(2, customerId);
                    statement.setInt(3, User.getUserId());
                    statement.setString(4, title);
                    statement.setString(5, description);
                    statement.setString(6, location);
                    statement.setString(7, contact);
                    statement.setString(8, type);
                    statement.setString(9, url);
                    statement.setTimestamp(10, startTimestamp);
                    statement.setTimestamp(11, endTimestamp);
                    statement.setTimestamp(12, currentTimestamp);
                    statement.setString(13, User.getUserName());
                    statement.setTimestamp(14, currentTimestamp);
                    statement.setString(15, User.getUserName());
                    statement.execute();
                    rs.close();
                    statement.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("New appointment created sucessfully!");
                    alert.showAndWait();
                    Parent calendarViewParent = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
                    Scene calendarViewScene = new Scene(calendarViewParent);

                    Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

                    window.setScene(calendarViewScene);
                     window.setTitle("Appointments");
                    window.show();
                } else {
                    String errorMessage = "Error: Appointment overlaps existing with: "
                            + rs.getString("c.customerName") + " from: "
                            + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(
                                    rs.getTimestamp("a.start")) + " to "
                            + new SimpleDateFormat("MM/dd/yyyy HH:mm").format(
                                    rs.getTimestamp("a.end"));
                    rs.close();
                    statement.close();
                    throw new AppointmentOverlapException(errorMessage);

                }
            } catch (AppointmentOverlapException a) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(a.getMessage());
                alert.showAndWait();

            }
        }
    }
}
