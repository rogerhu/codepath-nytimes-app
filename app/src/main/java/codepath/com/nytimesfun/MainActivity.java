package codepath.com.nytimesfun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mGridView = (GridView) findViewById(R.id.gvItems);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        mGridView.setAdapter(adapter);
        adapter.clear();

        loadDataFromApi(0);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = articles.get(position);
                Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
                intent.putExtra(ArticleActivity.ARTICLE, article);
                startActivity(intent);
            }
        });

        mGridView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d("here", String.valueOf(page));
                loadDataFromApi(page);
                return true;
            }
        });
    }

    public void loadDataFromApi(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", "e863004d8ddc2900d7111aa358a8bdbb:19:4701790");
//        params.put("fq", "news_desk:(\"Sports\")");
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        params.put("page", page);
        params.put("fq", "type_of_material:(\"News\")");
//        url = String.format("http://api.nytimes.com/svc/topstories/v1/%s.json", "home");
//        params.put("api-key", "8c0c9f857fb5e21ca09d54f3fad286ce:3:4701790");

        client.get(url, params,
                new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                            Throwable throwable) {
                        Log.d("hey", "here");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
/*                        Type collectionType = new TypeToken<List<Multimedia>>(){}.getType();

                        Gson gson = new GsonBuilder().registerTypeAdapter(collectionType, new MultimediaDeserializer()).create();
                        TopStories a = gson.fromJson(responseString, TopStories.class);*/

                        Gson gson = new GsonBuilder().create();
                        SearchResponseData a = gson.fromJson(responseString, SearchResponseData.class);
                        adapter.addAll(a.getSearchResponse().getDocs());

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
