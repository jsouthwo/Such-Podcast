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

    /** VERY IMPORTANT **/
    public static String determinePodcastShortName(String title){
    	if (title.contains("ience")){
    		return RssItem.SCIFRI;
    	} else if (title.contains("nswer")){
    		return RssItem.BAM;
    	} else {
    		return RssItem.OTHER;
    	}
    }

}
