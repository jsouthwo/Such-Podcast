
package edu.taylor.cse.jsouthwo.suchpodcast;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends ActionBarActivity {
    private static DatabaseHelper helper;
    private static SQLiteDatabase db;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);

		helper = DatabaseHelper.getHelper(getApplicationContext());
        db = helper.getWritableDatabase();

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

	            helper.createPodcast(newPodcast);
	            Log.d(DatabaseHelper.LOG, "Creating podcast: " + newPodcast.getTitle());

	        } catch (Exception e) {
	            Log.e("Error Parsing Data (add)", e + "");
	            e.printStackTrace();
	        }
	        return null;
	    }

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			PodcastActivity.adapter.clear();
            PodcastActivity.podcasts = helper.getAllPodcasts();
            for (Podcast podcast : PodcastActivity.podcasts) {
            	if (podcast != null){
            		PodcastActivity.adapter.add(podcast.getTitle());			//Adds the title to the listview adapter
            	}
            }

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

