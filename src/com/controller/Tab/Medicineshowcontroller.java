/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.Tab;

import com.DBConnect;
import com.model.Medicine;
import com.model.Patient;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.dialog.Dialogs;

/**
 *
 * @author Ashutosh Agarwal <your.name at your.org>
 */
public class Medicineshowcontroller implements Initializable
{
    private Connection con;
    private PreparedStatement ps;

    @FXML
    private TableView tv1;
    
    @FXML
    private TableColumn c1;
    
    @FXML
    private TableColumn c2;
    
    @FXML
    private TableColumn c3;
    
    @FXML
    private Button btAdd;
    
    @FXML
    private TextField tfMedicine;
    
    @FXML
    private TextField tfQuantity;
    
    @FXML
    private TextField tfPrice;
    
    private ObservableList<Medicine> Medicinedata = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        initTable();
        reloadTable();
    }
    
    private void initTable() 
    {
        c1.setCellValueFactory(
                new PropertyValueFactory<>("nameProperty")
        );
        c2.setCellValueFactory(
                new PropertyValueFactory<>("quantityProperty")
        );
        c3.setCellValueFactory(
                new PropertyValueFactory<>("priceProperty")
        );
        
    }    
        
        private void reloadTable() 
        {
        try {
            Medicinedata.clear();
            String sql = "SELECT * FROM medicine " ;  
            ResultSet rs = DBConnect.st.executeQuery(sql);
            while (rs.next()) 
            {
                Medicine p = new Medicine();
                p.setNameProperty(rs.getString(1));
                p.setQuantityProperty(rs.getInt(2));
                p.setPriceProperty(rs.getInt(3));
                Medicinedata.add(p);
            }
            tv1.setItems(Medicinedata);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(PatientTabController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
        
    @FXML
    public void handleAddButton() 
    {
        if (tfMedicine.getText().equals("") || tfQuantity.getText().equals("") || tfPrice.getText().equals("")) {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } else if (isDuplicate(tfMedicine.getText())) {
            Notifications.create().title("Add Error")
                    .text("Duplicate PatientID")
                    .showError();
        } else {
            String name = tfMedicine.getText();
            int quantity = Integer.valueOf(tfQuantity.getText());
            int price = Integer.valueOf(tfPrice.getText());

            Medicine medicine = new Medicine();
            medicine.setNameProperty(name);
            medicine.setQuantityProperty(quantity);
            medicine.setPriceProperty(price);
            Medicinedata.add(medicine);
            String add = "INSERT INTO medicine (name, quantity, price)"+ "VALUES ('"+name+"',"+quantity+","+price+")" ;
            try 
            {
                DBConnect.st.executeUpdate(add);
            } 
            catch (SQLException ex) {
                Dialogs.create()
                        .owner(medicine)
                        .title("Exception")
                        .masthead("There is an exception")
                        .message("Ooops, there was an exception!")
                        .showException(ex);
            }
            reloadTable();
            tfMedicine.clear();
            tfQuantity.clear();
            tfPrice.clear();
        }
    }
    public boolean isDuplicate(String s) 
    {
        for (Medicine medicine : Medicinedata) 
        {
            if (s.equals(medicine.getNameProperty())) 
            {
                return true;
            }
        }
        return false;
    }


       
      
    
    
}
