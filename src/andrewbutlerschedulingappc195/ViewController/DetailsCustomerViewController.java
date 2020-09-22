/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.ViewController;

import Exceptions.InvalidCustomerInformationException;
import andrewbutlerschedulingappc195.Model.Address;
import andrewbutlerschedulingappc195.Model.City;
import andrewbutlerschedulingappc195.Model.Country;
import andrewbutlerschedulingappc195.Model.Customer;
import andrewbutlerschedulingappc195.Model.User;
import andrewbutlerschedulingappc195.Utils.DBConnection;
import andrewbutlerschedulingappc195.Utils.DBQuery;
import static andrewbutlerschedulingappc195.ViewController.CustomerViewController.selectedCustomer;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author andrb
 */
public class DetailsCustomerViewController implements Initializable {

    private City currentCity;
    private int customerId;
    private int addressId;
    private int cityId;
    private Address currentAddress;
    @FXML
    private ToggleGroup newCityToggleGroup;
    @FXML
    private TextField nameField;
    @FXML
    private CheckBox activeCheckBox;
    @FXML
    private TextField addressField;
    @FXML
    private TextField addressField2;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField countryField;
    @FXML
    private RadioButton newCityRadioButton;
    @FXML
    private TextField newCityField;


    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerId = selectedCustomer.getId();
        addressId = selectedCustomer.getAddressId();
        this.activeCheckBox.setSelected(selectedCustomer.isActive());
        fetchAddress();
        cityId = currentAddress.getCityId();
        fetchCity();
        addressField.setText(currentAddress.getAddress());
        addressField.setEditable(false);
        addressField2.setText(currentAddress.getAddress2());
        addressField2.setEditable(false);
        phoneField.setText(currentAddress.getPhone());
        phoneField.setEditable(false);
        postalCodeField.setText(currentAddress.getPostalCode());
        postalCodeField.setEditable(false);
        nameField.setText(selectedCustomer.getName());
        nameField.setEditable(false);
        countryField.setText(currentCity.getCountryName());
        countryField.setEditable(false);
        cityField.setText(currentCity.getName());
        cityField.setEditable(false);
        activeCheckBox.setDisable(true);
        
        
    }

    @FXML
    public void okButtonHandler(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Parent customerViewParent = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
            Scene customerViewScene = new Scene(customerViewParent);

            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();

            window.setScene(customerViewScene);
            window.setTitle("Customers");
            window.show();
        }
    }

    private void fetchCity() {
        try {
            String sql = "select * from city ci inner join country co on ci.countryId = co.countryId where cityId = ?";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            statement.setInt(1, cityId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                currentCity = new City(rs.getInt("ci.cityId"), rs.getString("ci.city"),
                        new Country(rs.getInt("co.countryId"), rs.getString("co.country")),
                        rs.getTimestamp("ci.createDate"), rs.getString("ci.createdBy"));

            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void fetchAddress() {
        try {
            String sql = "select * from address where addressId = ?";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            statement.setInt(1, addressId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                currentAddress = new Address(rs.getInt("addressId"), rs.getString("address"),
                        rs.getString("address2"), rs.getInt("cityId"), rs.getString("postalCode"),
                        rs.getString("phone"), rs.getTimestamp("createDate"), rs.getString("createdBy"));

            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
           System.out.println(ex.getMessage());
        }
    }

}
