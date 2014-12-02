
package edu.taylor.cse.jsouthwo.suchpodcast;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;

public class AddActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

        Button addbutton = (Button)findViewById(R.id.add_button);
        final EditText textBox = (EditText)findViewById(R.id.editText1);
        addbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createPodcast(textBox.getText().toString()); //Takes the value from the input field and adds a podcast
			}
		});
    }
	
	/**
	 * This is called when the add button has been clicked by the user.
	 * It changes the view and calls the GetRssFeed execute function on the url parameter.
	 * @param url	the string url that was contained in the text box when the add button was pushed.
	 */
	private void createPodcast(String url) {
		Intent intent = new Intent(this, PodcastActivity.class);
		if ( PodcastActivity.adapter == null ) {
			PodcastActivity.adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);
		}
		new GetRssFeed().execute(url);
	    startActivity( intent );  
	    
	}
	
	private class GetRssFeed extends AsyncTask<String, Void, Void> {
	
    @Override
    /**
     * This is called when createPodcast calls the GetRssFeed execute method. It creates a new podcast to add to
     * the podcastList member list.
     */
    protected Void doInBackground(String... params) {
        try {
            RssReader rssReader = new RssReader(params[0]);		//Creates an RssReader using the url
            Podcast newPodcast = new Podcast();					//Creates a Podcast object
            newPodcast.setUrl(params[0]);						//params[0] is the URL passed into the function
            newPodcast.setTitle(rssReader.getChannelTitle());	//Sets the title of the new Podcast object using the RssReader
            newPodcast.setList(rssReader.getItems());			//Populates the newPodcast member list with the podcast episodes
            PodcastActivity.podcastList.add(newPodcast);						//Adds newPodcast to the podcastList
        } catch (Exception e) {
            Log.v("Error Parsing Data", e + "");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PodcastActivity.adapter.clear();
        for ( Podcast eachPodcast : PodcastActivity.podcastList ) {
        	PodcastActivity.adapter.add(eachPodcast.getPodcastTitle());
        }
        PodcastActivity.adapter.notifyDataSetChanged();
        PodcastActivity.mList.setAdapter(PodcastActivity.adapter);
        PodcastActivity.mList.setOnItemClickListener(new AdapterView.OnItemClickListener () {

			@Override
			/**
			 * This function describes what the application should do in the event that a user clicks on a list item in the
			 * listview. 
			 */
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if ( PodcastActivity.inEpisodeDisplay ) {
					RssItem episodeToPlay = PodcastActivity.currentDisplayedPodcast.getEpisodeList().get(position);
					Intent intent = new Intent(getApplicationContext(), AudioPlayerActivity.class);
					intent.putExtra("episodeName", episodeToPlay.getTitle());
					intent.putExtra("episodeDescription", episodeToPlay.getDescription());
					intent.putExtra("episodeLocalDirName", episodeToPlay.getLocalDirName());
//					intent.putExtra("episodePosition", position);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
					startActivity( intent );
				} else {
					showEpisodes(position);
					PodcastActivity.inEpisodeDisplay = true;
				}
				PodcastActivity.adapter.notifyDataSetChanged();
			}
        	
        });
    }
}
	
	public void showEpisodes(int position) {
		PodcastActivity.adapter.clear();
		PodcastActivity.currentDisplayedPodcast = PodcastActivity.podcastList.get(position);
		int counter = 0;
		for (RssItem episode : PodcastActivity.podcastList.get(position).getEpisodeList()) {	//Loops through the episode list and
			PodcastActivity.adapter.add(episode.getTitle());
			counter += 1;
			if ( counter > 4 )
				break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
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


