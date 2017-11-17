package com.nipsc.registration;

import com.nipsc.admin.AdminModel;
import com.nipsc.db.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author John Rey Alipe
 */
public class RegistrationModel 
{
    String ownerName;
    String plateNumber;
    String registrationStatus = "null";
    

    public String getOwnerName() {
        return ownerName;
    }


    public String getPlateNumber() {
        return plateNumber;
    }
    
    public String getRegistrationStatus()
    {
        return registrationStatus;
    }
    
    public boolean RFIDExists(String rfid)
    {
        boolean status = false;
        
        Connection conn = DB.getConnection();
        ResultSet rs;
        try 
        {
            String sql = "SELECT rfid from vehicle where rfid = '"+rfid+"'";

            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()) 
            {
                //in this case enter when at least one result comes it means user is valid
                //this.registrationStatus = rs.getString("status") + "";
                status = true;
            }

        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
        
        return status;
    }
    
    public int checkRFIDExpiration(String rfid)
    {
        Connection conn = DB.getConnection();
        ResultSet rs;
        int status = 0;
        try 
        {
            String sql = "SELECT DATEdiff(reg_date + interval 1 YEAR,current_date()) "
                    + "as days_rem from vehicle where rfid = '"+rfid+"'";

            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) 
            {
                //in this case enter when at least one result comes it means user is valid
                status = rs.getInt("days_rem");
            }

        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
            return status;
        }
        return status;
    }
    
    public void setRegistrationStatus(String rfid) {
        Connection conn = DB.getConnection();
        ResultSet rs;
        try 
        {
            String sql = "SELECT status from registration where rfid = '"+rfid+"' order by logtime desc limit 1";

            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) 
            {
                //in this case enter when at least one result comes it means user is valid
                this.registrationStatus = rs.getString("status") + "";
            }

        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }

    /**
     * Sets the local variable <code>plateNumber</code> and <code>ownerName</code>.
     *
     * @param rfid determines the <code>rfid</code> of the vehicle that will be
     *             searched from the database.
     * 
     */
    public void registerNow(String rfid)
    {
        Connection conn = DB.getConnection();
        ResultSet rs;
        try 
        {
            String sql = "SELECT v.plate_num as 'pn', CONCAT_WS(' ', o.fname, o.lname) AS 'name' FROM "
                        + "vehicle v, owner o " 
                        + "where v.owner_id = o.owner_id and rfid = '"+rfid+"'";
            
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) 
            {
                //in this case enter when at least one result comes it means user is valid
                this.plateNumber = rs.getString("pn");
                this.ownerName = rs.getString("name");
            }

        // You can also validate user by result size if its comes zero user is invalid else user is valid
        
        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
    }
    
    public void saveRegistration(String rfid, String adminID)
    {
        Connection conn = null;
        java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        try {
            conn = DB.getConnection();
            String sql = "Insert into registration (rfid,admin_id,logtime,status) values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, rfid);
            stmt.setString(2, adminID);
            stmt.setTimestamp(3, date);
            System.out.println(this.registrationStatus);
            if(this.registrationStatus.equals("null"))
            {
                stmt.setString(4,"Login");
            }
            else if(this.registrationStatus.equals("Logout"))
            {
                stmt.setString(4,"Login");
            }
            else
            {
                stmt.setString(4,"Logout");
            }
            stmt.execute();
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
