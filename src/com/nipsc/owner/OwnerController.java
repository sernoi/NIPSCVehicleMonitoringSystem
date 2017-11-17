package com.nipsc.owner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellEditor;
import net.proteanit.sql.DbUtils;

public class OwnerController 
{
    String oldOwnerID;
    
    OwnerPanel op;
    AddOwnerFrame aof;
    EditOwnerFrame eof;
    
    public OwnerController(OwnerPanel op, AddOwnerFrame aof, EditOwnerFrame eof)
    {
        this.op = op;
        this.aof = aof;
        this.eof = eof;
        
        this.op.searchOwner(new SearchOwnerClass());
        this.aof.saveOwnerListener(new SaveOwnerClass());
        this.op.editOwner(new EditOwnerClass());
        this.eof.updateOwnerListener(new UpdateOwnerClass());
        this.op.deleteOwner(new DeleteOwnerClass());
        
        this.op.addOwnerButton.addActionListener((ActionEvent e) -> {
            this.aof.setVisible(true);
        });
        
        
        displayOwners();
    }
    
    /**
     * 
     */
    void displayOwners()
    {
        //this is to load all the schedules in the database upon selecting the Event Scheduler in the menu bar
        ResultSet rs = OwnerModel.getAllOwners();
        op.employeeTable.setModel(DbUtils.resultSetToTableModel(rs));
        // this is to disable editing in the jtable
        for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
        {
            TableCellEditor ce = op.employeeTable.getDefaultEditor(c);
            if (ce instanceof DefaultCellEditor) 
            {
                ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
            }
        }
    }
    
    class SearchOwnerClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            ResultSet rs = OwnerModel.searchOwner(op.searchTF.getText());
            op.employeeTable.setModel(DbUtils.resultSetToTableModel(rs));
            // this is to disable editing in the jtable
            for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
            {
                TableCellEditor ce = op.employeeTable.getDefaultEditor(c);
                if (ce instanceof DefaultCellEditor) 
                {
                    ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
                }
            }
        }
    }
    
    
    class SaveOwnerClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            OwnerModel.saveOwner(Integer.parseInt(aof.ownerIDTF.getText()), aof.fNameTF.getText(),
                                aof.mNameTF.getText(), aof.lNameTF.getText(), aof.positionCB.getSelectedItem().toString(),
                                aof.courseCB.getSelectedItem().toString(), aof.yearCB.getSelectedItem().toString(),
                                aof.sectionCB.getSelectedItem().toString(), aof.brgyTF.getText(),
                                aof.townTF.getText(), aof.provinceTF.getText());
            displayOwners();
        }
    }
    
    class EditOwnerClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //eof.resetFields();
            int dataRow = op.employeeTable.getSelectedRow();
            if(dataRow >= 0)
            {
                int ownerID = (int) op.employeeTable.getValueAt(dataRow,0);
                oldOwnerID = Integer.toString(ownerID);
                eof.ownerIDTF.setText(""+ownerID);
                eof.setTitle("Edit Information of Owner ID: " + ownerID);
                eof.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select owner to edit.");
            }
        }
    }
    
    class UpdateOwnerClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            OwnerModel.updateOwner(oldOwnerID, Integer.parseInt(eof.ownerIDTF.getText()), eof.fNameTF.getText(),
                                eof.mNameTF.getText(), eof.lNameTF.getText(), eof.positionCB.getSelectedItem().toString(),
                                eof.courseCB.getSelectedItem().toString(), eof.yearCB.getSelectedItem().toString(),
                                eof.sectionCB.getSelectedItem().toString(), eof.brgyTF.getText(),
                                eof.townTF.getText(), eof.provinceTF.getText());
            displayOwners();
        }
    }
    
    class DeleteOwnerClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int dataRow = op.employeeTable.getSelectedRow();
            if(dataRow >= 0)
            {
                String ownerID = op.employeeTable.getValueAt(dataRow,0).toString();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to "
                                   + "Delete Owner: " + ownerID + "?","Warning",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION)
                {
                    OwnerModel.deleteOwner(ownerID);
                    displayOwners();
                }  
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select owner to delete.");
            }
        }
    }
}
