package edu.taylor.cse.jsouthwo.suchpodcast;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (C) 2014 Shirwa Mohamed <shirwa99@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class RssHandler extends DefaultHandler {
    private List<RssItem> rssItemList;
    private RssItem currentItem;
    private int titleCounter = 0;
    private String thisChannelTitle;
    private boolean parsingTitle;
    private boolean parsingLink;
    private boolean parsingDescription;
    private StringBuilder chars = new StringBuilder();

    public RssHandler() {
        //Initializes a new ArrayList that will hold all the generated RSS items.
        rssItemList = new ArrayList<RssItem>();
    }

    public List<RssItem> getRssItemList() {
        return rssItemList;
    }
    
    public String getChannelTitle() {
    	return thisChannelTitle;
    }

    //Called when an opening tag is reached, such as <item> or <title>
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("item"))
            currentItem = new RssItem();
        else if (qName.equals("title")) {
        	titleCounter += 1;
            parsingTitle = true;
        }
        else if (qName.equals("link"))
            parsingLink = true;
        else if (qName.equals("description"))
            parsingDescription = true;
    }

    //Called when a closing tag is reached, such as </item> or </title>
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("item")) {
            //End of an item so add the currentItem to the list of items.
        	DatabaseHelper helper = DatabaseHelper.getHelper(Global.getContext());
        	currentItem.setPodcast(Global.determinePodcastShortName(thisChannelTitle));
        	if (titleCounter <= 7){
        		helper.createEpisode(currentItem);
        		rssItemList.add(currentItem);
        	}
            chars = new StringBuilder();
            currentItem = null;
        } else if (qName.equals("title")) {
        	if ( currentItem != null ) currentItem.setTitle(chars.toString());
        	chars = new StringBuilder();
            parsingTitle = false;
        } else if (qName.equals("link")) {
            parsingLink = false;
        } else if (qName.equals("description")) {
        	if ( currentItem != null ) {
        		if ( chars.length() > 200 ) {
        			chars.setLength(200);
        			chars.append("...");
        		}
        		currentItem.setDescription(chars.toString());
        	}
        	chars = new StringBuilder();
            parsingDescription = false;
        }
    }

    //Goes through character by character when parsing whats inside of a tag.
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentItem != null) {
            //If parsingTitle is true, then that means we are inside a <title> tag so the text is the title of an item.
            if (parsingTitle)
//                currentItem.setTitle(new String(ch, start, length));
            	chars.append(ch, start, length);
                //If parsingLink is true, then that means we are inside a <link> tag so the text is the link of an item.
            else if (parsingLink)
                currentItem.setUrl(new String(ch, start, length));
                //If parsingDescription is true, then that means we are inside a <description> tag so the text is the description of an item.
            else if (parsingDescription)
            	chars.append(ch, start, length);
//                currentItem.setDescription(new String(ch, start, length));
        }
        if ( parsingTitle ) {
        	if ( titleCounter == 1 ) {
        		thisChannelTitle = new String(ch,start,length);
        	}
        }
    }
}


