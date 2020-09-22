/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.Model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author andrb
 */
public class Appointment {
    private int id;
    private String customer;
    private int customerId;
    private String user;
    private String Title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private Timestamp start;
    private Timestamp end;
    private String startDateString;
    private String endDateString; 
    private String createdBy;
    private Timestamp createdAt;
   

    public Appointment(int id, String customer, int customerId, String user, String Title, String description, String location, String contact, String type, String url, Timestamp start, Timestamp end, Timestamp createdAt, String createdBy) {
        this.id = id;
        this.customer = customer;
        this.customerId = customerId;
        this.user = user;
        this.Title = Title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.startDateString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(start);
        this.endDateString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(end);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getStart() {
        return start;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setStart(Timestamp start) {
        this.start = start;
        this.startDateString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(start);
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
        this.endDateString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(end);
    }

    public String getStartDateString() {
        return startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }
    
}
