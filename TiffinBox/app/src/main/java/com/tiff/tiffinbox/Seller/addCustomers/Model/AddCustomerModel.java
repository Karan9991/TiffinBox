package com.tiff.tiffinbox.Seller.addCustomers.Model;

import java.io.Serializable;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class AddCustomerModel implements Serializable {
    public String id;
    public String name;
    public String email;
    public String mobile;
    public String address;

    public AddCustomerModel() {
    }
    public AddCustomerModel(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public AddCustomerModel(String id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public AddCustomerModel(String id, String name, String email, String mobile, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
