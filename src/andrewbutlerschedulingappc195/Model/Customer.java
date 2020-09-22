/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andrewbutlerschedulingappc195.Model;

import java.sql.Timestamp;

/**
 *
 * @author andrb
 */
public class Customer {

    private int id;
    private String name;
    private int addressId;
    private boolean active;
    private String activeString;
    private Timestamp createdAt;
    private String createdBy;

    public Customer(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public Customer(int id, String name, int addressId, boolean active, Timestamp createdAt, String createdBy) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.active = active;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        if (active) {
            this.activeString = "active";
        } else {
            this.activeString = "inactive";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            this.activeString = "active";
        } else {
            this.activeString = "inactive";
        }
    }

    public String getActiveString() {
        return activeString;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

}
