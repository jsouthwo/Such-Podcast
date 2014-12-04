package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPlayerActivity extends Activity {

	public TextView songName,episodeDescriptionBox,startTimeField,endTimeField;
	private MediaPlayer mediaPlayer;
	private double startTime = 0;
	private double finalTime = 0;
	private Handler myHandler = new Handler();;
	private int forwardTime = 20000; 
	private int backwardTime = 10000;
	private SeekBar seekbar;
	private ImageButton playButton,pauseButton;
	public static int oneTimeOnly = 0;
	private String episodeTitle, episodeDescription;
    private static DatabaseHelper helper;
    private static SQLiteDatabase db;
    private RssItem episode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_player);

		episodeTitle = getIntent().getExtras().getString("episodeTitle");

		helper = DatabaseHelper.getHelper(getApplicationContext());
		db = helper.getWritableDatabase();

		episode = helper.getEpisode(episodeTitle);
		episodeDescription = episode.getDescription();
		
		//VIEWS
		songName = (TextView)findViewById(R.id.title_text);
		startTimeField =(TextView)findViewById(R.id.start_time);
		endTimeField =(TextView)findViewById(R.id.end_time);
		seekbar = (SeekBar)findViewById(R.id.seekBar1);
		playButton = (ImageButton)findViewById(R.id.playButton);
		pauseButton = (ImageButton)findViewById(R.id.pauseButton);
		episodeDescriptionBox = (TextView)findViewById(R.id.description_text);
		
		//OTHER THINGS
		songName.setText(episodeTitle);
		episodeDescriptionBox.setText(episodeDescription);
		
		try {
			mediaPlayer = MediaPlayer.create(this, Uri.parse(episode.getFilename()));
		} catch (NullPointerException e){
			Toast.makeText(getApplicationContext(), "Try refreshing your podcasts first.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		seekbar.setClickable(false);
		pauseButton.setEnabled(false);
		songName.setSelected(true);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void play(View view){
		mediaPlayer.start();
		finalTime = mediaPlayer.getDuration();
		startTime = mediaPlayer.getCurrentPosition();
		seekbar.setProgress(500);
		if(oneTimeOnly == 0){
			seekbar.setMax((int) finalTime);
			oneTimeOnly = 1;
		} 

		endTimeField.setText(String.format("%02d:%02d", 
				TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
				TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) finalTime)))
				);
		startTimeField.setText(String.format("%02d:%02d", 
				TimeUnit.MILLISECONDS.toMinutes((long) startTime),
				TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) startTime)))
				);
		seekbar.setProgress((int)startTime);
		myHandler.postDelayed(UpdateSongTime,100);
		pauseButton.setEnabled(true);
		playButton.setEnabled(false);
		songName.setSelected(true);
		
		mark(episode);
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void resume(View view){
		mediaPlayer.start();
		Log.e("Caleb", String.valueOf(episode.getCurrentPosition()));
		mediaPlayer.seekTo((int)episode.getCurrentPosition());
		finalTime = mediaPlayer.getDuration();
		startTime = mediaPlayer.getCurrentPosition();
		seekbar.setProgress(500);
		if(oneTimeOnly == 0){
			seekbar.setMax((int) finalTime);
			oneTimeOnly = 1;
		} 

		endTimeField.setText(String.format("%02d:%02d", 
				TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
				TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) finalTime)))
				);
		startTimeField.setText(String.format("%02d:%02d", 
				TimeUnit.MILLISECONDS.toMinutes((long) startTime),
				TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) startTime)))
				);
		seekbar.setProgress((int)startTime);
		myHandler.postDelayed(UpdateSongTime,100);
		pauseButton.setEnabled(true);
		playButton.setEnabled(false);
		songName.setSelected(true);

		mark(episode);
	}

	private Runnable UpdateSongTime = new Runnable() {
		@TargetApi(Build.VERSION_CODES.GINGERBREAD) public void run() {
			startTime = mediaPlayer.getCurrentPosition();
			startTimeField.setText(String.format("%02d:%02d", 
					TimeUnit.MILLISECONDS.toMinutes((long) startTime),
					TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
							toMinutes((long) startTime)))
					);
			seekbar.setProgress((int)startTime);
			myHandler.postDelayed(this, 100);
		}
	};

	public void pause(View view){
		mediaPlayer.pause();
		pauseButton.setEnabled(false);
		playButton.setEnabled(true);
		songName.setSelected(true);
	}	

	public void forward(View view){
		int temp = (int)startTime;
		if((temp+forwardTime)<=finalTime){
			startTime = startTime + forwardTime;
			mediaPlayer.seekTo((int) startTime);
			songName.setSelected(true);
		}
		else{
			Toast.makeText(getApplicationContext(), 
					"Cannot jump forward 20 seconds", 
					Toast.LENGTH_SHORT).show();
			songName.setSelected(true);
		}

	}
	public void rewind(View view){
		int temp = (int)startTime;
		if((temp-backwardTime)>0){
			startTime = startTime - backwardTime;
			mediaPlayer.seekTo((int) startTime);
			songName.setSelected(true);
		}
		else{
			Toast.makeText(getApplicationContext(), 
					"Cannot jump backward 10 seconds",
					Toast.LENGTH_SHORT).show();
			songName.setSelected(true);
		}
	}

	public void markAsPlayed(View view){
		mark(episode);
	}
	
	private void mark(RssItem e){

		if (e.getPlayed() == 0){

			String title = e.getTitle();
			title = "** ALREADY PLAYED **   " + title;
			e.setTitle(title);

			e.setPlayed(1);
			helper.updateEpisode(e);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//   getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		try {
			episode.setCurrentPosition(mediaPlayer.getCurrentPosition());
			helper.updateEpisode(episode);
			mediaPlayer.stop();
			myHandler.removeCallbacks(UpdateSongTime);
		} catch (Exception e){
			e.printStackTrace();
		}
		
//		PodcastActivity.resetAdapter(episode);
	    super.onBackPressed();
	}

}
