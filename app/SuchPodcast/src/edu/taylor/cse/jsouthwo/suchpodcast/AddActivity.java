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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
		//new GetRssFeed().execute(url);
	    Intent intent = new Intent(this, PodcastActivity.class);
//	    intent.putExtra("called", true);
	    startActivity( intent );  
		/*
		setContentView(R.layout.activity_podcast);
		mList = (ListView) findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(this, R.layout.basic_list_item);
        new GetRssFeed().execute(url);
        */
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
