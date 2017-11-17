package com.nipsc.main.login;

import com.nipsc.db.DB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class LoginModel 
{
    private static String fnameToSend;
    
    public static String loginEmp(String un, String pw)
    {
        String position = null;
        Connection conn = DB.getConnection();;
        ResultSet rs;
        try 
        {
            if (un != null && pw != null) 
            {
                String sql = "SELECT position, CONCAT_WS(' ', fname, lname) AS 'name' FROM admin "
                            + "where admin_id='" + un + "' and pw='" + pw + "'";
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) 
                {
                    //in this case enter when at least one result comes it means user is valid
                    position = rs.getString("position");
                    fnameToSend = rs.getString("name");
                }
                else
                {
                    position = "none";
                    fnameToSend = "none";
                    JOptionPane.showMessageDialog(null, "Wrong username or password!","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Fields cannot be empty!","Error",JOptionPane.ERROR_MESSAGE);
            }

        // You can also validate user by result size if its comes zero user is invalid else user is valid
        
        } catch (SQLException err) 
        {
            JOptionPane.showMessageDialog(null, err.getMessage());
        }
        return position;
    }
    
    public static String getFname()
    {
        return fnameToSend;
    }
}
