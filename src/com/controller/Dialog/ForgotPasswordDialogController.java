package com.controller.Dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ForgotPasswordDialogController implements Initializable 
{
    @FXML
    private TextField tfCode;
    @FXML
    private PasswordField tfNewPass;
    @FXML
    private PasswordField tfConfirm;
    @FXML
    private Button btOk;
    @FXML
    private Button btCancel;
    private String code = CodeSenderController.code;
    private String mail = CodeSenderController.mail;
    private Connection con;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createConnection();

    }

    @FXML
    private void handleBtOk(ActionEvent e) {
        if (tfCode.getText() == null || tfConfirm.getText() == null || tfNewPass.getText() == null) {
            Notifications.create().title("Error")
                    .text("All field must be filled")
                    .showError();
        } else if (!this.code.equals(tfCode.getText())) {
            Notifications.create().title("Error")
                    .text("Your code doesn't match")
                    .showError();
        } else if (!tfNewPass.getText().equals(tfConfirm.getText())) {
            Notifications.create().title("Error")
                    .text("Two password doesn't match")
                    .showError();
        } else {
            String updateAcc = "UPDATE APP_Account SET Password = ? where email = ?";
            try {
                ps = con.prepareStatement(updateAcc);
                ps.setString(1, DigestUtils.md5Hex(tfNewPass.getText()));
                ps.setString(2, mail);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
            }
        }
        Notifications.create().title("Error")
                .text("Your code doesn't match")
                .showError();
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void handleBtCancel(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

    private void createConnection() 
    {

    }
}
