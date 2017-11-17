package com.nipsc.util;

import com.nipsc.registration.RegistrationPanel;
import javax.swing.JOptionPane;
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class JSSCModel
{
    RegistrationPanel rp;
    SerialPort arduinoPort = null;
            
    public JSSCModel(RegistrationPanel rp)
    {
        this.rp = rp;
    }
    public String[] getPortNames()
    {   
        //Method getPortNames() returns an array of strings. Elements of the array is already sorted.
        String[] portNames = SerialPortList.getPortNames();
        for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }
        return portNames;
    }
    
    public String connectArduino(String port)
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
                        int bytecount = 11;
                        //String st = serialPort.readString(serialPortEvent.getEventValue());
                        String st = serialPort.readString(bytecount);
                        System.out.println(st);
                        this.rp.rfidLbl.setText(st);
                    } catch (SerialPortException ex) {
                        JOptionPane.showMessageDialog(null,ex);
                    }
                }
            });
            
            arduinoPort = serialPort;
            success = "Connected";
        } catch (SerialPortException ex) {
            //JOptionPane.showMessageDialog(null,ex);
            System.out.println("SerialPortException: " + ex.toString());
        }
        return success;
    }

    public static void disconnectArduino(String portStr){
        SerialPort port = new SerialPort(portStr);
        System.out.println("disconnectArduino()");
        System.out.println(portStr);
        try 
        {
            port.removeEventListener();

            if(port.isOpened()){
                port.closePort();
                System.out.println("gin close");
            }

        } catch (SerialPortException ex) {
            //JOptionPane.showMessageDialog(null,ex);
        }
    }
}