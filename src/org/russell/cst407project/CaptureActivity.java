package org.russell.cst407project;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


 // remember to credit Juliet Kemp http://www.linux.com/learn/tutorials/722038-android-calling-the-camera


public class CaptureActivity extends Activity {
	
	private static final String TAG = "CallCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
	public static  String filepath = "default name";
	
	

	Uri fileUri = null;
	ImageView photoImage = null;

	private File getOutputPhotoFile() {

		  File directory = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES), getPackageName());

		  if (!directory.exists()) {
		    if (!directory.mkdirs()) {
		      Log.e(TAG, "Failed to create storage directory.");
		      return null;
		    }
		  }

		  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		  
		  File fullfile = new File(directory.getPath() + File.separator + "IMG_"  
                  + timeStamp + ".jpg");
		  
		  filepath = fullfile.toString();
		  

		  return fullfile;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);

		
		
		photoImage = (ImageView) findViewById(R.id.photo_image);

		Button callCameraButton = (Button) findViewById(R.id.button_callcamera);
		Button callSaveButton = (Button) findViewById(R.id.button_savedata);
		final TextView textLocation = (TextView) findViewById(R.id.text_location);

		final EditText textCaption = (EditText) findViewById(R.id.text_edit);
		
		Intent intent = getIntent();
		final String currentLocation = intent.getStringExtra("currentlocation");
		Log.d("Capture Acvivity Location", currentLocation);
		textLocation.setText(currentLocation);

		callCameraButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = Uri.fromFile(getOutputPhotoFile());
				i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
			}
		});
		
		callSaveButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				
				saveData(currentLocation, filepath.toString(), textCaption.getText().toString());
				
				
			}
		});
	}
	
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		    if (resultCode == RESULT_OK) {
		      Uri photoUri = null;
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        Toast.makeText(this, "Image saved successfully", 
		                       Toast.LENGTH_LONG).show();
		        photoUri = fileUri;
		      } else {
		        photoUri = data.getData();
		        Toast.makeText(this, "Image saved successfully in: " + data.getData(), 
		                       Toast.LENGTH_LONG).show();
		      }
		      showPhoto(photoUri.getPath());
		    } else if (resultCode == RESULT_CANCELED) {
		      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		    } else {
		      Toast.makeText(this, "Callout for image capture failed!", 
		                     Toast.LENGTH_LONG).show();
		    }
		  }
	}

	private void showPhoto(String photoUri) {
		  File imageFile = new File (photoUri);
		  if (imageFile.exists()){
		     Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		     BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
		     photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
		     photoImage.setImageDrawable(drawable);
		  }       
	}
	
	public void saveData (String location, String image_path, String caption) {
		// db object creation
				DatabaseHelper db = DatabaseHelper.getInstance(this);
		
		/**
         * CRUD Operations
         * */
        // Inserting PhotoCaptionContracts
        Log.d("Insert: ", "Inserting .."); 
        db.addPhotoCaptionContract(new PhotoCaptionContract(location, image_path, caption));
        
         
        // Reading all PhotoCaptionContracts
        Log.d("Reading: ", "Reading all PhotoCaptionContracts.."); 
        List<PhotoCaptionContract> PhotoCaptionContracts = db.getAllPhotoCaptionContracts();       
         
        for (PhotoCaptionContract cn : PhotoCaptionContracts) {
            String log = "Id: "+cn.getID()+" ,Location: " + cn.getLocation() +
            		     " ,ImagePath: " + cn.getImagePath() +
            		     " , Caption: " + cn.getCaption();
                // Writing PhotoCaptionContracts to log
        Log.d("Name: ", log);
        
        // read a single 
        
      
    }
		
	}
	

}