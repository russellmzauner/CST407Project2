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

public class DisplayActivity extends Activity {
	
	Uri fileUri = null;
	ImageView savedImage = null;
	TextView textSavedLocation = null;
	TextView textSavedCaption = null;
	Button getDataButton = null;
	
	
	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);	        
	        setContentView(R.layout.activity_display);
	        
	        savedImage = (ImageView) findViewById(R.id.saved_image);
	        getDataButton = (Button) findViewById(R.id.button_getdata);
	        textSavedLocation = (TextView) findViewById(R.id.saved_location);
	        textSavedCaption = (TextView) findViewById(R.id.saved_textview);
	        
	        
	        
	        
			getDataButton.setOnClickListener( new View.OnClickListener() {
				public void onClick(View view) {
					
					dbLoaderTest();	
					
				}
			});
	  }
	  
	  
	  public void dbLoaderTest (){
		  DatabaseHelper db = DatabaseHelper.getInstance(this);
		  
		
		Log.d("Retrieve: ", "Retrieving ..");
		  
		  // retrieve last db entry
		  int lastEntry = db.getPhotoCaptionContractsCount();
		  //adjust for index if not first one
		  
		  PhotoCaptionContract singleRow = db.getPhotoCaptionContract(lastEntry);
		  String location = singleRow.getLocation();
		  String imagePath = singleRow.getImagePath();
		  String caption = singleRow.getCaption();
		  // display the data
		  displayData(location, imagePath, caption);
		
	  }
		  
	  
	  public void displayData (String location, String image_path, String caption){
		  
		  savedImage.setImageURI(Uri.parse(new File(image_path).toString()));
		  textSavedLocation.setText(location.toString());
		  textSavedCaption.setText(caption.toString());
		  
		  
	  }

	  
}
