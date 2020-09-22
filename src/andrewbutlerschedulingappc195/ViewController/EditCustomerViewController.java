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
import java.util.stream.Collectors;
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
public class EditCustomerViewController implements Initializable {

    private ArrayList<City> cities = new ArrayList();
    private ArrayList<Country> countries = new ArrayList();
    private boolean newCity;
    private City currentCity;
    private int customerId;
    private int cityId;
    private int maxCityId;
    private int addressId;
    private boolean active;
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
    private RadioButton newCityRadioButton;
    @FXML
    private TextField newCityField;
    @FXML
    private ComboBox<Country> countrySelector;
    @FXML
    private RadioButton selectCityRadioButton;
    @FXML
    private ComboBox<City> citySelector;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newCityToggleGroup = new ToggleGroup();
        this.newCityRadioButton.setToggleGroup(newCityToggleGroup);
        this.selectCityRadioButton.setToggleGroup(newCityToggleGroup);
        this.newCityToggleGroup.selectToggle(selectCityRadioButton);
        newCity = false;
        active = selectedCustomer.isActive();
        customerId = selectedCustomer.getId();
        addressId = selectedCustomer.getAddressId();
        this.activeCheckBox.setSelected(selectedCustomer.isActive());
        newCityField.setDisable(true);
        countrySelector.setDisable(true);
        citySelector.setDisable(false);
        fetchMaxCityId();
        fetchAddress();
        cityId = currentAddress.getCityId();
        populateCityDropdown();
        populateCountryDropdown();
        countrySelector.getSelectionModel().selectFirst();
        citySelector.getSelectionModel().select(currentCity);
        addressField.setText(currentAddress.getAddress());
        addressField2.setText(currentAddress.getAddress2());
        phoneField.setText(currentAddress.getPhone());
        postalCodeField.setText(currentAddress.getPostalCode());
        nameField.setText(selectedCustomer.getName());
    }

    @FXML
    public void selectCityButtonHandler(ActionEvent event) {
        if (this.newCityToggleGroup.getSelectedToggle().equals(this.newCityRadioButton)) {
            newCity = true;
            newCityField.setDisable(false);
            countrySelector.setDisable(false);
            citySelector.setDisable(true);
        } else if (this.newCityToggleGroup.getSelectedToggle().equals(this.selectCityRadioButton)) {
            newCity = false;
            newCityField.setDisable(true);
            countrySelector.setDisable(true);
            citySelector.setDisable(false);
        }
    }

    @FXML
    public void cancelButtonHandler(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?", ButtonType.YES, ButtonType.NO);
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

    private void fetchCities() {
        try {
            String sql = "select * from city ci inner join country co on ci.countryId = co.countryId";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                cities.add(new City(rs.getInt("ci.cityId"), rs.getString("ci.city"),
                        new Country(rs.getInt("co.countryId"), rs.getString("co.country")),
                        rs.getTimestamp("ci.createDate"), rs.getString("ci.createdBy")));
                if (rs.getInt("ci.cityId") == cityId) {
                    currentCity = cities.get(cities.size() - 1);
                }
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void populateCityDropdown() {
        fetchCities();
        //lambda to group cities in dropdown by country
        cities.sort((a,b) -> a.getCountryName().compareTo(b.getCountryName()));
        citySelector.setItems(FXCollections.observableArrayList(cities));
        citySelector.setConverter(new StringConverter<City>() {
            @Override
            public String toString(City object) {
                return (object.getName() + ", " + object.getCountryName());
            }

            @Override
            public City fromString(String string) {
                return null;
            }
        });
    }

    private void fetchCountries() {
        try {
            String sql = "select * from country";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                countries.add(new Country(rs.getInt("countryId"),
                        rs.getString("country")));

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

    private void populateCountryDropdown() {
        fetchCountries();
        countrySelector.setItems(FXCollections.observableArrayList(countries));
        countrySelector.setConverter(new StringConverter<Country>() {
            @Override
            public String toString(Country object) {
                return object.getName();
            }

            @Override
            public Country fromString(String string) {
                return null;
            }
        });
    }

    private void fetchMaxCityId() {
        try {
            String sql = "select MAX(cityId) m from city";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                maxCityId = rs.getInt("m") + 1;

            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void validateCustomer(String name, String address1, String address2,
            String postalCode, String phoneNumber, String cityName,
            int countryId) throws InvalidCustomerInformationException {
        String error;
        //check if customer name is less than 45 characters
        if (name.length() > 45) {
            error = "error: customer name must be less than 45 characters in length.";
            throw new InvalidCustomerInformationException(error);
        }
        //check for blank customer name
        if (name.isEmpty()) {
            error = "error: customer name cannot be blank.";
            throw new InvalidCustomerInformationException(error);
        }
        //check if address fields are less than 50 characters
        if ((address1.length() > 50) || (address2.length() > 50)) {
            error = "error: address fields must be less than 50 characters in length.";
            throw new InvalidCustomerInformationException(error);
        }
        //check for blank address field
        if (address1.isEmpty()) {
            error = "error: address1 cannot be blank.";
            throw new InvalidCustomerInformationException(error);
        }
        //check if postal code field is less than 10 characters
        if (postalCode.length() > 10) {
            error = "error: postal code must be less than 10 characters in length.";
            throw new InvalidCustomerInformationException(error);
        }
        //check for blank postal code field or invalid characters
        if ((postalCode.isEmpty()) || (postalCode.matches("\\D+"))) {
            error = "error: postal code cannot be blank, and may only contain numbers 0-9";
            throw new InvalidCustomerInformationException(error);
        }
        //check if phone number field is less than 20 characters
        if (phoneNumber.length() > 10) {
            error = "error: phone number must be less than 20 characters in length.";
            throw new InvalidCustomerInformationException(error);
        }
        //check for blank phone number field or invalid characters
        if ((phoneNumber.isEmpty()) || (phoneNumber.matches("[a-zA-Z]+"))) {
            error = "error: phone number cannot be blank, and may not contain letters";
            throw new InvalidCustomerInformationException(error);
        }
        // if new city is being added, check for already existing record
        if (newCity) {
            for (City c : cities) {
                if ((c.getCountryId() == countryId) && (c.getName().equals(cityName))) {
                    error = "error: city already exists in database.";
                    throw new InvalidCustomerInformationException(error);
                }
            }
        }
    }

    @FXML
    public void okButtonHandler(ActionEvent event) throws SQLException, IOException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        String name;
        String address1;
        String address2;
        String postalCode;
        String phoneNumber;
        String cityName;
        int activeInt;
        int countryId;
        cityId = 1;
        if (newCity) {
            cityId = maxCityId;
            cityName = newCityField.getText();
            countryId = countrySelector.getSelectionModel().getSelectedItem().getId();
        } else {
            cityId = citySelector.getSelectionModel().getSelectedItem().getId();
            cityName = citySelector.getSelectionModel().getSelectedItem().getName();
            countryId = citySelector.getSelectionModel().getSelectedItem().getCountryId();
        }
        name = nameField.getText();
        address1 = addressField.getText();
        address2 = addressField2.getText();
        postalCode = postalCodeField.getText();
        phoneNumber = phoneField.getText();
        activeInt = activeCheckBox.isSelected() ? 1 : 0;

        try {
            validateCustomer(name, address1, address2, postalCode, phoneNumber,
                    cityName, countryId);
            String sql = "select * from customer";
            DBQuery.setConn(DBConnection.getConn());
            DBQuery.setSql(sql);
            PreparedStatement statement = DBQuery.getPs();
            statement.close();
            if (newCity) {

                sql = "insert into city values(?,?,?,?,?,?,?)";
                DBQuery.setConn(DBConnection.getConn());
                DBQuery.setSql(sql);
                statement = DBQuery.getPs();
                statement.setInt(1, cityId);
                statement.setString(2, cityName);
                statement.setInt(3, countryId);
                statement.setTimestamp(4, currentTimestamp);
                statement.setString(5, User.getUserName());
                statement.setTimestamp(6, currentTimestamp);
                statement.setString(7, User.getUserName());
                statement.execute();
                statement.close();

            }
            sql = "update address set address = ? , address2 = ? , "
                    + "cityId = ?, postalCode = ?, phone = ?, lastUpdate = ?, "
                    + "lastUpdateBy = ? where addressId = ?";
            DBQuery.setSql(sql);
            statement = DBQuery.getPs();

            statement.setString(1, address1);
            statement.setString(2, address2);
            statement.setInt(3, cityId);
            statement.setString(4, postalCode);
            statement.setString(5, phoneNumber);
            statement.setTimestamp(6, currentTimestamp);
            statement.setString(7, User.getUserName());
            statement.setInt(8, addressId);
            statement.execute();
            statement.close();

            sql = "update customer set customerName = ?, "
                    + "active = ?, lastUpdate = ?, "
                    + "lastUpdateBy = ? where customerId = ?";
            DBQuery.setSql(sql);
            statement = DBQuery.getPs();
            statement.setString(1, name);
            statement.setInt(2, activeInt);
            statement.setTimestamp(3, currentTimestamp);
            statement.setString(4, User.getUserName());
            statement.setInt(5, customerId);
            statement.execute();
            statement.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Customer updated sucessfully!");
            alert.showAndWait();
            Parent calendarViewParent = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
            Scene calendarViewScene = new Scene(calendarViewParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(calendarViewScene);
            window.setTitle("Customers");
            window.show();
        } catch (InvalidCustomerInformationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }
    }
}
