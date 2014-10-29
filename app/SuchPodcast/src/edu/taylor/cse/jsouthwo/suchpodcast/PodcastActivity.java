package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
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
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
        Button addbutton = (Button)findViewById(R.id.add_button);
        final EditText textBox = (EditText)findViewById(R.id.editText1);
        addbutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				createPodcast(textBox.getText().toString()); //Takes the value from the input field and adds a podcast
			}
		});
    }
	
	private void createPodcast(String url) {
		setContentView(R.layout.activity_podcast);
		mList = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);
        new GetRssFeed().execute(url);
	}
	
	private class GetRssFeed extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                RssReader rssReader = new RssReader(params[0]);
                Podcast newPodcast = new Podcast();
                newPodcast.setTitle(rssReader.getChannelTitle());
                adapter.add(newPodcast.getPodcastTitle());
                newPodcast.setList(rssReader.getItems());
                podcastList.add(newPodcast);
                
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
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					adapter.remove(podcastList.get(position).getPodcastTitle());
					for (RssItem episode : podcastList.get(position).getEpisodeList()) {
						adapter.add(episode.getTitle());
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_podcast, container, false);
            return rootView;
        }
    }
}