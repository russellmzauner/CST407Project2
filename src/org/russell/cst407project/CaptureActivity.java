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

/** 
 * CaptureActivity handles the device camera actions and storing a data set 
 * consisting of location, image path, caption/annotation.
 * <p>
 * This was initially embedded into the app, but it was becoming complication 
 * to manage all the camera/image functions manually, so I ended up calling
 * the stock camera app using an intent.
 * <p>
 * TODO Build the minimal camera functionality into a camera handler so the 
 * user doesn't have to leave the app.
 * <p>
 * Some code reused from Juliet Kemp's online tutorials.
 * http://julietkemp.com/
 * 
 * @author Russell Zauner
 * @version 0.1 120814
 *
 */

public class CaptureActivity extends Activity {
	
	private String TAG = "CallCamera";
	private String mFilepath = "default filename";
	private String mCurrentLocation;
	
	private Integer CAPTURE_IMAGE_ACTIVITY_REQ = 0;
	
	private Uri mFileUri = null;
	
	private ImageView photoImage = null;
	private Button mCallCameraButton = null;
	private Button mCallSaveButton = null;
	private TextView mTextLocation = null;
	private EditText mtextCaption = null; 
	
	private Intent mIntent = null;
	
	protected String mTimeStamp = null;
	protected File mDirectory = null;
	protected File mFullfile = null; 
	protected File mImageFile = null;
	protected Uri mPhotoUri = null;
	protected Bitmap mBitmap = null;
	protected BitmapDrawable mDrawable = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);		
		
		photoImage = (ImageView) findViewById(R.id.photo_image);
		mCallCameraButton = (Button) findViewById(R.id.button_callcamera);
		mCallSaveButton = (Button) findViewById(R.id.button_savedata);
		mTextLocation = (TextView) findViewById(R.id.text_location);
		mtextCaption = (EditText) findViewById(R.id.text_edit);
		
		// 
		// Instantiating Intent object to get location from Listener in other Activity
		//
		mIntent = getIntent();
		mCurrentLocation = mIntent.getStringExtra("currentlocation");		
		mTextLocation.setText(mCurrentLocation);
		
		mCallCameraButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				
				//
				// This tells android that we want to use and manipulate
				// an image and its file.  It adds the URI of the file to
				// its bundle.
				//
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				mFileUri = Uri.fromFile(getOutputPhotoFile());
				i.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
				
				//
				// Calling whatever responds to a camera Intent, and telling it
				// to use what we've set up above for whatever it's doing.
				//
				startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
			}
		});
		
		mCallSaveButton.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				
				saveData(mCurrentLocation, mFilepath.toString(), mtextCaption.getText().toString());
				
				
			}
		});
	}
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//
		// Handles results from calling external camera application.  Checks to make
		// sure the result is usable, then reports out on what the activity did as well
		// as show the captured and stored image.
		//
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
			if (resultCode == RESULT_OK) {
				if (data == null) {
					// A known bug here! The image should have saved in fileUri
					Toast.makeText(this, "Image saved successfully", Toast.LENGTH_LONG).show();
					mPhotoUri = mFileUri;
					} else {
						mPhotoUri = data.getData();
						Toast.makeText(this, "Image saved successfully in: " + data.getData(), Toast.LENGTH_LONG).show();
						}
				showPhoto(mPhotoUri.getPath());
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "Callout for image capture failed!", Toast.LENGTH_LONG).show();
						}
		}
	}

	
	/**
	 * Creates application image directory if none exists and stores the photo.
	 *   
	 * @return File object of image
	 */
	private File getOutputPhotoFile() {
		mDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getPackageName());
		
		//
		// Checks for directory, if no directory then creates it.
		//
		if (!mDirectory.exists()) {
			if (!mDirectory.mkdirs()) {
				Log.e(TAG, "Failed to create storage directory.");
				return null;
			}		
		}
		
		//
		// Builds filename components with timestamp.
		//
		mTimeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		
		//
		// Builds full filename with path.
		//
		mFullfile = new File(mDirectory.getPath() + File.separator + "IMG_" + mTimeStamp + ".jpg");
		
		//
		// Creates the File object from full pathname.
		//
		mFilepath = mFullfile.toString();		
		return mFullfile;
	}
	
	/**
	 *  Processes {photoUri} and puts it in the current ImageView.
	 *  
	 * @param photoUri
	 */
	private void showPhoto(String photoUri) {
		  mImageFile = new File (photoUri);
		  if (mImageFile.exists()){
		     mBitmap = BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
		     mDrawable = new BitmapDrawable(this.getResources(), mBitmap);
		     photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
		     photoImage.setImageDrawable(mDrawable);
		  }       
	}
	
	/**
	 * 
	 * @param location - user's current location 
	 * @param image_path - full path to image associated with this location
	 * @param caption - text data user wants to include with this image/location set
	 */
	public void saveData (String location, String image_path, String caption) {
		
		// 
		// Instantiate database object
		//
		DatabaseHelper db = DatabaseHelper.getInstance(this);

		// 
		// Insert data set into database. 
		//
        Log.d("Insert: ", "Inserting .."); 
        db.addPhotoCaptionContract(new PhotoCaptionContract(location, image_path, caption));
        
         
        // Reading all PhotoCaptionContracts
        // TODO Debug code REMOVE THIS IN FINAL VERSION
        Log.d("Reading: ", "Reading all PhotoCaptionContracts.."); 
        List<PhotoCaptionContract> PhotoCaptionContracts = db.getAllPhotoCaptionContracts();       
         
        for (PhotoCaptionContract cn : PhotoCaptionContracts) {
            String log = "Id: "+cn.getID()+" ,Location: " + cn.getLocation() +
            		     " ,ImagePath: " + cn.getImagePath() +
            		     " , Caption: " + cn.getCaption();
                // Writing PhotoCaptionContracts to log
        Log.d("Name: ", log);
      
    }
		
	}

}