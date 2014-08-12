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

/** 
 * TimerService defines a scheduled timer service to remind the user 
 * to snap a picture.  This code file is *extremely* messy.
 * <p>
 * TODO Finish extracting and wiring up the VibratingToast method.
 * <br>
 * TODO Finish organizing and cleaning this whole file up, it's a mess.
 * Inline documentation and refactoring is really needed here.
 * <br>
 * TODO Have the timer interval and vibration duration point towards user settings.
 * <p>
 * Some code reused from Pete Houston's online tutorial.
 * http://xjaphx.wordpress.com/
 * 
 * @author Russell Zauner
 * @version 0.1 120814
 *
 */

public class TimerService extends Service {
    // Timer interval
	public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
 
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
 
    // timer/vibrator handling
    private Timer mTimer = null;
    Vibrator mVibrator = null;
 
    @Override
    public IBinder onBind(Intent intent) {
    	return null;
    }
 
    @Override
    public void onCreate() {
    	//
        // cancel if already existed, takes care of accidental multiple calls
    	// to start/restart service.
    	//
        if(mTimer != null) {
        	mTimer.cancel();
       	} else {
       		mTimer = new Timer();
       	}
        //
        // Attach the task to the timer.
        //
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }
 
    //
    // Going through this code, I'm sort of confused at how I got this to work
    // properly, it's certainly not clean and perhaps I need to pull this
    // out into its own file named TimeDisplayTimerTask.java.  It seems to 
    // function coexisting in the same file with the Service, but Javadocs is 
    // giving me cues that perhaps this isn't according to good style conventions.
    //
    /**
     * This class maybe should be a method - but I'm not sure.  I would try and fix it
     * but I don't want to break my code in the 11th hour.
     * <p>
     * TODO refactor and clean up.
     * 
     * @author rmzauner
     *
     */
    class TimeDisplayTimerTask extends TimerTask {
    	//
    	// Instantiating an object to let us use the vibrator.  This probably belongs somewhere else
    	// but it's working here for now.
    	//
    	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
 
        @Override
        public void run() {        	
        	//
            // setting this up to run on another thread.  I forgot why this was happening.
        	//
            mHandler.post(new Runnable() {
            	
                @Override
                public void run() {
                	vibratingToast();
                }                
                //
                // this method adds a vibration to the Toast.
                //
				private void vibratingToast() {
					Toast.makeText(getApplicationContext(), getDateTime(), Toast.LENGTH_LONG).show();
                    //
                    // call to vibrator - 
                    //
        			v.vibrate(300);
				} 
            });
        }
        
        /**
         * Creates a formatted time message.  
         * <p>
         * TODO This seems risky to me - I think it should format the object then return the
         * finished object.  Working for now...
         * 
         * @return Formatted date object
         */
 
        private String getDateTime() {            
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            //
            // There's been some weirdness with performing any calls concurrent 
            // with returning an object.  Not sure if that's only the DB calls, but
            // bears investigation to see if it's case by case or generally
            // poor style.
            //
            return sdf.format(new Date());
        }
        
        /**
         * I don't think this class is currently being instantiated, as I've discovered it
         * doesn't seem to be properly wired up to the rest of the code.
         * <p>
         * TODO Properly extract vibrating toast method/class
         * 
         * @author rmzauner
         *
         */
        public class VibratingToast extends Toast {
    		public VibratingToast(Context context, String date, int duration) {
    			super(context);
    			mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    			v.vibrate(300);
    			super.makeText(context, date, duration).show();
    		}
        }
    }
    
    //
    // The sample code would not stop the service when the application was
    // exited, so I had to add the right calls to MainActivity and 
    // TimerService onDestroy() methods.  I believe the one in MainActivity 
    // calls the TimerService object to execute its own onDestroy method. 
    //
    @Override 
    public void onDestroy() {
    	super.onDestroy();
    	mTimer.cancel();    	
    }    
}