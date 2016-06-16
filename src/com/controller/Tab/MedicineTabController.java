
package com.controller.Tab;

import com.DBConnect;
import com.controller.LoginController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;


public class MedicineTabController extends LoginController implements Initializable
{

    @FXML
    private Button btSearch;
    
    @FXML
    private Button btadd ;
    
    @FXML
    private Button btsub ;
    
    @FXML
    private Button btorder ;
    
    @FXML
    private Label l1 ;
       
    @FXML
    private TextField t1 ;
   
    
    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException, SQLException 
    {
        if(e.getSource() == btadd)
        {
            String s1 = l1.getText() ;
            l1.setText(String.valueOf(Integer.parseInt(s1)+ 1) );
        }
        
        if(e.getSource() == btsub)
        {
            String s1 = l1.getText() ;
            if(Integer.valueOf(s1) >= 1 )
            {    
                l1.setText(String.valueOf(Integer.parseInt(s1)- 1) );
            }    
        }
        
        if(e.getSource() == btSearch)
        {
            String n = t1.getText() ;
            int l = Integer.valueOf(l1.getText()) ;
            ResultSet rs = DBConnect.st.executeQuery("Select * from medicine where name ='"+ n+"' and quantity >" + l) ;
            if(rs.next())
            {
                JOptionPane.showMessageDialog(null, "Medicine Available! Fill in your Address and click on Order. \nPrice = "+rs.getInt("price")*l);
            }    
        }   
        if(e.getSource()== btorder)
        {
            int tot = 0 ;
            String n = t1.getText() ;
            int l = Integer.valueOf(l1.getText()) ;
            ResultSet rs = DBConnect.st.executeQuery("Select * from medicine where name ='"+ n+"' and quantity >" + l) ;            
            String pr = LoginController.globalName ;
            if(rs.next())
            {
                tot = rs.getInt("price")*l ;
                DBConnect.st.executeUpdate("update medicine set quantity ="+(rs.getInt(2) - l) + "where name='"+n+"'") ;    
                JOptionPane.showMessageDialog(null, "Order Submitted! "+ tot + " Rs. has been added to your pending fee");
            } 
            ResultSet rs1 = DBConnect.st.executeQuery("Select * from app_patient where username = '"+pr+"'") ;
            if(rs1.next())
                {    
                    System.out.println(rs1.getString(1));
                    DBConnect.st.executeUpdate("update app_patient set fee ="+(rs1.getInt(4) + tot) + "where username='"+pr+"'") ;
                }
                
        }    
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        
    }
    
    
    
}
