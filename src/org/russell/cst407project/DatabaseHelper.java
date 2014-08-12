package org.russell.cst407project;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** 
 * DatabaseHelper handles SQLite calls.
 * <p>
 * TODO Clean up and harden CRUD methods.  Some of this done already, but more to do.
 * <br>
 * TODO Separate out schema into abstract methods, maybe in the contract.  This would allow user
 * modifiable annotation fields - note to self, study on dynamic data structure management.
 * <br>
 * TODO Review member variables - a lot of this seems awkward and/or fragile. 
 * <br>
 * TODO Compose proper JavaDocs for methods and fix inline documentation style consistency.
 * <p>
 * Some code reused from Ravi Tamada's online tutorial.
 * https://plus.google.com/+RaviTamada/about
 * 
 * @author Russell Zauner
 * @version 0.1 120814
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "database";
 
    // PhotoCaptionContracts table name
    private static final String TABLE_NAME = "dataset";
 
    // PhotoCaptionContracts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_CAPTION = "caption";
 
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + 
                               TABLE_NAME + "(" +
                               KEY_ID + " INTEGER PRIMARY KEY," + 
        		               KEY_LOCATION + " TEXT,"  + 
                               KEY_IMAGE_PATH + " TEXT, " + 
        		               KEY_CAPTION + " TEXT " + ")";
        
        db.execSQL(CREATE_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new PhotoCaptionContract
    void addPhotoCaptionContract(PhotoCaptionContract PhotoCaptionContract) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION, PhotoCaptionContract.getLocation()); // PhotoCaptionContract Location
        values.put(KEY_IMAGE_PATH, PhotoCaptionContract.getImagePath()); // PhotoCaptionContract ImagePath
        values.put(KEY_CAPTION, PhotoCaptionContract.getCaption()); // PhotoCaptionContract Caption
 
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single PhotoCaptionContract
    PhotoCaptionContract getPhotoCaptionContract(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                KEY_LOCATION, KEY_IMAGE_PATH, KEY_CAPTION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        PhotoCaptionContract PhotoCaptionContract = new PhotoCaptionContract(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return PhotoCaptionContract
        return PhotoCaptionContract;
    }
     
    // Getting All PhotoCaptionContracts
    public List<PhotoCaptionContract> getAllPhotoCaptionContracts() {
        List<PhotoCaptionContract> PhotoCaptionContractList = new ArrayList<PhotoCaptionContract>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PhotoCaptionContract PhotoCaptionContract = new PhotoCaptionContract();
                PhotoCaptionContract.setID(Integer.parseInt(cursor.getString(0)));
                PhotoCaptionContract.setLocation(cursor.getString(1));
                PhotoCaptionContract.setImagePath(cursor.getString(2));
                PhotoCaptionContract.setCaption(cursor.getString(3));
                // Adding PhotoCaptionContract to list
                PhotoCaptionContractList.add(PhotoCaptionContract);
            } while (cursor.moveToNext());
        }
 
        // return PhotoCaptionContract list
        return PhotoCaptionContractList;
    }
 
    // Updating single PhotoCaptionContract
    public int updatePhotoCaptionContract(PhotoCaptionContract PhotoCaptionContract) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_LOCATION, PhotoCaptionContract.getLocation());
        values.put(KEY_IMAGE_PATH, PhotoCaptionContract.getImagePath());
        values.put(KEY_CAPTION, PhotoCaptionContract.getCaption()); 
        
        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(PhotoCaptionContract.getID()) });
    }
 
    // Deleting single PhotoCaptionContract
    public void deletePhotoCaptionContract(PhotoCaptionContract PhotoCaptionContract) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(PhotoCaptionContract.getID()) });
        db.close();
    }
 
    //
    // Getting PhotoCaptionContracts Count
    // had to fix a bug in this where getting the count was 
    // closing via the getCount then trying to return a closed cursor
    //
    public int getPhotoCaptionContractsCount() {
    	
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
 
        // return count
        return count;
    }
    
    //
    // had to extend the helper here to make it available to all activities
    // this doesn't do the job but makes it so we can't access the constructor
    // directly...
    //
    private static DatabaseHelper sInstance;
    
    public static DatabaseHelper getInstance(Context context) {
    	
    	if (sInstance == null) {
    		sInstance = new DatabaseHelper (context.getApplicationContext());
    	}
    	return sInstance;    	
    }    
}