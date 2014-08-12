package org.russell.cst407project;

import java.io.File;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/** 
* DisplayActivity handles loading up an entry from db into the current View.
* <p>
* TODO Clean up comments style consistency, as well as review placement
* of member variables
* <br>
* TODO Add menu for various operations, including a ListView of current Table
* 
* 
* @author Russell Zauner
* @version 0.1 120814
*
*/

public class DisplayActivity extends Activity {
	
	private ImageView msavedImage = null;
	private TextView mtextSavedLocation = null;
	private TextView mtextSavedCaption = null;
	private Button mgetDataButton = null;
	private Integer mLastEntry = null;
	private String mLocation = null;
	private String mImagePath = null; 
	private String mCaption = null; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		msavedImage = (ImageView) findViewById(R.id.saved_image);
		mgetDataButton = (Button) findViewById(R.id.button_getdata);
		mtextSavedLocation = (TextView) findViewById(R.id.saved_location);
		mtextSavedCaption = (TextView) findViewById(R.id.saved_textview);
		
		mgetDataButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				dbDataLoader();
			}
		});		
	}	  
	
	/**
	 * Performs db operation requested by user.
	 * 
	 */
	public void dbDataLoader(){	
		DatabaseHelper db = DatabaseHelper.getInstance(this);
		Log.d("Retrieve: ", "Retrieving ..");
		
		// retrieve last db entry
		mLastEntry = db.getPhotoCaptionContractsCount();
		
		// Instantiates a data set object and calls the db for the last
		// data set stored.
		PhotoCaptionContract singleRow = db.getPhotoCaptionContract(mLastEntry);
		mLocation = singleRow.getLocation();
		mImagePath = singleRow.getImagePath();
		mCaption = singleRow.getCaption();
		  
		// calls the single data set display method
		displayData(mLocation, mImagePath, mCaption);
	}
	
	/**
	 * Populates View with single data set.
	 * 
	 * @param location - location associated with image
	 * @param image_path - image path to image associated with location
	 * @param caption - text data user stored with the image/location set
	 * 
	 */
	  
	public void displayData (String location, String image_path, String caption){
		msavedImage.setImageURI(Uri.parse(new File(image_path).toString()));
		mtextSavedLocation.setText(location.toString());
		mtextSavedCaption.setText(caption.toString());
	}	  
}
