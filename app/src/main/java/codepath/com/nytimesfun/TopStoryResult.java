package codepath.com.nytimesfun;

import java.util.List;

/**
 * Created by rhu on 12/16/15.
 */
public class TopStoryResult {

    String title;

    List<Multimedia> multimedia;

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public String getThumbnail() {
        if (multimedia.size() > 0 ) {
//            return "http://www.nytimes.com/" + multimedia.get(0).getUrl();
            return multimedia.get(0).getUrl();

        }
        return null;
    }

    public String getTitle() {
        return title;
    }



}
