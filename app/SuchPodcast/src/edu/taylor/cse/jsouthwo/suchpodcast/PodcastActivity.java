package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.Toast;

public class PodcastActivity extends ActionBarActivity {
	public static ListView mList; 
    public static ArrayAdapter<String> adapter;
    public static ArrayList<Podcast> podcastList = new ArrayList<Podcast>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        mList = (ListView) findViewById(R.id.list);
        
        try {
            RssReader rssReader = new RssReader(params[0]);		//Creates an RssReader using the url
            Podcast newPodcast = new Podcast();					//Creates a Podcast object
            newPodcast.setUrl(params[0]);						//params[0] is the URL passed into the function
            newPodcast.setTitle(rssReader.getChannelTitle());	//Sets the title of the new Podcast object using the RssReader
            PodcastActivity.adapter.add(newPodcast.getPodcastTitle());			//Adds the title to the listview adapter
            newPodcast.setList(rssReader.getItems());			//Populates the newPodcast member list with the podcast episodes
            PodcastActivity.podcastList.add(newPodcast);						//Adds newPodcast to the podcastList
            
        } catch (Exception e) {
            Log.v("Error Parsing Data", e + "");
        }


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
			}
		});

	}
	
//	protected void onResume() {
//		super.onResume();
//		if ( adapter.getCount() == 0 ) {
//			return;
//		}
//		else {
//			mList.setAdapter(adapter);
//		}
//	}


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
    
}