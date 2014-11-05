package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.List;


public class Podcast {
	private String url;
	private String podcastTitle;
	private List<RssItem> episodeList;
	
	public void setTitle(String newTitle) {
		podcastTitle = newTitle;
	}
	
	public void setList(List<RssItem> rssList) {
		episodeList = rssList;
	}

	public String getPodcastTitle() {
		return podcastTitle;
	}

	public List<RssItem> getEpisodeList() {
		return episodeList;
	}
	
	public void setUrl(String newUrl) {
		url = newUrl;
	}
	
	public String getUrl() {
		return url;
	}
}
