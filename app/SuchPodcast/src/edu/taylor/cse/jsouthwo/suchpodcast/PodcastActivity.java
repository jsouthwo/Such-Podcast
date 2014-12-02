package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.ArrayList;
import android.support.v7.app.ActionBarActivity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class PodcastActivity extends ActionBarActivity {
	public static ListView mList;
    public static ArrayAdapter<String> adapter;
    public static ArrayList<Podcast> podcastList = new ArrayList<Podcast>();
    public static boolean inEpisodeDisplay = false;
    public static Podcast currentDisplayedPodcast;
    private static DatabaseHelper helper;
    private static SQLiteDatabase db;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);

        helper = DatabaseHelper.getHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        mList = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);

        ImageButton addLink = (ImageButton)findViewById(R.id.add_link);
        addLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
			    startActivity( intent );
			}
		});

        ImageButton downloadButton = (ImageButton)findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Downloading all new podcasts.", Toast.LENGTH_LONG).show();

				performDownloads();
			}
		});

	}

	public void performDownloads() {
		int episodeCounter = 0;
		for ( Podcast eachPodcast : podcastList ) {
			episodeCounter = 0;
			for ( RssItem eachEpisode : eachPodcast.getEpisodeList() ) {
				if ( episodeCounter == 5 ) {
					break;
				}
				String url = eachEpisode.getUrl();
				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
				request.setDescription(eachEpisode.getDescription());
				request.setTitle(eachEpisode.getTitle());
				// in order for this if to run, you must use the android 3.2 to compile your app
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				    request.allowScanningByMediaScanner();
				    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				} else {
					Toast.makeText(getApplicationContext(), 
							"Your Android version is incompatible.", Toast.LENGTH_LONG).show();
					return;
				}
				String[] title = eachEpisode.getTitle().split("\\s+");
				StringBuilder sb = new StringBuilder();
//				for  ( int i = 0; i < title.length || i == 2; i++ ) {
//					sb.append(title[i]);
//				}
				sb.append(title[0]);
				eachEpisode.setLocalDirName(sb.toString() + ".mp3");
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, eachEpisode.getLocalDirName());

				// get download service and enqueue file
				DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				manager.enqueue(request);
				episodeCounter++;
			}
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.podcast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        helper.closeDB();
    }

    public void onBackPressed() {
		if ( inEpisodeDisplay ) {
			inEpisodeDisplay = false;
		}
	    super.onBackPressed();
	}
}
