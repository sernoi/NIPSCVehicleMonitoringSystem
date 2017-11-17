package com.nipsc.vehicle;

import com.nipsc.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class VehicleModel 
{
    public static ResultSet getAllVehicles()
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "SELECT rfid as 'RFID', plate_num as 'Plate Number', "
                         + "vehicle_type as 'Vehicle Type', reg_date as 'Registration Date', "
                         + "or_num as 'OR Number', cr_num as 'CR Number', owner_id as 'Owner ID'  from vehicle";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static ResultSet searchVehicle(String str)
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
           
            String sql = "SELECT rfid as 'RFID', plate_num as 'Plate Number', "
                         + "vehicle_type as 'Vehicle Type', reg_date as 'Registration Date', "
                         + "or_num as 'OR Number', cr_num as 'CR Number', owner_id as 'Owner ID' from "
                         + "vehicle where "
                         + "rfid LIKE '%" + str + "%' or plate_num LIKE '%" + str + "%' or "
                         + "vehicle_type LIKE '%" + str + "%' or reg_date LIKE '%" + str + "%' or "
                         + "or_num LIKE '%" + str + "%' or cr_num LIKE '%" + str + "%' or "
                         + "owner_id LIKE '%" + str + "%'";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //Logger.getLogger(AddSubscriberModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static void saveVehicle(String rfid, String plateNumber, String vehicleType, 
                                    String regDate, String orNumber, String crNumber, String ownerID)
    {
        Connection conn = null;
        
        int startPoint = ownerID.indexOf(":")+1;
        int numOwnerID = Integer.parseInt(ownerID.substring(startPoint,(ownerID.length()-1)));
        
        try {
            conn = DB.getConnection();
            String sql = "Insert into vehicle values (?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, rfid);
            stmt.setString(2, plateNumber);
            stmt.setString(3, vehicleType);
            stmt.setString(4, regDate);
            stmt.setString(5, orNumber);
            stmt.setString(6, crNumber);
            stmt.setInt(7, numOwnerID);
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Vehicle Added!");
        } catch (SQLException ex) {
            Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection to DB!");
                Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void updateVehicle(String oldrfid, String rfid, String plateNumber, String vehicleType, 
                                    String regDate, String orNumber, String crNumber, String ownerID)
    {
        Connection conn = null;
        
        int startPoint = ownerID.indexOf(":")+1;
        int numOwnerID = Integer.parseInt(ownerID.substring(startPoint,(ownerID.length()-1)));
        
        try {
            conn = DB.getConnection();
            String sql = "Update vehicle set rfid = ? , plate_num = ? , "
                    + "vehicle_type = ? , reg_date = ? , or_num = ?, cr_num = ?, "
                    + "owner_id = ? where rfid = '"+oldrfid+"'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, rfid);
            stmt.setString(2, plateNumber);
            stmt.setString(3, vehicleType);
            stmt.setString(4, regDate);
            stmt.setString(5, orNumber);
            stmt.setString(6, crNumber);
            stmt.setInt(7, numOwnerID);
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Vehicle Info Updated!");
        } catch (SQLException ex) {
            Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection of DB!");
                Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void deleteVehicle(String rfid)
    {
        Connection conn = null;
        try {
            conn = DB.getConnection();
            String sql = "Delete from vehicle where rfid = '"+rfid+"'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
            //JOptionPane.showMessageDialog(null,"Subscriber Deleted!");
        } catch (SQLException ex) {
            Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException ex) 
            {
                JOptionPane.showMessageDialog(null, "Cannot close connection to DB!");
                Logger.getLogger(VehicleModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
