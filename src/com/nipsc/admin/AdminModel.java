package com.nipsc.admin;

import com.nipsc.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AdminModel 
{
    public static ResultSet getAllAdmins()
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "SELECT admin_id as 'Admin ID', fname as 'First Name', "
                         + "mname as 'Middle Name', lname as 'Last Name', position as 'Position' from admin";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static ResultSet searchAdmin(String str)
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "SELECT admin_id as 'Admin ID', fname as 'First Name', "
                         + "mname as 'Middle Name', lname as 'Last Name', position as 'Position' from admin where "
                         + "admin_id LIKE '%" + str + "%' or fname LIKE '%" + str + "%' or "
                         + "mname LIKE '%" + str + "%' or lname LIKE '%" + str + "%' or "
                         + "position LIKE '%" + str + "%'";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //Logger.getLogger(AddSubscriberModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static void saveAdmin(String adminID, String fName, String mName, String lName, String position, String password)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Insert into admin values (?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, adminID);
            stmt.setString(2, fName);
            stmt.setString(3, mName);
            stmt.setString(4, lName);
            stmt.setString(5, position);
            stmt.setString(6, password);
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Admin Added!");
        } catch (SQLException ex) {
            Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection to DB!");
                Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void updateAdmin(String oldAdminID, String adminID, String fName, String mName, String lName, String position)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Update admin set admin_id = ? , fname = ? , "
                    + "mname = ? , lname = ? , position = ? where admin_id = '"+oldAdminID+"'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, adminID);
            stmt.setString(2, fName);
            stmt.setString(3, mName);
            stmt.setString(4, lName);
            stmt.setString(5, position);
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Admin Info Updated!");
        } catch (SQLException ex) {
            Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection of DB!");
                Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void deleteAdmin(String adminID)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Delete from admin where admin_id = '"+adminID+"'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
            //JOptionPane.showMessageDialog(null,"Subscriber Deleted!");
        } catch (SQLException ex) {
            Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection to DB!");
                Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
