package com.model;


import javafx.beans.property.SimpleStringProperty;

public class Doctor {
    private SimpleStringProperty idProperty = new SimpleStringProperty();
    private SimpleStringProperty nameProperty = new SimpleStringProperty();
    private SimpleStringProperty departmentProperty = new SimpleStringProperty();

    public String getIdProperty() {
        return idProperty.get();
    }

    public void setIdProperty(String s) {
        this.idProperty.setValue(s);
    }

    public String getNameProperty() {
        return nameProperty.get();
    }

    public void setNameProperty(String s) {
        this.nameProperty.setValue(s);
    }

    public String getDepartmentProperty() {
        return departmentProperty.get();
    }

    public void setDepartmentProperty(String s) {
        this.departmentProperty.setValue(s);
    }
}