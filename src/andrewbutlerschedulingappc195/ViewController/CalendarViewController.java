/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.ViewController;

import andrewbutlerschedulingappc195.Model.Appointment;
import andrewbutlerschedulingappc195.Model.User;
import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrb
 */
public class CalendarViewController implements Initializable {

    public static Appointment selectedAppointment;
    private ArrayList<Appointment> appointments = new ArrayList();
    private Timestamp currentTimestamp;
    private String sql;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> startColumn;
    @FXML
    private TableColumn<Appointment, String> endColumn;
    @FXML
    private TableColumn<Appointment, String> customerColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    private ToggleGroup appointmentToggleGroup;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button detailsButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button exitButton;
    @FXML
    private RadioButton allRadioButton;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private RadioButton monthRadioButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        fetchAppointments();
        appointmentToggleGroup = new ToggleGroup();
        this.allRadioButton.setToggleGroup(appointmentToggleGroup);
        this.weekRadioButton.setToggleGroup(appointmentToggleGroup);
        this.monthRadioButton.setToggleGroup(appointmentToggleGroup);
        this.allRadioButton.setSelected(true);
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDateString"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endDateString"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        appointmentsTable.setItems(FXCollections.observableArrayList(appointments));

    }

    private void fetchAppointments() {
        try {
            selectedAppointment = null;
            currentTimestamp = new Timestamp(System.currentTimeMillis());
            sql = "select * from appointment a inner join customer c on a.customerId = c.customerId where a.userId = ? and a.end >= ?";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            statement.setInt(1, User.getUserId());
            statement.setTimestamp(2, currentTimestamp);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                appointments.add(new Appointment(rs.getInt("appointmentId"),
                        rs.getString("customerName"), rs.getInt("customerId"), User.getUserName(), rs.getString("title"),
                        rs.getString("description"), rs.getString("location"), rs.getString("contact"),
                        rs.getString("type"), rs.getString("url"), rs.getTimestamp("start"),
                        rs.getTimestamp("end"), rs.getTimestamp("createDate"), rs.getString("createdBy")));

            }
            rs.close();
            statement.close();
            // TODO
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @FXML
    public void deleteButtonHandler(ActionEvent e) throws SQLException, IOException {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        deleteAppointment(selectedAppointment.getId());
        appointments.remove(selectedAppointment);
        selectedAppointment = null;
        //appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        filterAppointments(e);
    }

    @FXML
    public void addButtonHandler(Event e) throws IOException {
        Parent addViewParent = FXMLLoader.load(getClass().getResource("AddAppointmentView.fxml"));
        Scene calendarViewScene = new Scene(addViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(calendarViewScene);
        window.setTitle("New Appointment");
        window.show();
    }

    @FXML
    public void updateButtonHandler(Event e) throws IOException {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        Parent updateViewParent = FXMLLoader.load(getClass().getResource("UpdateAppointmentView.fxml"));
        Scene updateViewScene = new Scene(updateViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(updateViewScene);
        window.setTitle("Update Appointment");
        window.show();
    }

    @FXML
    public void detailsButtonHandler(Event e) throws IOException {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        Parent detailsViewParent = FXMLLoader.load(getClass().getResource("DetailsAppointmentView.fxml"));
        Scene detailsViewScene = new Scene(detailsViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(detailsViewScene);
        window.setTitle("Appointment Details");
        window.show();
    }

    @FXML
    public void reportButtonHandler(Event e) throws IOException {
        Parent reportViewParent = FXMLLoader.load(getClass().getResource("ReportView.fxml"));
        Scene reportViewScene = new Scene(reportViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(reportViewScene);
        window.setTitle("Reports");
        window.show();
    }

    @FXML
    public void customerButtonHandler(Event e) throws IOException {
        selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
        Parent customerViewParent = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
        Scene customerViewScene = new Scene(customerViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(customerViewScene);
        window.setTitle("Customers");
        window.show();
    }

    @FXML
    public void exitButtonHandler(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

    public void deleteAppointment(int id) throws SQLException {
        String sql = "delete from appointment where appointmentId = ?";
        DBQuery.setSql(sql);
        PreparedStatement statement = DBQuery.getPs();
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
    }

    @FXML
    //lambda expressions used to filter appointments
    private void filterAppointments(ActionEvent event) {
        Stream<Appointment> appointmentsStream = appointments.stream();
        if (this.appointmentToggleGroup.getSelectedToggle().equals(this.allRadioButton)) {
            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        } else if (this.appointmentToggleGroup.getSelectedToggle().equals(this.monthRadioButton)) {
            appointmentsTable.setItems(FXCollections.observableArrayList(new ArrayList(appointmentsStream.
                    filter(a -> a.getEnd().toLocalDateTime().isAfter(currentTimestamp.toLocalDateTime())
                    && a.getStart().toLocalDateTime().isBefore(currentTimestamp.toLocalDateTime().plusMonths(1)))
                    .collect(Collectors.toList()))));
        } else if (this.appointmentToggleGroup.getSelectedToggle().equals(this.weekRadioButton)) {
            appointmentsTable.setItems(FXCollections.observableArrayList(new ArrayList(appointmentsStream.
                    filter(a -> a.getEnd().toLocalDateTime().isAfter(currentTimestamp.toLocalDateTime())
                    && a.getStart().toLocalDateTime().isBefore(currentTimestamp.toLocalDateTime().plusDays(7)))
                    .collect(Collectors.toList()))));
        }
    }
}
