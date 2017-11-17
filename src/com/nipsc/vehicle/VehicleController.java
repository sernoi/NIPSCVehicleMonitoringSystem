package com.nipsc.vehicle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellEditor;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author John Rey Alipe
 */
public class VehicleController 
{
    String oldRFID;
    
    VehiclePanel vp;
    AddVehicleFrame avf;
    EditVehicleFrame evf;
    
    public VehicleController(VehiclePanel vp, AddVehicleFrame avf, EditVehicleFrame evf)
    {
        this.vp = vp;
        this.avf = avf;
        this.evf = evf;
        
        this.vp.searchVehicle(new SearchVehicleClass());
        this.avf.saveVehicleListener(new SaveVehicleClass());
        this.vp.editVehicle(new EditVehicleClass());
        this.evf.updateVehicleListener(new UpdateVehicleClass());
        this.vp.deleteVehicle(new DeleteVehicleClass());
        
        this.vp.addVehicleBtn.addActionListener((ActionEvent e) -> {
            this.avf.setVisible(true);
        });
        
        
        displayVehicles();
    }
    
    void displayVehicles()
    {
        //this is to load all the schedules in the database upon selecting the Event Scheduler in the menu bar
        ResultSet rs = VehicleModel.getAllVehicles();
        vp.vehicleTable.setModel(DbUtils.resultSetToTableModel(rs));
        // this is to disable editing in the jtable
        for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
        {
            TableCellEditor ce = vp.vehicleTable.getDefaultEditor(c);
            if (ce instanceof DefaultCellEditor) 
            {
                ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
            }
        }
    }
    
    class SearchVehicleClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            ResultSet rs = VehicleModel.searchVehicle(vp.searchTF.getText());
            vp.vehicleTable.setModel(DbUtils.resultSetToTableModel(rs));
            // this is to disable editing in the jtable
            for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
            {
                TableCellEditor ce = vp.vehicleTable.getDefaultEditor(c);
                if (ce instanceof DefaultCellEditor) 
                {
                    ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
                }
            }
        }
    }
    
    class SaveVehicleClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(avf.jCalendar1.getDate());
                VehicleModel.saveVehicle(avf.RFIDTF.getText(), avf.plateNumberTF.getText(),
                                avf.vehicleTypeTF.getText(), dateStr,avf.orNumberTF.getText(),
                                avf.crNumberTF.getText(),avf.ownerCB.getSelectedItem().toString());
                displayVehicles();
        }
    }
    
    class EditVehicleClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //evf.resetFields();
            int dataRow = vp.vehicleTable.getSelectedRow();
            if(dataRow >= 0)
            {
                String vehicleID = (String) vp.vehicleTable.getValueAt(dataRow,0);
                oldRFID = vehicleID;
                evf.RFIDTF.setText(""+vehicleID);
                evf.setTitle("Edit Information of Vehicle ID: " + vehicleID);
                evf.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select vehicle to edit.");
            }
        }
    }
    
    class UpdateVehicleClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(evf.jCalendar1.getDate());
                VehicleModel.updateVehicle(oldRFID, evf.RFIDTF.getText(), evf.plateNumberTF.getText(),
                                evf.vehicleTypeTF.getText(), dateStr,evf.orNumberTF.getText(),
                                evf.crNumberTF.getText(),evf.ownerCB.getSelectedItem().toString());
                displayVehicles();
        }
    }
    
    class DeleteVehicleClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int dataRow = vp.vehicleTable.getSelectedRow();
            if(dataRow >= 0)
            {
                String rfid = vp.vehicleTable.getValueAt(dataRow,0).toString();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to "
                                   + "Delete Vehicle: " + rfid + "?","Warning",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION)
                {
                    VehicleModel.deleteVehicle(rfid);
                    displayVehicles();
                }  
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select vehicle to delete.");
            }
        }
    }

        
}
