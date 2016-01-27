package codepath.com.nytimesfun;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements SettingsDialog.onFinishedListener {

    RecyclerView mRecyclerView;
    GridView mGridView;

    ArrayList<Article> articles;
    ArticleRecyclerViewAdapter adapter;
    ArticleArrayAdapter adapter2;
    String searchQuery;

    DateFormat dateFormatGmt = DateFormat.getDateInstance(DateFormat.SHORT);
    EditText beginDate;

    String mSortOrder = "";
    ArrayList<String> mNewsDeskValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        articles = new ArrayList<>();
        mGridView = (GridView) findViewById(R.id.lvResults);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvResults);

        beginDate = (EditText) findViewById(R.id.beginDate);

        if (BuildConfig.RecyclerViewAdapter) {
            //        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            //GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
            //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            mGridView.setVisibility(View.GONE);
            adapter = new ArticleRecyclerViewAdapter(articles);
            mRecyclerView.setAdapter(adapter);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);

            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    Log.d("here", String.valueOf(page));
                    loadDataFromApi(page);
                }
            });
        } else {
            mRecyclerView.setVisibility(View.GONE);
            adapter2 = new ArticleArrayAdapter(this, articles);
            mGridView.setAdapter(adapter2);
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
    }

    public void loadDataFromApi(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api-key", "70ac94dc6da14de76882da2adbcd5119:10:4701790");
        params.put("q", searchQuery);

        if (!TextUtils.isEmpty(mSortOrder)) {
            params.put("sort_order", mSortOrder);
        }

        if (mNewsDeskValues.size() > 0) {
            params.put("fq", String.format("news_desk:(%s)", TextUtils.join(", ", mNewsDeskValues)));
        }
//        params.put("fq", "news_desk:(\"Sports\" \"Science\")");

//        params.put("fq", "news_desk:(\"Fashion & Style\")");
//        params.put("fq", "news_desk:(\"Arts\")");

        //params.put("fq", "news_desk:(\"Blogs\" \"Foreign\" \"Sports\" \"Arts\" \"Fashion & Style\")");

        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        params.put("page", page);
//        params.put("fq", "type_of_material:(\"News\")");
//        url = String.format("http://api.nytimes.com/svc/topstories/v1/%s.json", "home");

        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray articleJsonResults = null;
                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            int curPosition = articles.size();
                            ArrayList<Article> newArticles = Article.fromJSONArray(articleJsonResults);

                            if (BuildConfig.RecyclerViewAdapter) {
                                articles.addAll(newArticles);
                                adapter.notifyItemRangeInserted(curPosition,
                                        curPosition + newArticles.size() - 1);
                            } else {
                                adapter2.addAll(newArticles);
                            }
                            Log.d("DEBUG", articleJsonResults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchQuery = query;
                searchView.clearFocus();
                initiateSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Handle this selection
                launchSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void search(View view) {
        initiateSearch();
    }

    public void launchSettings()  {
/*        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);*/

        FragmentManager fm = getSupportFragmentManager();
        SettingsDialog settingsDialog = SettingsDialog.newInstance();
        settingsDialog.show(fm, "fragment_edit_name");

    }

    public void initiateSearch() {
        if (BuildConfig.RecyclerViewAdapter) {
            articles.clear();
            adapter.notifyDataSetChanged();
        } else {
            adapter2.clear();
        }

        loadDataFromApi(0);
    }

    public void showTimePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    @Override
    public void onFinished(@SettingsDialog.SortOrder int sortOrder,
            @CheckboxOptions.CHOICES int checkboxOptions, Calendar curDate) {
        if (sortOrder == SettingsDialog.OLDEST) {
            mSortOrder = "oldest";
        } else if (sortOrder == SettingsDialog.NEWEST) {
            mSortOrder = "newest";
        } else {
            mSortOrder = "";
        }

        if (checkboxOptions > 0) {
            mNewsDeskValues.clear();
            if ((checkboxOptions & CheckboxOptions.ARTS) == CheckboxOptions.ARTS) {
                mNewsDeskValues.add("\"arts\"");
            }

            if ((checkboxOptions & CheckboxOptions.FASHION) == CheckboxOptions.FASHION) {
                mNewsDeskValues.add("\"fashion\"");
            }

            if ((checkboxOptions & CheckboxOptions.SPORTS) == CheckboxOptions.SPORTS) {
                mNewsDeskValues.add("\"sports\"");
            }
        }
    }


}
