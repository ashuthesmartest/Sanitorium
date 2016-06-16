package com;

import java.sql.*;
import javax.swing.JOptionPane;

public class DBConnect 
{
    static Connection c ;
    public static Statement st ;
    static
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver") ;
            c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ashuthesmartest", "ashutosha") ;
            st = c.createStatement(); 
        }    
        catch(Exception ex)
        {
         JOptionPane.showMessageDialog(null, "Database Error");
        }
    }        
}
