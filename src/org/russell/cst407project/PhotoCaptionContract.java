package org.russell.cst407project;

//code reused from ravi tamada  http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

public class PhotoCaptionContract {
    
    //private variables
    int _id;
    String _location;
    String _image_path;
    String _caption;
     
    // Empty constructor
    public PhotoCaptionContract(){
         
    }
    // constructor
    public PhotoCaptionContract(int id, String location, String image_path, String caption){
        this._id = id;
        this._location = location;
        this._image_path = image_path;
        this._caption = caption;
    }
     
    // constructor
    public PhotoCaptionContract(String location, String image_path, String caption){
        this._location = location;
        this._image_path = image_path;
        this._caption = caption;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting location
    public String getLocation(){
        return this._location;
    }
     
    // setting location
    public void setLocation(String location){
        this._location = location;
    }
     
    // getting image path
    public String getImagePath(){
        return this._image_path;
    }
     
    // setting image path
    public void setImagePath(String image_path){
        this._image_path = image_path;
    }
    
    // getting caption
    public String getCaption(){
        return this._caption;
    }
     
    // setting image path
    public void setCaption(String caption){
        this._caption = caption;
    }
}