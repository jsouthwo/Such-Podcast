package edu.taylor.cse.jsouthwo.suchpodcast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB {

	private PodcastOpenHelper dbHelper;

	private SQLiteDatabase database;

	public final static String PODCAST_ID = "_id";
	public final static String PODCAST_URL = "url";
	public final static String PODCAST_TITLE = "title";
	public final static String PODCAST_TABLE = PodcastOpenHelper.PODCAST_TABLE;

	// public final static String PODCAST_NAME = "name";

	public DB(Context context) {
		dbHelper = new PodcastOpenHelper(context);
		database = dbHelper.getWritableDatabase();
	}

	public long createRecords(String id, String url, String title) {
		ContentValues values = new ContentValues();
		values.put(PODCAST_ID, id);
		values.put(PODCAST_URL, url);
		values.put(PODCAST_TITLE, title);
		return database.insert(PODCAST_TABLE, null, values);
	}

	public Cursor selectRecords() {
		String[] cols = new String[] { PODCAST_ID, PODCAST_TITLE };
		Cursor mCursor = database.query(true, PODCAST_TABLE, cols, null, null,
				null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor; // iterate to get each value.
	}
}