package edu.taylor.cse.jsouthwo.suchpodcast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Singleton implementation
	private static DatabaseHelper helper;

    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "blackeye";

    // Table Names
    private static final String TABLE_PODCAST = "Podcast";
    private static final String TABLE_EPISODE = "Episode";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";

    // PODCAST Table - column names
    // TODO: Hopefully I can get by without doing this.
//    private static final String KEY_EPISODE_IDS = "episodes";

    // EPISODE Table - column names
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_FILENAME = "filename";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_PODCAST = "podcast";
    private static final String KEY_POSITION = "current_position";
    private static final String KEY_PLAYED = "played";

    /** Table Create Statements **/
    // PODCAST table create statement
    private static final String CREATE_TABLE_PODCAST = "CREATE TABLE " + TABLE_PODCAST +
    		" (" +
	    		KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
	    		KEY_URL + " TEXT, " +
	    		KEY_TITLE + " TEXT" +
    		");";

    // EPISODE table create statement
    private static final String CREATE_TABLE_EPISODE = "CREATE TABLE " + TABLE_EPISODE +
    		" (" +
	    		KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
	    		KEY_URL + " TEXT, " +
	    		KEY_TITLE + " TEXT, " +
	    		KEY_CREATED_AT + " DATETIME, " + 
	    		KEY_FILENAME + " TEXT, " +
	    		KEY_DESCRIPTION + " TEXT, " +
	    		KEY_PODCAST + " TEXT, " +
	    		KEY_POSITION + " REAL, " +
	    		KEY_PLAYED + " INTEGER" +
    		");";

    public static DatabaseHelper getHelper(Context context) {
    	// Use the application context, which will ensure that you 
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (helper == null) {
          helper = new DatabaseHelper(context.getApplicationContext());
        }
        return helper;
      }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        values.put(KEY_TITLE, podcast.getTitle());

        // insert row
        long podcast_id = db.insert(TABLE_PODCAST, null, values);

        return podcast_id;
    }

    /*
     * Creating an episode
     */
    public long createEpisode(RssItem episode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
		values.put(KEY_URL, episode.getUrl());
		values.put(KEY_TITLE, episode.getTitle());
		values.put(KEY_CREATED_AT, episode.getCreatedAt());
		values.put(KEY_FILENAME, episode.getFilename());
		values.put(KEY_DESCRIPTION, episode.getDescription());
		values.put(KEY_PODCAST, episode.getPodcast());
		values.put(KEY_POSITION, episode.getCurrentPosition());
		values.put(KEY_PLAYED, episode.getPlayed());

        // insert row
        long episode_id = db.insert(TABLE_EPISODE, null, values);

        return episode_id;
    }

    /*
     * get single Podcast
     */
    public Podcast getPodcast(long podcast_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PODCAST + " WHERE "
                + KEY_ID + " = '" + podcast_id + "';";

        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Podcast podcast = new Podcast();
        podcast.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        podcast.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        podcast.setUrl(c.getString(c.getColumnIndex(KEY_URL)));

        return podcast;
    }
    /*
     * get single Episode (RssItem)
     */
    public RssItem getEpisode(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EPISODE + " WHERE "
                + KEY_TITLE + " = '" + title + "' LIMIT 1;";

        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        RssItem episode = new RssItem();
        episode.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        episode.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        episode.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
        episode.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        episode.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
        episode.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        episode.setCurrentPosition(c.getFloat(c.getColumnIndex(KEY_POSITION)));
        episode.setPlayed(c.getInt(c.getColumnIndex(KEY_PLAYED)));

        return episode;
    }

    /*
     * get single Episode (RssItem)
     */
    public RssItem getEpisode(long episode_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_EPISODE + " WHERE "
                + KEY_ID + " = '" + episode_id + "';";

        Log.i(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        RssItem episode = new RssItem();
        episode.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        episode.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
        episode.setFilename(c.getString(c.getColumnIndex(KEY_FILENAME)));
        episode.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        episode.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
        episode.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        episode.setCurrentPosition(c.getFloat(c.getColumnIndex(KEY_POSITION)));
        episode.setPlayed(c.getInt(c.getColumnIndex(KEY_PLAYED)));

        return episode;
    }

    /*
     * getting all podcasts
     */
    public List<Podcast> getAllPodcasts() {
        List<Podcast> podcasts = new ArrayList<Podcast>();
        String selectQuery = "SELECT * FROM " + TABLE_PODCAST + " ORDER BY " + KEY_TITLE + ";";

//        String selectQuery = "SELECT * FROM " + TABLE_PODCAST + ";";
        Log.i(LOG, selectQuery);

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
     * getting episodes from podcast
     */
    public List<RssItem> getEpisodes(String podcastRssItemName) {
        List<RssItem> episodes = new ArrayList<RssItem>();
        String selectQuery = "SELECT * FROM " + TABLE_EPISODE + 
        		" WHERE " +
        		KEY_PODCAST + " = '" + podcastRssItemName + "';";

        Log.i(LOG, selectQuery);

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
                episode.setPodcast(c.getString(c.getColumnIndex(KEY_PODCAST)));
                episode.setCurrentPosition(c.getFloat(c.getColumnIndex(KEY_POSITION)));
                episode.setPlayed(c.getInt(c.getColumnIndex(KEY_PLAYED)));

                // adding to podcast list
                episodes.add(episode);
            } while (c.moveToNext());
        }

        return episodes;
    }

    /** 
     ** NOTE: Skipping update podcast. It's unnecessary for now.
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
		values.put(KEY_PODCAST, episode.getPodcast());
		values.put(KEY_POSITION, episode.getCurrentPosition());
		values.put(KEY_PLAYED, episode.getPlayed());

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
