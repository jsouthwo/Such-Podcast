package edu.taylor.cse.jsouthwo.suchpodcast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
 
    // PODCAST Table - column names
    // TODO: Hopefully I can get by without doing this.
//    private static final String KEY_EPISODE_IDS = "episodes";
 
    // EPISODE Table - column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_CREATED_AT = "created_at";
 
    /** Table Create Statements **/
    // PODCAST table create statement
    private static final String CREATE_TABLE_PODCAST = "CREATE TABLE" + TABLE_PODCAST +
    		"( " +
	    		KEY_ID + " INTEGER PRIMARY_KEY, " +
	    		KEY_URL + " TEXT " +
	    		KEY_TITLE + " TEXT " +
//	    		KEY_CREATED_AT + "DATETIME" + 
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
    
    /**************************** CRUD Ops *****************************/
    
    /*
     * Creating a podcast
     */
    public long createPodcast(Podcast podcast) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_URL, podcast.getUrl());
        values.put(KEY_TITLE,  podcast.getTitle());
//        values.put(KEY_CREATED_AT, getDateTime());
     
        // insert row
        long podcast_id = db.insert(TABLE_PODCAST, null, values);

        /* Keep in case you need it later.
        // assigning tags to todo
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }
        */
     
        return podcast_id;
    }
    
    /*
     * Creating an episode
     */
    public long createEpisode(RssItem episode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, episode.getId());
		values.put(KEY_URL, episode.getUrl());
		values.put(KEY_TITLE, episode.getTitle());
		values.put(KEY_CREATED_AT, episode.getCreatedAt());
		values.put(KEY_FILENAME, episode.getFilename());
		values.put(KEY_DESCRIPTION, episode.getDescription());
     
        // insert row
        long podcast_id = db.insert(TABLE_PODCAST, null, values);

        /* Keep in case you need it later.
        // assigning tags to todo
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }
        */
     
        return podcast_id;
    }

    /*
     * get single Podcast
     */
    public Podcast getPodcast(long podcast_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PODCAST + " WHERE "
                + KEY_ID + " = " + podcast_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Podcast podcast = new Podcast();
        podcast.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        podcast.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        podcast.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
//        podcast.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        
        /*
        podcast.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        podcast.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
        podcast.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        */
     
        return podcast;
    }

    /*
     * get single Episode (RssItem)
     */
    public RssItem getEpisode(long episode_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EPISODE + " WHERE "
                + KEY_ID + " = " + episode_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        
        RssItem episode = new RssItem();
        episode.setCreatedAt(c.getString(c.getColumnIndex(KEY_ID)));
        episode.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        episode.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
        episode.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        episode.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
        episode.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
     
        return episode;
    }
    
    /*
     * getting all podcasts
     * */
    public List<Podcast> getAllPodcasts() {
        List<Podcast> podcasts = new ArrayList<Podcast>();
        String selectQuery = "SELECT  * FROM " + TABLE_PODCAST;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Podcast podcast = new Podcast();
                podcast.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                podcast.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                podcast.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
     
                // adding to podcast list
                podcasts.add(podcast);
            } while (c.moveToNext());
        }

        return podcasts;
    }

    /*
     * getting all episodes
     * */
    public List<RssItem> getAllEpisodes() {
        List<RssItem> episodes = new ArrayList<RssItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_EPISODE;
     
        Log.e(LOG, selectQuery);
     
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                RssItem episode = new RssItem();
                episode.setCreatedAt(c.getString(c.getColumnIndex(KEY_ID)));
                episode.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                episode.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
                episode.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                episode.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
                episode.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
     
                // adding to podcast list
                episodes.add(episode);
            } while (c.moveToNext());
        }

        return episodes;
    }
    
    /** TODO: Skipping "Fetching all Todos under a Tag name"
     ** 	  as though we only have one podcast.... hmm.
     **
     **		  And update podcast. It's unnecessary...
     **/

    /*
     * Updating an episode
     */
    public int updateEpisode(RssItem episode) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_ID, episode.getId());
		values.put(KEY_URL, episode.getUrl());
		values.put(KEY_TITLE, episode.getTitle());
		values.put(KEY_CREATED_AT, episode.getCreatedAt());
		values.put(KEY_FILENAME, episode.getFilename());
		values.put(KEY_DESCRIPTION, episode.getDescription());
     
        // updating row
        return db.update(TABLE_EPISODE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(episode.getId()) });
    }

    /*
     * Deleting a podcast
     */
    public void deletePodcast(long podcast_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PODCAST, KEY_ID + " = ?",
                new String[] { String.valueOf(podcast_id) });
    }

    /*
     * Deleting a episode
     */
    public void deleteEpisode(long episode_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EPISODE, KEY_ID + " = ?",
                new String[] { String.valueOf(episode_id) });
    }
    
    
    /**************************** Random *****************************/

	/*
	 * Always close database
	 */
	public void closeDB() {
	    SQLiteDatabase db = this.getReadableDatabase();
	    if (db != null && db.isOpen())
	        db.close();
	}

    /**
     * get datetime
     **/
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
