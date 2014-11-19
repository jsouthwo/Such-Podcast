package edu.taylor.cse.jsouthwo.suchpodcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PodcastOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "blackeye";
    public static final String PODCAST_TABLE = "podcast";
    private static final String PODCAST_TABLE_CREATE = "CREATE TABLE" + PODCAST_TABLE +
    		"( " +
	    		"_id INTEGER PRIMARY_KEY, " +
	    		"url TEXT NOT_NULL" +
	    		"title TEXT NOT_NULL" +
    		");";



    PodcastOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	public PodcastOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public PodcastOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PODCAST_TABLE_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
    	Log.e("SQL", "You don't want to do this.");
//        Log.w(PodcastOpenHelper.class.getName(),
//                         "Upgrading database from version " + oldVersion + " to "
//                         + newVersion + ", which will destroy all old data");
//        database.execSQL("DROP TABLE IF EXISTS *");
//        onCreate(database);
    }
}
