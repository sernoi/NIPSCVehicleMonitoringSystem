package com.nipsc.report.registration;

import com.nipsc.admin.AdminModel;
import com.nipsc.db.DB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author John Rey Alipe
 */
public class ReportRegistrationModel 
{ 
    public ResultSet getAllRegistration()
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            
            String sql = "SELECT r.reg_id as 'Reg #', r.rfid as 'RFID', v.plate_num as 'Plate Number', "
                         + "o.owner_id as 'Owner ID', "
                         + "CONCAT_WS(' ', o.fname, o.lname) AS 'Name', "
                         + "r.admin_id as 'Admin ID', r.logtime as 'Log Date and Time', r.status as Status "
                         + "FROM owner o, vehicle v, registration r "
                         + "where r.rfid = v.rfid and o.owner_id = v.owner_id order by r.reg_id";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(AdminModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static ResultSet searchRegistration(String str)
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            
            String sql = "SELECT r.reg_id as 'Reg #', r.rfid as 'RFID', v.plate_num as 'Plate Number', "
                         + "o.owner_id as 'Owner ID', "
                         + "CONCAT_WS(' ', o.fname, o.lname) AS 'Name', "
                         + "r.admin_id as 'Admin ID', r.logtime as 'Log Date and Time', r.status as Status "
                         + "FROM registration r "
                         + "join vehicle v on r.rfid = v.rfid "
                         + "join owner o on o.owner_id = v.owner_id "
                         + "where "
                         + "r.reg_id LIKE '%" + str + "%' or r.rfid LIKE '%" + str + "%' or "
                         + "v.plate_num LIKE '%" + str + "%' or o.owner_id LIKE '%" + str + "%' or "
                         + "CONCAT_WS(' ', o.fname, o.lname) like '%" + str + "%' or r.admin_id LIKE '%" + str + "%' or "
                         + "r.status LIKE '%" + str + "%' and "
                         + "r.rfid = v.rfid and o.owner_id = v.owner_id";
            
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //Logger.getLogger(AddSubscriberModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public static ResultSet searchRegistration(String strDate1, String strDate2)
    {
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = DB.getConnection();
            
            String sql = "SELECT r.reg_id as 'Reg #', r.rfid as 'RFID', v.plate_num as 'Plate Number', "
                         + "o.owner_id as 'Owner ID', "
                         + "CONCAT_WS(' ', o.fname, o.lname) AS 'Name', "
                         + "r.admin_id as 'Admin ID', r.logtime as 'Log Date and Time', r.status as Status "
                         + "FROM registration r "
                         + "join vehicle v on r.rfid = v.rfid "
                         + "join owner o on o.owner_id = v.owner_id "
                         + "where "
                         + "logtime BETWEEN '"+strDate1+ " 00:00:00.00' AND '"+strDate2+ " 23:59:59.000'";
            
            //String sql = "SELECT * FROM registration WHERE logtime BETWEEN '"+strDate1+ " 00:00:00.00' AND '"+strDate2+ " 23:59:59.000'";
            
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //Logger.getLogger(AddSubscriberModel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            return null;
        }
        return rs;
    }
    
    public List<String> getOwnerOfVehicle(int regID)
    {
        Connection conn = DB.getConnection();
        List<String> ownerData = new ArrayList<String>();
        try 
        {
            String sql = "SELECT o.owner_id,o.fname,o.mname,o.lname,o.position,o.course,o.year,o.sec,o.brgy,o.town,o.province "
                        + "FROM owner o, vehicle v, registration r "
                        + "where r.rfid = v.rfid and o.owner_id = v.owner_id and r.reg_id = '"+regID+"'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    ownerData.add(columnValue);
                }
                System.out.println("");
            }
        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
        return ownerData;
    }
    
    public List<String> getVehicleInfo(int regID)
    {
        Connection conn = DB.getConnection();
        List<String> ownerData = new ArrayList<String>();
        try 
        {
            String sql = "SELECT v.rfid,v.plate_num,v.vehicle_type,v.reg_date,v.or_num,v.cr_num,o.owner_id, "
                        + "CONCAT_WS(' ', o.fname, o.lname) AS 'Owner' "
                        + "FROM owner o, vehicle v, registration r "
                        + "where r.rfid = v.rfid and o.owner_id = v.owner_id and r.reg_id = '"+regID+"'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    ownerData.add(columnValue);
                }
                System.out.println("");
            }
        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
        return ownerData;
    }
}
