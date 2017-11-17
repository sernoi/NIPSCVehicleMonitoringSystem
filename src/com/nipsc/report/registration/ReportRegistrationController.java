package com.nipsc.report.registration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import net.proteanit.sql.DbUtils;

public class ReportRegistrationController 
{
    ReportRegistrationPanel rrp;
    ReportRegistrationModel rrm;
            
    public ReportRegistrationController(ReportRegistrationPanel rrp, ReportRegistrationModel rrm)
    {
        this.rrp = rrp;
        this.rrm = rrm;
        
        this.rrp.viewOwner(new ViewOwnerClass());
        this.rrp.viewVehicle(new ViewVehicleClass());
        this.rrp.searchVehicle(new SearchRegistrationClass());
        this.rrp.printReport(new PrintReportClass());
        
        displayReport();
    }
 
    void displayReport()
    {
        //this is to load all the schedules in the database upon selecting the Event Scheduler in the menu bar
        ResultSet rs = this.rrm.getAllRegistration();
        rrp.reportTable.setModel(DbUtils.resultSetToTableModel(rs));
        // this is to disable editing in the jtable
        for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
        {
            TableCellEditor ce = rrp.reportTable.getDefaultEditor(c);
            if (ce instanceof DefaultCellEditor) 
            {
                ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
            }
        }
    }
    
    class SearchRegistrationClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(rrp.jRadioButton1.isSelected())
            {
                ResultSet rs = ReportRegistrationModel.searchRegistration(rrp.searchTF.getText());
                rrp.reportTable.setModel(DbUtils.resultSetToTableModel(rs));
                // this is to disable editing in the jtable
                for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
                {
                    TableCellEditor ce = rrp.reportTable.getDefaultEditor(c);
                    if (ce instanceof DefaultCellEditor) 
                    {
                        ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
                    }
                }
            }
            else
            {
                String strDate1 = ((JTextField)rrp.jDateChooser1.getDateEditor().getUiComponent()).getText();
                String strDate2 = ((JTextField)rrp.jDateChooser2.getDateEditor().getUiComponent()).getText();
                ResultSet rs = ReportRegistrationModel.searchRegistration(strDate1, strDate2);
                rrp.reportTable.setModel(DbUtils.resultSetToTableModel(rs));
                // this is to disable editing in the jtable
                for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
                {
                    TableCellEditor ce = rrp.reportTable.getDefaultEditor(c);
                    if (ce instanceof DefaultCellEditor) 
                    {
                        ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
                    }
                }
            }
        }
    }
    
    class ViewOwnerClass implements ActionListener
    {
        List<String> ownerData = new ArrayList<String>();
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int dataRow = rrp.reportTable.getSelectedRow();
            if(dataRow >= 0)
            {
                int regID = (int) rrp.reportTable.getValueAt(dataRow,0);
                ownerData = rrm.getOwnerOfVehicle(regID);
                JOptionPane.showMessageDialog(rrp,"Owner ID: " + ownerData.get(0) 
                                                + "\nFirst Name: " + ownerData.get(1) 
                                                + "\nMIddle Name: " + ownerData.get(2) 
                                                + "\nLast Name: " + ownerData.get(3) 
                                                + "\nPosition: " + ownerData.get(4) 
                                                + "\nCourse: " + ownerData.get(5) 
                                                + "\nYear: " + ownerData.get(6) 
                                                + "\nSection: " + ownerData.get(7) 
                                                + "\nBrgy: " + ownerData.get(8) 
                                                + "\nTown: " + ownerData.get(9) 
                                                + "\nProvince: " + ownerData.get(10) 
                                                ,"Owner Info",JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select registration to view.");
            }
        }
    }
    
    class ViewVehicleClass implements ActionListener
    {
        List<String> vehicleData = new ArrayList<String>();
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int dataRow = rrp.reportTable.getSelectedRow();
            if(dataRow >= 0)
            {
                int regID = (int) rrp.reportTable.getValueAt(dataRow,0);
                vehicleData = rrm.getVehicleInfo(regID);
                JOptionPane.showMessageDialog(rrp,"RFID: " + vehicleData.get(0) 
                                                + "\nPlate Number: " + vehicleData.get(1) 
                                                + "\nVehicle Type: " + vehicleData.get(2) 
                                                + "\nReg Date: " + vehicleData.get(3) 
                                                + "\nOR Number: " + vehicleData.get(4) 
                                                + "\nCR Number: " + vehicleData.get(5) 
                                                + "\nOwner ID: " + vehicleData.get(6) 
                                                + "\nOwner Name: " + vehicleData.get(7) 
                                                ,"Owner Info",JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select registration to view.");
            }
        }
    }
    
    class PrintReportClass implements ActionListener
    {
        public void actionPerformed(ActionEvent e) 
        {
            MessageFormat header = new MessageFormat("Registration");
            MessageFormat footer = new MessageFormat("Page{0,number,integer}");

            try
            {

                rrp.reportTable.print(JTable.PrintMode.FIT_WIDTH,header,footer);
            }catch (java.awt.print.PrinterException p)
            {
                JOptionPane.showMessageDialog(null, p.getMessage());
            }
        }
    }
}
