package com.controller;

import com.DBConnect;
import java.awt.Desktop;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javax.swing.JOptionPane;

public class LoginController implements Initializable 
{

    Stage stage ;
    Parent root ;
    Scene scene ;
    public static String globalName ;
    
    @FXML
    ComboBox cb = new ComboBox() ;
    
    @FXML
    TextField t1 = new TextField() ;

    @FXML
    TextField t2 = new TextField() ;
            
    @FXML
    Button login = new Button() ;   
    
    @FXML 
    Hyperlink hl1 = new Hyperlink() ;

    @FXML 
    Hyperlink hl2 = new Hyperlink() ;

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException, SQLException 
    {
            String s1 = t1.getText() ;
            String s2 = t2.getText() ;
            if(s1.equals("admin") && s2.equals("admin"))
            {
                ((Node) (e.getSource())).getScene().getWindow().hide();
                 Parent parent = FXMLLoader.load(getClass().getResource("/com/view/Main_admin.fxml"));
                 Stage stage = new Stage();
                 Scene scene = new Scene(parent);
                 stage.setScene(scene);
                 stage.getIcons().add(new Image("/com/icon.png"));
                 stage.setResizable(false);
                 stage.show();
            }
            else
            {   
                String out = cb.getSelectionModel().getSelectedItem().toString() ;
                if(out.equals("Doctor"))
                {
                    ResultSet rs = DBConnect.st.executeQuery("select * from doctordb where username = '"+s1+"' and password = '"+s2+"'");
                    if(rs.next())
                    {
                        globalName = rs.getString(3) ;
                        ((Node) (e.getSource())).getScene().getWindow().hide();
                        Parent parent = FXMLLoader.load(getClass().getResource("/com/view/Main_doctor.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("/com/icon.png"));
                        stage.setResizable(false);
                        stage.show();
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(null, "Wrong Password. Please Try Again! ");
                        t2.setText("");
                    }       
                }
                else if(out.equals("Patient"))
                {
                    ResultSet rs = DBConnect.st.executeQuery("select * from patientdb where username = '"+s1+"' and password = '"+s2+"'");
                    if(rs.next())
                    {
                        globalName = rs.getString(1) ;
                        ((Node) (e.getSource())).getScene().getWindow().hide();
                        Parent parent = FXMLLoader.load(getClass().getResource("/com/view/Main_patient.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("/com/icon.png"));
                        stage.setResizable(false);
                        stage.show();
                    } 
                    else 
                    {
                        JOptionPane.showMessageDialog(null, "Wrong Password. Please Try Again! ");
                        t2.setText("");
                    }       
                }
            }
}

    @FXML
    private void linkaction(ActionEvent event) throws IOException 
    {     
        if(event.getSource() == hl1)
        {    
            try 
            {
                Desktop.getDesktop().browse(new URI("https://www.twitter.com/Siemens"));
            } 
            catch (Exception ex) 
            {
                ex.printStackTrace(); 
            }
        }    
        else if(event.getSource() == hl2)
        {
            try
            {    
                Desktop.getDesktop().browse(new URI("https://www.facebook.com/Siemens"));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }        
        }    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {

        Platform.runLater(() -> {
            t1.requestFocus();
        });
        cb.getItems().addAll("Doctor", "Patient") ;
    }
}
