package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.ArrayList;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

        Button addLink = (Button)findViewById(R.id.add_link);
        addLink.setOnClickListener(new View.OnClickListener() {

			
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "Taking you to add a podcast.", Toast.LENGTH_SHORT).show();
			    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
			    startActivity( intent );

//				createPodcast(textBox.getText().toString()); //Takes the value from the input field and adds a podcast
			}
		});
        
        Button downloadButton = (Button)findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Downloading all new podcasts.", Toast.LENGTH_LONG).show();
//				createPodcast(textBox.getText().toString()); //Takes the value from the input field and adds a podcast
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