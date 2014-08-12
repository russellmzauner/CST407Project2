package org.russell.cst407project;

import android.app.Activity;
import android.os.Bundle;

/**
 * SettingsActivity will hold user customizable app settings and 
 * other configuration/persistent data of interest that doesn't fit 
 * elsewhere.
 * <p>
 * TODO <br>
 * Add: Initial zoom level<br>
 * Add: User settable timer interval<br>
 *  
 * @author Russell Zauner
 * @version 0.1 120814
 */


public class SettingsActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_settings);
	}

}


