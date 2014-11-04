package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class PodcastActivity extends ActionBarActivity {
	private ListView mList;
    ArrayAdapter<String> adapter;
    ArrayList<Podcast> podcastList = new ArrayList<Podcast>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
		setContentView(R.layout.activity_podcast);
		mList = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);
        new GetRssFeed().execute(url);
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
                adapter.add(newPodcast.getPodcastTitle());			//Adds the title to the listview adapter
                newPodcast.setList(rssReader.getItems());			//Populates the newPodcast member list with the podcast episodes
                podcastList.add(newPodcast);						//Adds newPodcast to the podcastList
                
            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            mList.setAdapter(adapter);
            mList.setOnItemClickListener(new AdapterView.OnItemClickListener () {

				@Override
				/**
				 * This function describes what the application should do in the event that a user clicks on a list item in the
				 * listview. 
				 */
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					adapter.remove(podcastList.get(position).getPodcastTitle());			//Remove the listitem containing the podcast name
					for (RssItem episode : podcastList.get(position).getEpisodeList()) {	//Loops through the episode list and
						adapter.add(episode.getTitle());									//Adds each episode's title to the listview adapter
					}
					adapter.notifyDataSetChanged();
				}
            	
            });
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
}