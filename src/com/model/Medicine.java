/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Ashutosh Agarwal <your.name at your.org>
 */
public class Medicine 
{
    private SimpleStringProperty nameProperty = new SimpleStringProperty();
    private SimpleIntegerProperty quantityProperty = new SimpleIntegerProperty();
    private SimpleIntegerProperty priceProperty = new SimpleIntegerProperty();

    public String getNameProperty() 
    {
        return nameProperty.get();
    }

    public void setNameProperty(String name) 
    {
        this.nameProperty.set(name);
    }

    public int getQuantityProperty() 
    {
        return quantityProperty.get();
    }

    public void setQuantityProperty(int quantity) 
    {
        this.quantityProperty.set(quantity);
    }

    public int getPriceProperty() {
        return priceProperty.get();
    }

    public void setPriceProperty(int price) {
        this.priceProperty.set(price);
    }
}
