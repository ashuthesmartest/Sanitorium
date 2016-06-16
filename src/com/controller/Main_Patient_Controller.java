package com.controller;

import com.DBConnect;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * @author 404NotFound
 */


public class Main_Patient_Controller implements Initializable 
{
    private ResultSet rs;
    @FXML
    private Tab docTab;
    @FXML
    private Tab patientTab;
    @FXML
    private Tab userTab;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu userMenu;
    @FXML
    private MenuItem changePassword;
    @FXML
    private TabPane tabPane;
    private int level = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
     /*   createConnection();
        if (level != 1) 
        {
            tabPane.getTabs().remove(2);
        } */
    }

    private void createConnection() 
    {
        try {
            String checkAccount = "SELECT lev FROM app.account where isActive = true";
            rs = DBConnect.st.executeQuery(checkAccount);
            while (rs.next()) 
            {
                level = rs.getInt("lev");
            }
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handlechangePassword() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/view/Dialog/PasswordChangeDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Change Password");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
