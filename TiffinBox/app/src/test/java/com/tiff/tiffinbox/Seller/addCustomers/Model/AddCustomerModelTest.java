package com.tiff.tiffinbox.Seller.addCustomers.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddCustomerModelTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getName() {
        AddCustomerModel addCustomerModel = new AddCustomerModel();
        addCustomerModel.setName("Karan");
        assertNull(addCustomerModel.getAddress());
    }

    @Test
    void setName() {
    }
}