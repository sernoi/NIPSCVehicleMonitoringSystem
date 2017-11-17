package com.nipsc.main;

import com.nipsc.admin.AdminModel;
import com.nipsc.main.login.LoginController;
import com.nipsc.main.login.LoginFrame;
import java.lang.reflect.Array;
import javax.swing.JOptionPane;
import jssc.SerialPortList;

public class Start 
{
    
    public static void main(String[] args)
    {
        try
        {
            String[] list = getPortNames();
            Object selectedValue = JOptionPane.showInputDialog(null,
            "Choose RFID USB Port", "USB Communication Port",
            JOptionPane.INFORMATION_MESSAGE, null,
            list, list[0]);
            if(selectedValue == null)
            {
                System.exit(0);
            }

            LoginFrame lf = new LoginFrame();
            new LoginController(lf, (String) selectedValue);
            lf.setVisible(true);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to "
                               + "Continue Without the RFID Scanner?","Device Not Found",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION)
            {
                LoginFrame lf = new LoginFrame();
                new LoginController(lf, "None");
                lf.setVisible(true);
            }
            else
            {
                System.exit(0);
            }  
        }
    }
    
    static String[] getPortNames()
    {   
        //Method getPortNames() returns an array of strings. Elements of the array is already sorted.
        String[] portNames = SerialPortList.getPortNames();
        return portNames;
    }
}
