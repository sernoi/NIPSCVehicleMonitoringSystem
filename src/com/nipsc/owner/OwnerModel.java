package com.nipsc.owner;

import com.nipsc.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class OwnerModel 
{
    public static ResultSet getAllOwners()
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "SELECT owner_id as 'Owner ID', fname as 'First Name', "
                         + "mname as 'Middel Name', lname as 'Last Name', position as 'Position',"
                         + " course as 'Course', year as 'Year', sec as 'Section', brgy as 'Brgy.',"
                         + " town as 'Town', province as Province from owner";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static ResultSet searchOwner(String str)
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "SELECT owner_id as 'Owner ID', fname as 'First Name', "
                         + "mname as 'Middle Name', lname as 'Last Name', position as 'Position',"
                         + " course as 'Course', year as 'Year', sec as 'Section', brgy as 'Brgy.',"
                         + " town as 'Town', province as Province from owner where "
                         + "owner_id LIKE '%" + str + "%' or fname LIKE '%" + str + "%' or "
                         + "mname LIKE '%" + str + "%' or lname LIKE '%" + str + "%' or "
                         + "position LIKE '%" + str + "%' or course LIKE '%" + str + "%' or "
                         + "year LIKE '%" + str + "%' or sec LIKE '%" + str + "%' or "
                         + "brgy LIKE '%" + str + "%' or town LIKE '%" + str + "%' or "
                         + "province LIKE '%" + str + "%';";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //Logger.getLogger(AddSubscriberModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static void saveOwner(int ownerID, String fName, String mName, String lName, String position, 
                          String course, String year, String section, String brgy, String town, String province)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Insert into owner values (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ownerID);
            stmt.setString(2, fName);
            stmt.setString(3, mName);
            stmt.setString(4, lName);
            stmt.setString(5, position);
            stmt.setString(6, course);
            stmt.setString(7, year);
            stmt.setString(8, section);
            stmt.setString(9, brgy);
            stmt.setString(10, town);
            stmt.setString(11, province);
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Owner Added!");
        } catch (SQLException ex) {
            Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection to DB!");
                Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void updateOwner(String oldOwnerID, int ownerID, String fName, String mName, String lName, String position, 
                          String course, String year, String section, String brgy, String town, String province)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Update owner set owner_id = ? , fname = ? , "
                    + "mname = ? , lname = ? , position = ? , course = ? , "
                    + "year = ? , sec = ? , brgy = ?, town = ?, province = ? where owner_id = '"+oldOwnerID+"'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ownerID);
            stmt.setString(2, fName);
            stmt.setString(3, mName);
            stmt.setString(4, lName);
            stmt.setString(5, position);
            stmt.setString(6, course);
            stmt.setString(7, year);
            stmt.setString(8, section);
            stmt.setString(9, brgy);
            stmt.setString(10, town);
            stmt.setString(11, province);
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Owner Info Updated!");
        } catch (SQLException ex) {
            Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection of DB!");
                Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void deleteOwner(String ownerID)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Delete from owner where owner_id = '"+ownerID+"'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
            //JOptionPane.showMessageDialog(null,"Subscriber Deleted!");
        } catch (SQLException ex) {
            Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection to DB!");
                Logger.getLogger(OwnerModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
