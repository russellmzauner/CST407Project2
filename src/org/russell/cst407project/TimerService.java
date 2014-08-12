package org.russell.cst407project;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

// code reuse from http://xjaphx.wordpress.com/2012/07/07/create-a-service-that-does-a-schedule-task/
// service never stopped but I fixed it.  Most code at websites is broken in some way.  I guess one
// way to learn is to fix broken stuff.  Some might argue it's the best way.


public class TimerService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
 
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    @Override
    public void onCreate() {
        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }
 
    class TimeDisplayTimerTask extends TimerTask {
    	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
 
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
 
                @Override
                public void run() {
                    // display toast
                    Toast.makeText(getApplicationContext(), getDateTime(),
                            Toast.LENGTH_LONG).show();
                    // vibrate phone
                	
                	
        			v.vibrate(300);
                }
 
            });
        }
 
        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }
        
        public class VibratingToast extends Toast {

    		public VibratingToast(Context context, String date, int duration) {
    			super(context);
    			Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    			v.vibrate(300);
    			super.makeText(context, date, duration).show();
    		}
        }
    }
    
    // The sample code would not stop the service when the application was
    // exited, so I had to add the right calls to MainActivity and 
    // TimerService onDestroy() methods.  I believe the one in MainActivity 
    // calls the TimerService object to execute its 
    
    @Override 
    public void onDestroy() {
    	super.onDestroy();
    	mTimer.cancel();    	
    }
 
    
}