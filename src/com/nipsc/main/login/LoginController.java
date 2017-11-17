package com.nipsc.main.login;

import com.nipsc.main.MainFrame;
import com.nipsc.registration.RegistrationController;
import com.nipsc.registration.RegistrationPanel;
import com.nipsc.util.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class LoginController 
{
    String adminName;
    String adminPos;
    String port;
    
    LoginFrame lf;
    
    public LoginController(LoginFrame lf, String port)
    {
        this.lf = lf;
        this.port = port;
        //this.lf.loginListener(new LoginClass());
        
        this.lf.loginBtn.addActionListener((ActionEvent e) -> {
            loginNow();
        });
        
        this.lf.pwPF.addKeyListener(new KeyListener() 
        {
        public void actionPerformed(KeyEvent evt) 
            {
                System.out.println("Handled by anonymous class listener");
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    loginNow();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
    }

    public void loginNow() 
    {
        adminPos = LoginModel.loginEmp(lf.unTF.getText(), lf.pwPF.getText());
        adminName = LoginModel.getFname();

        if(!adminPos.equals("none"))
        {
            RegistrationPanel rp = new RegistrationPanel();
            MainFrame mf = new MainFrame(lf,rp,port);
            Timer t = new Timer(mf,rp);
            new RegistrationController(rp,adminName,lf.unTF.getText(),port);
            t.setTime();
            mf.fnameLbl.setText(adminName);
            mf.setExtendedState(JFrame.MAXIMIZED_BOTH); 
            mf.setVisible(true);
            lf.setVisible(false);
            
            if(adminPos.equals("Guard"))
            {
                mf.registrationMenu.setEnabled(false);
            }
        }
    }
}
