package codepath.com.nytimesfun;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Article implements Serializable {
    String web_url;
    String snippet;

    ArrayList<Multimedia> multimedia;

    public Article() {
        multimedia = new ArrayList<>();
    }


    public String getWebUrl() {
        return web_url;
    }

    public void setWebUrl(String webUrl) {
        this.web_url = webUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public String getThumbnail() {
        if (multimedia.size() > 0 ) {
            return "http://www.nytimes.com/" + multimedia.get(0).getUrl();
        }
        return null;
    }
}
