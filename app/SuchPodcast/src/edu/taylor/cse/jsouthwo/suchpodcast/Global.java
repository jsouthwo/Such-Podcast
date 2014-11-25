package edu.taylor.cse.jsouthwo.suchpodcast;

import android.app.Application;
import android.content.Context;

public class Global extends Application {
	private static Global instance;

	public Global() {
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}
}
