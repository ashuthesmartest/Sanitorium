package com.controller.Dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.apache.commons.codec.digest.DigestUtils;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PasswordChangeDialogController implements Initializable {

    private final String driver = "oracle.jdbc.driver.OracleDriver";
    private ResultSet rs;
    private Connection con;
    private PreparedStatement ps;
    private Statement st;
    @FXML
    private PasswordField tfOldPass;
    @FXML
    private PasswordField tfNewPass;
    @FXML
    private PasswordField tfRepeat;
    @FXML
    private Button btOk;
    @FXML
    private Button btCancel;
    private String id;
    private String oldPass;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createConnect();
    }

    private void createConnect() {
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ashuthesmartest", "ashutosha");
            String sql = "SELECT id, password FROM APP_Account where isActive = true";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                id = rs.getString("ID");
                oldPass = rs.getString("password");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleOkBt(ActionEvent e) {
        if (tfOldPass.getText().equals("")
                || tfNewPass.getText().equals("")
                || tfRepeat.getText().equals("")) {
            Notifications.create().title("Error")
                    .text("All Text Field must be filled")
                    .showError();
        } else if (!DigestUtils.md5Hex(tfOldPass.getText()).equals(oldPass)) {
            Notifications.create().title("Error")
                    .text("Old password doesn't match")
                    .showError();
        } else if (!tfNewPass.getText().equals(tfRepeat.getText())) {
            Notifications.create().title("Error")
                    .text("New password and Repeat doesn't match")
                    .showError();
        } else {
            String update = "UPDATE APP_Account SET Password = ? where id = ?";
            try {
                ps = con.prepareStatement(update);
                ps.setString(1, DigestUtils.md5Hex(tfNewPass.getText()));
                ps.setString(2, id);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
            }
        }
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void handleCancelBt(ActionEvent e) {
        ((Node) (e.getSource())).getScene().getWindow().hide();
    }


}
