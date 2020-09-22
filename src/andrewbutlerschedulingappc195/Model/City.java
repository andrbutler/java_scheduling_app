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
public class City {
    private int id;
    private String name;
    private Country country;
    private Timestamp createdAt;
    private String createdBy;

    public City(int id, String name, Country country, Timestamp createdAt, String createdBy) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getCountryId() {
        return country.getId();
    }
    
    public String getCountryName() {
        return country.getName();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
}
