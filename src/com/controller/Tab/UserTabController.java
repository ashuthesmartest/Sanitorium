package com.controller.Tab;

import com.DBConnect;
import com.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserTabController implements Initializable 
{
    private Statement st;
    private ResultSet rs;
    
    @FXML
    ComboBox cb = new ComboBox() ;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn userNameColumn;
    @FXML
    private TableColumn emailColumn;
    @FXML
    private TableColumn levelColumn;
    @FXML
    private TableColumn docIdColumn;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfDoctorID;
    @FXML
    private RadioButton rdBt1;
    @FXML
    private ToggleGroup groupLevel;
    @FXML
    private RadioButton rdBt2;
    @FXML
    private RadioButton rdBt3;
    @FXML
    private Button btAdd;
    @FXML
    private Button btUpdate;
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private int selectedLevel;
    private EmailValidator validator = EmailValidator.getInstance();

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try 
        {
            ResultSet rs = DBConnect.st.executeQuery("Select name from app_doctor") ;
            while(rs.next())
            {
                cb.getItems().add(rs.getString("name")) ;
            }    
        } 
        catch (SQLException ex) 
        {
        
        } 
    }

 
    @FXML
    public void handleBtAdd() {
        if (!isValidID(tfDoctorID.getText())) {
            Notifications.create().title("Add Error")
                    .text("ID not avaible")
                    .showError();
        }
        if (isDuplicateID(tfDoctorID.getText())) {
            Notifications.create().title("Add Error")
                    .text("ID already taken")
                    .showError();
        } else if (tfEmail.getText().equals("") || tfPassword.getText().equals("")
                || tfUsername.getText().equals("")) {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } else if (rdBt1.isSelected() == false
                && rdBt2.isSelected() == false
                && rdBt3.isSelected() == false) {
            Notifications.create().title("Add Error")
                    .text("Please select a user level")
                    .showError();
        } else if (isDuplicateName(tfUsername.getText())) {
            Notifications.create().title("Add Error")
                    .text("Username already taken")
                    .showError();
        } else if (isDuplicateEmail(tfEmail.getText())) {
            Notifications.create().title("Add Error")
                    .text("Email already taken!")
                    .showError();
        } else if (!(validator.isValid(tfEmail.getText()))) {
            Notifications.create().title("Add Error")
                    .text("Invalid Email Adress")
                    .showError();
        } else {
            String id = tfDoctorID.getText();
            String uname = tfUsername.getText();
            String pass = DigestUtils.md5Hex(tfPassword.getText());
            String email = tfEmail.getText();

            User user = new User();
            user.setDoctorID(id);
            user.setUsername(uname);
            user.setPassworld(pass);
            user.setEmail(email);
            user.setLevel(selectedLevel);
            userData.add(user);
        }
    }

    @FXML
    public void handleBtUpdate() 
    {
        if (tfEmail.getText().equals("") || tfPassword.getText().equals("")|| tfUsername.getText().equals("")) 
        {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } 
        else if (!isDuplicateName(tfUsername.getText()) || !userTable.getSelectionModel().getSelectedItem().getUsername().equals(tfUsername.getText())) 
        {
            Notifications.create().title("Add Error")
                    .text("Username doesn't exist!")
                    .showError();
        } 
        else if (!validator.isValid(tfEmail.getText())) 
        {
            Notifications.create().title("Add Error").text("Invalid Email Adress").showError();
        } 
        else 
        {
            String uname = tfUsername.getText();
            String pass = DigestUtils.md5Hex(tfPassword.getText());
            String email = tfEmail.getText();

            User user = userTable.getSelectionModel().getSelectedItem();
            user.setEmail(email);
            user.setLevel(selectedLevel);
            user.setPassworld(pass);
            forceRefresh();
            Notifications.create().title("Update").text("Update Successfully").showInformation();
        }
    }

    @FXML
    public void handleBtDelete() 
    {
        int selectedIndex = userTable.getSelectionModel().getSelectedIndex();
        String selectedUsername = userData.get(selectedIndex).getUsername();
        userData.remove(selectedIndex);
    }

    public boolean isDuplicateName(String s) 
    {
        return userData.stream().anyMatch((user) -> (s.equals(user.getUsername())));
    }

    public boolean isDuplicateEmail(String s) 
    {
        return userData.stream().anyMatch((user) -> (s.equals(user.getEmail())));
    }

    public boolean isDuplicateID(String id) 
    {
        return userData.stream().anyMatch((user) -> (id.equals(user.getDoctorID())));
    }

    public boolean isValidID(String id) 
    {
        List<String> idList = new ArrayList<>();
        String sql = "SELECT ID FROM APP_Doctor";
        try 
        {
            rs = DBConnect.st.executeQuery(sql);
            while (rs.next()) 
            {
                idList.add(rs.getString(1));
            }
            if (st != null) 
            {
                st.close();
            }
            if (rs != null) 
            {
                rs.close();
            }
            
        } 
        catch(SQLException ex) 
        {
            ex.printStackTrace();
        }
        return idList.stream().anyMatch((doctorID) -> (doctorID.equals(id)));
    }

    public User getUser(String name) {
        for (User user : userData) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public void forceRefresh() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    userTable.getColumns().get(0).setVisible(false);
                    userTable.getColumns().get(0).setVisible(true);
                });
            }
        }, 50);
    }

}
