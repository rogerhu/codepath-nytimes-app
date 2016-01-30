package codepath.com.nytimesfun;

import java.util.List;

/**
 * Created by rhu on 12/15/15.
 */
public class SearchResponse {

    Meta meta;

    List<Article> docs;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Article> getDocs() {
        return docs;
    }

    public void setDocs(List<Article> docs) {
        this.docs = docs;
    }
}
