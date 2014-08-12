package org.russell.cst407project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/** 
 * MainActivity handles geolocation and map display.
 * 
 * It looks like the GoogleMap class has some methods that may use fewer 
 * programmatic steps to perform the same function.  I will continue
 * to explore them, but this works.
 * 
 * @author Russell Zauner
 * @version 0.1 120724
 *
 */

public class MainActivity extends Activity implements LocationListener, Listener {
	
	private GoogleMap mMap = null;
	private LocationManager mLocManager = null;
	private String mMarkerTitle = null;
	private MarkerOptions mMarker = null;
	private Intent timerService = null;
	
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        // start service
        timerService = new Intent(this, TimerService.class);
        startService(timerService); 
        
        //
        // This instantiates a GoogleMaps object.  The map can be displayed purely from
        // barebones XML, but we need the object itself in order to get/set attributes
        // and values.
        //
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap(); 
        
        //
        // Makes sure we have a valid object to work with.
        //
        setUpMapIfNeeded();
        
        //
        // Instantiate location services and marker objects.
        //
        mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mMarker = new MarkerOptions();
        
        //
        // These two are actually superfluous to the assignment.  I've left them in because
        // they're useful built-in methods and also fun (so I want to keep them visible in
        // my code, which I'll certainly refer back to).  They set the automatic default marker
        // from the API and change the map type to show some photo detail, respectively.
        //
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);       
        
    }
   
    // 
    // This is sample/test code directly from the Google Developers site.  It makes sure we
    // have an object to safely manipulate.
    //
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }
    
    //
    // This is partially code from Nick Ferraro's lecture demonstration.
    //
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	// 
    	// Google Developers site says we need to check this again in onResume, 
    	// to make sure we're always working with a valid object.
    	//
    	setUpMapIfNeeded();
    	
    	//
    	// These enable the LocationManager object to get GPS data and get notified of
    	// any change in position.  When the app starts, there is no position so any 
    	// position is a changed position.  Right now, once the application is launched, 
    	// I'd expect it will only update location after onRestart is called since I 
    	// haven't added any other logic.
    	//
		mLocManager.addGpsStatusListener(this);
		mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    //
    // This is partially code from Nick Ferraro's lecture demonstration.
    //
    @Override
	public void onPause() {
		super.onPause();
		
		//
		// We should remove references to services if we're not going to use them
		// for a while.
		//
		mLocManager.removeUpdates(this);
		mLocManager.removeGpsStatusListener(this);
	}
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	// Stop service - I believe stopService calls the onDestroy method in 
    	// TimerService, as this command here was not enough to keep the 
    	// service from continuing to run after the application was exited.
        stopService(timerService); 
        
    }
    
    @Override
	public void onLocationChanged(Location location) {
    	
    	//
    	// Get rid of old map data.
    	//
    	mMap.clear();    	
    	
    	for (Integer index = 0;index <3; ++index){    		
    	
    	// 
    	// Build title from our current location.
    	//
		mMarkerTitle = "Lat: "+ (location.getLatitude() + index) + " / Long: " + (location.getLongitude() + index);
		
		//
		// Tell the marker where it's at (our location).
		//
		mMarker.position(new LatLng(location.getLatitude()+index, location.getLongitude()+index)).title(mMarkerTitle);
		
		//
		// Stick it on the map.
		//
		mMap.addMarker(mMarker);
    	}
		// 
		// Move the map camera view to where the pin is and zoom it up a bit.
		//
		mMap.animateCamera(CameraUpdateFactory
						   .newLatLngZoom(new LatLng(location.getLatitude(),
													 location.getLongitude()),
   													 4));		
		
		
		
	}

	@Override
	public void onGpsStatusChanged(int event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// 
	// Menu stuff goes below.

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {

		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;

		case R.id.action_takepicture:
			
			Intent intent = new Intent("org.russell.cst407project.CURRENT_LOCATION");
			String currentLoc = String.valueOf(mMarkerTitle);
			intent.putExtra("currentlocation", currentLoc);
			startActivity(intent);
			return true;

		case R.id.action_viewpicture:
			startActivity(new Intent(this, DisplayActivity.class));
			return true;

		default:
			return false;				
				
			}
		
	}

}
