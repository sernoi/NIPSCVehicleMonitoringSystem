package com.nipsc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DB {
    public static Connection getConnection()
    {
        Connection con = null;
        try 
        {
            String userName = "root";  //user of the database
            String password = "1234";  //password of the database
            String url = "jdbc:mysql://localhost/nipsc";  //the database that will be used
            Class.forName ("com.mysql.jdbc.Driver"); //the driver of the database
            con = DriverManager.getConnection (url, userName, password);
        } 
        catch (ClassNotFoundException| SQLException ex) 
        {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.err.print(ex);
            JOptionPane.showMessageDialog(null, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return con;
    }
}
