package com.nipsc.registration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;

/**
 *
 * @author John Rey Alipe
 */
public class RegistrationController 
{
    SerialPort arduinoPort;
    String port;
    RegistrationPanel rp;
    RegistrationModel rm = new RegistrationModel();
    String adminID;
    
    public RegistrationController(RegistrationPanel rp, String adminName, String adminID, String port)
    {
        this.rp = rp;
        this.adminID = adminID;
        this.port = port;
        
        this.rp.openNow(new OpenNowClass());
        this.rp.closeNow(new CloseNowClass());
        
        //initialize the
        initializeKeyBindings();
        
        this.rp.adminLbl.setText(adminName);

        this.rp.statusLbl.setText(""+connectArduino());
        
    }
    
    String connectArduino()
    {
        
        System.out.println("connectArduino");
        
        String success = "Disconnected";
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try {
                        int bytecount = 8;
                        //String st = serialPort.readString(serialPortEvent.getEventValue());
                        String st = serialPort.readString(bytecount);
                        arduinoPort = serialPort;
                        System.out.println(st);
                        int daysRemaining = rm.checkRFIDExpiration(st);
                        System.out.println(daysRemaining);
                        if(rm.RFIDExists(st))
                        {
                            if(daysRemaining < 0)
                            {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Your RFID has expired!\n"
                                        + "Please renew your card.","Error",JOptionPane.ERROR_MESSAGE);
                            }
                            else if(daysRemaining <= 30 && daysRemaining >= 1)
                            {
                                java.awt.Toolkit.getDefaultToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Your RFID is about to expire!\n"
                                        + "You have " + daysRemaining + " days remaining.","Warning",JOptionPane.WARNING_MESSAGE);
                                this.rp.rfidLbl.setText(st);
                                registerNow(st);
                                try 
                                {
                                    arduinoPort.writeString("o");
                                } catch (SerialPortException ex) {
                                    Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            else
                            {
                                this.rp.rfidLbl.setText(st);
                                registerNow(st);
                                try 
                                {
                                    arduinoPort.writeString("o");
                                } catch (SerialPortException ex) {
                                    Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                        }
                        else
                        {
                            java.awt.Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null,"RFID does not exist in the database!",
                                                        "RFID Not Found",JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SerialPortException ex) {
                        JOptionPane.showMessageDialog(null,ex);
                    }
                }
            });
            
            
            success = "Connected";
        } catch (SerialPortException ex) {
            //JOptionPane.showMessageDialog(null,ex);
            System.out.println("SerialPortException: " + ex.toString());
        }
        return success;
    }

    public void disconnectArduino(){
        
        if(arduinoPort != null){
            try {
                System.out.println("disconnectArduino()");
                arduinoPort.removeEventListener();
                arduinoPort.sendBreak(1000);
                //if(arduinoPort.isOpened()){
                    arduinoPort.closePort();
                //}
                
            } catch (SerialPortException ex) {
                //JOptionPane.showMessageDialog(null,ex);
            }
        }
    }
    
    boolean RFIDExists()
    {
        boolean status = false;
        
        return status;
    }
    
    void registerNow(String st)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        DateFormat tf = new SimpleDateFormat("hh:mm:ss a");
        Date dateobj = new Date();

        rm.registerNow(st);
        rp.ownerLbl.setText(""+rm.getOwnerName());
        rp.plateNumLbl.setText(""+rm.getPlateNumber());
        rp.dateLbl.setText(df.format(dateobj));
        rp.loginTimeLbl.setText(tf.format(dateobj));
        
        rm.setRegistrationStatus(st);
        rm.saveRegistration(rp.rfidLbl.getText(), adminID);
        
        String data1 = rp.rfidLbl.getText();
        String data2 = rp.ownerLbl.getText();
        String data3 = rp.plateNumLbl.getText();
        String data4 = tf.format(dateobj);
        String data5;
        
        //this condition sets registrationStatus variable during the first login of vehicle to "Login" instead of "null" 
        if(rm.getRegistrationStatus().equals("null"))
        {
           data5 = "Login";
        }
        else if(rm.getRegistrationStatus().equals("Login"))
        {
            data5 = "Logout";
        }
        else
        {
            data5 = "Login";
        }

        Object[] row = { data1, data2, data3, data4, data5 };

        DefaultTableModel model = (DefaultTableModel) rp.jTable1.getModel();

        model.addRow(row);
    }
    
    class OpenNowClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            try {
                arduinoPort.writeString("o");
            } catch (SerialPortException ex) {
                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    class CloseNowClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            try {
                arduinoPort.writeString("c");
            } catch (SerialPortException ex) {
                Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void initializeKeyBindings() { 
        Action a = new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    arduinoPort.writeString("c");
                } catch (SerialPortException ex) {
                    Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        this.rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "KEY");
        this.rp.getActionMap().put("KEY", a);
    }
}
