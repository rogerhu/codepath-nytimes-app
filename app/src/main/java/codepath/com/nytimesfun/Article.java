package codepath.com.nytimesfun;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

// multimedia: 1) must be prefixed with www.nytimes.com, random to provide staggered image view
// stuff in <include layout=
// heteregenous views: document_type
// handling json objects that don't have
// news desk values -- adding params

public class Article implements Serializable {
    String web_url;
    String headline;

    String thumbNail;

    public Article(JSONObject jsonObject) {
        try {
            this.web_url = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if (multimedia.length() == 1) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
            } else if (multimedia.length() > 1) {
                JSONObject multimediaJson = multimedia.getJSONObject(new Random().nextInt(multimedia.length() - 1));
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.thumbNail = "";
            }
        } catch (JSONException e) {
           Log.d("here", e.toString());
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<Article>();
        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


    public String getWebUrl() {
        return web_url;
    }
    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() { return thumbNail;}
}
