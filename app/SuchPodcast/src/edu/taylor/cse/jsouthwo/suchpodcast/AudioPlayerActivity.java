package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioPlayerActivity extends Activity {

	public TextView songName,startTimeField,endTimeField;
	private MediaPlayer mediaPlayer;
	private double startTime = 0;
	private double finalTime = 0;
	private Handler myHandler = new Handler();;
	private int forwardTime = 20000; 
	private int backwardTime = 10000;
	private SeekBar seekbar;
	private ImageButton playButton,pauseButton;
	public static int oneTimeOnly = 0;
	private String podcastName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		podcastName = getIntent().getExtras().getString("EXTRA_PODCAST_NAME");
		setContentView(R.layout.activity_audio_player);
		songName = (TextView)findViewById(R.id.textView4);
		startTimeField =(TextView)findViewById(R.id.textView1);
		endTimeField =(TextView)findViewById(R.id.textView2);
		seekbar = (SeekBar)findViewById(R.id.seekBar1);
		playButton = (ImageButton)findViewById(R.id.imageButton1);
		pauseButton = (ImageButton)findViewById(R.id.imageButton2);
		songName.setText(podcastName);
		mediaPlayer = MediaPlayer.create(this, R.raw.scifri201411071);
		seekbar.setClickable(false);
		pauseButton.setEnabled(false);

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) public void play(View view){
		Toast.makeText(getApplicationContext(), "Playing sound", 
				Toast.LENGTH_SHORT).show();
		mediaPlayer.start();
		finalTime = mediaPlayer.getDuration();
		startTime = mediaPlayer.getCurrentPosition();
		if(oneTimeOnly == 0){
			seekbar.setMax((int) finalTime);
			oneTimeOnly = 1;
		} 

		endTimeField.setText(String.format("%d min, %d sec", 
				TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
				TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) finalTime)))
				);
		startTimeField.setText(String.format("%d min, %d sec", 
				TimeUnit.MILLISECONDS.toMinutes((long) startTime),
				TimeUnit.MILLISECONDS.toSeconds((long) startTime) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
						toMinutes((long) startTime)))
				);
		seekbar.setProgress((int)startTime);
		myHandler.postDelayed(UpdateSongTime,100);
		pauseButton.setEnabled(true);
		playButton.setEnabled(false);
	}

	private Runnable UpdateSongTime = new Runnable() {
		@TargetApi(Build.VERSION_CODES.GINGERBREAD) public void run() {
			startTime = mediaPlayer.getCurrentPosition();
			startTimeField.setText(String.format("%d min, %d sec", 
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
		Toast.makeText(getApplicationContext(), "Pausing sound", 
				Toast.LENGTH_SHORT).show();

		mediaPlayer.pause();
		pauseButton.setEnabled(false);
		playButton.setEnabled(true);
	}	
	public void forward(View view){
		int temp = (int)startTime;
		if((temp+forwardTime)<=finalTime){
			startTime = startTime + forwardTime;
			mediaPlayer.seekTo((int) startTime);
		}
		else{
			Toast.makeText(getApplicationContext(), 
					"Cannot jump forward 20 seconds", 
					Toast.LENGTH_SHORT).show();
		}

	}
	public void rewind(View view){
		int temp = (int)startTime;
		if((temp-backwardTime)>0){
			startTime = startTime - backwardTime;
			mediaPlayer.seekTo((int) startTime);
		}
		else{
			Toast.makeText(getApplicationContext(), 
					"Cannot jump backward 10 seconds",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//   getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
