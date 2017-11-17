package com.nipsc.util;

import com.nipsc.main.MainFrame;
import com.nipsc.registration.RegistrationPanel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer{
    MainFrame mf;
    RegistrationPanel rp;
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    DateFormat tf = new SimpleDateFormat("hh:mm:ss a");
    
    public Timer(MainFrame mf, RegistrationPanel rp)
    {
        this.mf = mf;
        this.rp = rp;
    }

    public void setTime()
    {  
        Worker worker = new Worker();
        worker.start();
    }
    
    class Worker extends Thread 
    {
        @Override
        public void run() {
            while(true)
            {
                Date dateobj = new Date();
                mf.dateLabel.setText(df.format(dateobj));
                mf.realTimeLabel.setText(tf.format(dateobj));
                rp.timeLbl.setText(tf.format(dateobj));
                
                // Sleep for a while
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Interrupted exception will occur if
                    // the Worker object's interrupt() method
                    // is called. interrupt() is inherited
                    // from the Thread class.
                    break;
                }
            }
        }
    }
}
