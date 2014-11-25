package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.ArrayList;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    private static DatabaseHelper helper;
    private static SQLiteDatabase db;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);

        helper = DatabaseHelper.getHelper(getApplicationContext());
        db = helper.getWritableDatabase();
//        Log.w("DB1","Pre");
//        helper.onCreate(db);
//        Log.w("DB1","Post");
        
        mList = (ListView) findViewById(R.id.list);

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
    
}