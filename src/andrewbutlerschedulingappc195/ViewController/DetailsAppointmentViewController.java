package andrewbutlerschedulingappc195.ViewController;

import Exceptions.AppointmentOverlapException;
import andrewbutlerschedulingappc195.Model.Customer;
import andrewbutlerschedulingappc195.Model.User;
import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import static andrewbutlerschedulingappc195.ViewController.CalendarViewController.selectedAppointment;
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
public class DetailsAppointmentViewController implements Initializable {

    private ArrayList<LocalTime> startTimes = new ArrayList();
    private ArrayList<LocalTime> endTimes = new ArrayList();
    private ArrayList<Customer> customers = new ArrayList();
    private String sql;
    private Customer currentCustomer;
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
        populateCustomerDropdown();
        currentCustomer = getCurrentCustomer();
        customerSelector.getSelectionModel().select(currentCustomer);
        //start and end times based around operating hours of main office and converted to local time.
        populateStartTimes();
        populateEndTimes();
        startTimeSelector.getSelectionModel().select(selectedAppointment.getStart().toLocalDateTime().toLocalTime());
        endTimeSelector.getSelectionModel().select(selectedAppointment.getEnd().toLocalDateTime().toLocalTime());
        startDateSelector.setValue(selectedAppointment.getStart().toLocalDateTime().toLocalDate());
        endDateSelector.setValue(selectedAppointment.getEnd().toLocalDateTime().toLocalDate());

        titleField.setText(selectedAppointment.getTitle());
        typeField.setText(selectedAppointment.getType());
        typeField.setText(selectedAppointment.getType());
        urlField.setText(selectedAppointment.getUrl());
        contactField.setText(selectedAppointment.getContact());
        LocationField.setText(selectedAppointment.getLocation());
        descriptionField.setText(selectedAppointment.getDescription());
        
        customerSelector.setDisable(true);
        startTimeSelector.setDisable(true);
        endTimeSelector.setDisable(true);
        startDateSelector.setDisable(true);
        endDateSelector.setDisable(true);
        titleField.setEditable(false);
        typeField.setEditable(false);
        typeField.setEditable(false);
        urlField.setEditable(false);
        contactField.setEditable(false);
        LocationField.setEditable(false);
        descriptionField.setEditable(false);

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

    public Customer getCurrentCustomer() {
        for (Customer c : customers) {
            if (c.getId() == selectedAppointment.getCustomerId()) {
                return c;
            }
        }
        return new Customer(-1, "error");
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

    public void okButtonHandler(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Parent calendarViewParent = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
            Scene calendarViewScene = new Scene(calendarViewParent);

            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

            window.setScene(calendarViewScene);
            window.setTitle("New Appointments");
            window.show();
        }
    }

}