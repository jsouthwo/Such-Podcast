package edu.taylor.cse.jsouthwo.suchpodcast;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 **/

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";
 
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "blackeye";

    // Table Names
    private static final String TABLE_PODCAST = "podcast";
    private static final String TABLE_EPISODE = "episode";
 
    // Common column names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String KEY_CREATED_AT = "created_at";
 
    // PODCAST Table - column names
    // TODO: Hopefully I can get by without doing this.
//    private static final String KEY_EPISODE_IDS = "episodes";
 
    // EPISODE Table - column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FILENAME = "filename";
 
    /** Table Create Statements **/
    // PODCAST table create statement
    private static final String CREATE_TABLE_PODCAST = "CREATE TABLE" + TABLE_PODCAST +
    		"( " +
	    		KEY_ID + " INTEGER PRIMARY_KEY, " +
	    		KEY_URL + " TEXT " +
	    		KEY_TITLE + " TEXT " +
	    		KEY_CREATED_AT + "DATETIME" + 
//	    		KEY_EPISODE_IDS + "LIST" + 
    		");";

    // EPISODE table create statement
    private static final String CREATE_TABLE_EPISODE = "CREATE TABLE" + TABLE_EPISODE +
    		"( " +
	    		KEY_ID + " INTEGER PRIMARY_KEY, " +
	    		KEY_URL + " TEXT " +
	    		KEY_TITLE + " TEXT " +
	    		KEY_CREATED_AT + "DATETIME" + 
	    		KEY_FILENAME + "TEXT" +
	    		KEY_DESCRIPTION + "TEXT" +
    		");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	public DatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PODCAST);
        db.execSQL(CREATE_TABLE_EPISODE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS *");
        onCreate(db);
    }
}
