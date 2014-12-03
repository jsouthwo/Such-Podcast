package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.List;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class PodcastActivity extends ActionBarActivity {
	public static ListView mList;
    public static ArrayAdapter<String> adapter;
    public static List<Podcast> podcasts;
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

        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);
        adapter.clear();
        podcasts = helper.getAllPodcasts();
        for (Podcast podcast : podcasts) {
        	if (podcast != null){
        		Log.d("JUSTIN", "loop: " + podcast.getTitle());
        		adapter.add(podcast.getTitle());			//Adds the title to the listview adapter
        	}
        }
//		adapter.notifyDataSetChanged();
        Log.d("JUSTIN", "Post");
        Log.d("JUSTIN", "");

        mList = (ListView) findViewById(R.id.list);
        mList.setAdapter(adapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        		Toast.makeText(getApplicationContext(), "Clicked position " + position, Toast.LENGTH_SHORT).show();
        	}
		});
//        mList.setOnItemClickListener(new AdapterView.OnItemClickListener () {
//
//			@Override
//			/**
//			 * This function describes what the application should do in the event that a user clicks on a list item in the
//			 * listview. 
//			 */
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
///** TODO: MERGE **/
//				if ( inEpisodeDisplay ) {
//					Global.determinePodcastShortName(PodcastActivity.currentDisplayedPodcast.getTitle());
//					String title = (String) view.getTag(position);
//					Log.d("DBDBDB", title);
//
////					RssItem episodeToPlay = PodcastActivity.currentDisplayedPodcast.getEpisodeList().get(position);
//					Intent intent = new Intent(getApplicationContext(), AudioPlayerActivity.class);
////					intent.putExtra("episodeName", episodeToPlay.getTitle());
////					intent.putExtra("episodeDescription", episodeToPlay.getDescription());
////					intent.putExtra("episodeLocalDirName", episodeToPlay.getLocalDirName());
//////					intent.putExtra("episodePosition", position);
//////					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
//					startActivity( intent );
//				} else {
//					showEpisodes(position);
//					PodcastActivity.inEpisodeDisplay = true;
///* TODO: MERGE */
//				}
//				PodcastActivity.adapter.notifyDataSetChanged();
//			}
//
//			public void showEpisodes(int position) {
//				PodcastActivity.adapter.clear();
//				PodcastActivity.currentDisplayedPodcast = PodcastActivity.podcasts.get(position);
//				int counter = 0;
//				for (RssItem episode : helper.getEpisodes(Global.determinePodcastShortName(currentDisplayedPodcast.getTitle()))) {
//					PodcastActivity.adapter.add(episode.getTitle());
//					counter += 1;
//					if ( counter > 4 )
//						break;
//				}
//			}
//        });

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
		int episodeCounter;
		for ( Podcast podcast : podcasts ) {
			episodeCounter = 0;
			String name = Global.determinePodcastShortName(podcast.getTitle());
			for ( RssItem episode : helper.getEpisodes(name)) {
				if ( episodeCounter == 5 ) {
					break;
				}
				String url = episode.getUrl();
				DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
				request.setDescription(episode.getDescription());
				request.setTitle(episode.getTitle());
				// in order for this if to run, you must use the android 3.2 to compile your app
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				    request.allowScanningByMediaScanner();
				    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				} else {
					Toast.makeText(getApplicationContext(), 
							"Your Android version is incompatible.", Toast.LENGTH_LONG).show();
					return;
				}
				String[] title = episode.getTitle().split("\\s+");
				StringBuilder sb = new StringBuilder();
//				for  ( int i = 0; i < title.length || i == 2; i++ ) {
//					sb.append(title[i]);
//				}
				sb.append(title[0]);
				episode.setLocalDirName(sb.toString() + ".mp3");
				request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, episode.getLocalDirName());

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
