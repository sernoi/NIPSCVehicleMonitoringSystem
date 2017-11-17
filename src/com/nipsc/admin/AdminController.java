package com.nipsc.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Arrays;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellEditor;
import net.proteanit.sql.DbUtils;

public class AdminController 
{
    String oldAdminID;
    
    AdminPanel ap;
    AddAdminFrame aaf;
    EditAdminFrame eaf;
    
    public AdminController(AdminPanel ap, AddAdminFrame aaf, EditAdminFrame eaf)
    {
        this.ap = ap;
        this.aaf = aaf;
        this.eaf = eaf;
        
        this.ap.searchAdmin(new SearchAdminClass());
        this.aaf.saveAdminListener(new SaveAdminClass());
        this.ap.editAdmin(new EditAdminClass());
        this.eaf.updateAdminListener(new UpdateAdminClass());
        this.ap.deleteAdmin(new DeleteAdminClass());
        
        this.ap.addAdminBtn.addActionListener((ActionEvent e) -> {
            this.aaf.setVisible(true);
        });
        
        
        displayAdmins();
    }
    
    void displayAdmins()
    {
        //this is to load all the schedules in the database upon selecting the Event Scheduler in the menu bar
        ResultSet rs = AdminModel.getAllAdmins();
        ap.adminTable.setModel(DbUtils.resultSetToTableModel(rs));
        // this is to disable editing in the jtable
        for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
        {
            TableCellEditor ce = ap.adminTable.getDefaultEditor(c);
            if (ce instanceof DefaultCellEditor) 
            {
                ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
            }
        }
    }
    
    class SearchAdminClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            ResultSet rs = AdminModel.searchAdmin(ap.searchTF.getText());
            ap.adminTable.setModel(DbUtils.resultSetToTableModel(rs));
            // this is to disable editing in the jtable
            for (Class c: Arrays.asList(Object.class, Number.class, Boolean.class)) 
            {
                TableCellEditor ce = ap.adminTable.getDefaultEditor(c);
                if (ce instanceof DefaultCellEditor) 
                {
                    ((DefaultCellEditor) ce).setClickCountToStart(Integer.MAX_VALUE);
                }
            }
        }
    }
    
    class SaveAdminClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(aaf.jPasswordField1.getText().equals(aaf.jPasswordField2.getText()))
            {
                AdminModel.saveAdmin(aaf.adminIDTF.getText(), aaf.fNameTF.getText(),
                                aaf.mNameTF.getText(), aaf.lNameTF.getText(), aaf.positionCB.getSelectedItem().toString(),
                                aaf.jPasswordField1.getText()); 
                displayAdmins();
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Password mismatched!","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class EditAdminClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            //eaf.resetFields();
            int dataRow = ap.adminTable.getSelectedRow();
            if(dataRow >= 0)
            {
                String adminID = (String) ap.adminTable.getValueAt(dataRow,0);
                oldAdminID = adminID;
                eaf.adminIDTF.setText(""+adminID);
                eaf.setTitle("Edit Information of Admin ID: " + adminID);
                eaf.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select admin to edit.");
            }
        }
    }
    
    class UpdateAdminClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            AdminModel.updateAdmin(oldAdminID, eaf.adminIDTF.getText(), eaf.fNameTF.getText(), 
                    eaf.mNameTF.getText(), eaf.lNameTF.getText(), eaf.positionCB.getSelectedItem().toString());
            displayAdmins();
        }
    }
    
    class DeleteAdminClass implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            int dataRow = ap.adminTable.getSelectedRow();
            if(dataRow >= 0)
            {
                String adminID = ap.adminTable.getValueAt(dataRow,0).toString();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to "
                                   + "Delete Admin: " + adminID + "?","Warning",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION)
                {
                    AdminModel.deleteAdmin(adminID);
                    displayAdmins();
                }  
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select admin to delete.");
            }
        }
    }

        
}
