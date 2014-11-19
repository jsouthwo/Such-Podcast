package edu.taylor.cse.jsouthwo.suchpodcast;

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

public class RssItem {
    private String title;
    private String description;
    private String url;
    private String filename;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return url;
    }

	public String getFilename() {
		return filename;
	}

    public void setLink(String link) {
        this.url = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
