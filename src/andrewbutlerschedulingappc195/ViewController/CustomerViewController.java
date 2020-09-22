/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.ViewController;

import andrewbutlerschedulingappc195.Model.Customer;
import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrb
 */
public class CustomerViewController implements Initializable {
    private ArrayList customers = new ArrayList();
    public static Customer selectedCustomer;
    @FXML
    private TableView<Customer> customersTable;
    @FXML
    private TableColumn<Customer, String> activeColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button detailsButton;
    @FXML
    private Button cancelButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedCustomer = null;
        fetchCustomers();
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("activeString"));
        customersTable.setItems(FXCollections.observableArrayList(customers));
    }    

    @FXML
    public void addButtonHandler(Event e) throws IOException {
        Parent addViewParent = FXMLLoader.load(getClass().getResource("AddCustomerView.fxml"));
        Scene calendarViewScene = new Scene(addViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(calendarViewScene);
        window.setTitle("New Customer");
        window.show();
    }


    @FXML
    public void editButtonHandler(Event e) throws IOException {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        Parent updateViewParent = FXMLLoader.load(getClass().getResource("EditCustomerView.fxml"));
        Scene updateViewScene = new Scene(updateViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(updateViewScene);
        window.setTitle("Edit Customer");
        window.show();
    }

    @FXML
    public void detailsButtonHandler(Event e) throws IOException {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        Parent detailsViewParent = FXMLLoader.load(getClass().getResource("DetailsCustomerView.fxml"));
        Scene detailsViewScene = new Scene(detailsViewParent);

        Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

        window.setScene(detailsViewScene);
         window.setTitle("Customer Details");
        window.show();
    }


    @FXML
     public void deleteButtonHandler(ActionEvent e) throws SQLException, IOException {
        selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        deleteCustomer(selectedCustomer.getId());
        customers.remove(selectedCustomer);
        selectedCustomer = null;
        customersTable.setItems(FXCollections.observableArrayList(customers));
    }

    @FXML
    public void cancelButtonHandler(ActionEvent e) throws IOException {
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
    
        private void fetchCustomers() {
        try {
            String sql = "select * from customer";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();
            boolean active;
            while (rs.next()) {
                if(rs.getInt("active") == 0){
                    active = false;
                } else {
                    active = true;
                }
                customers.add(new Customer(rs.getInt("customerId"),
                        rs.getString("customerName"), rs.getInt("addressId"), 
                        active, rs.getTimestamp("createDate"), rs.getString("createdBy")));

            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
        private void deleteCustomer(int id) throws SQLException {
        //delete all appointments associated with customer
        String sql = "delete from appointment where customerId = ?";
        DBQuery.setSql(sql);
        PreparedStatement statement = DBQuery.getPs();
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
        
        //delete customer and address
        sql = "delete customer, address from customer left join address using(addressId) where customerId = ?";
        DBQuery.setSql(sql);
        statement = DBQuery.getPs();
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
        
    }
    
}
