package edu.taylor.cse.jsouthwo.suchpodcast;

import java.util.List;


public class Podcast {
	private int id;
	private String url;
	private String title;
	private List<RssItem> episodeList;
	
	public void setTitle(String newTitle) {
		title = newTitle;
	}
	
	public void setList(List<RssItem> rssList) {
		episodeList = rssList;
	}

	public String getTitle() {
		return title;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
