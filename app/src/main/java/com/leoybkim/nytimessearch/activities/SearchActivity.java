package com.leoybkim.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.leoybkim.nytimessearch.R;
import com.leoybkim.nytimessearch.adapters.ArticleArrayAdapter;
import com.leoybkim.nytimessearch.fragments.FilterDialogFragment;
import com.leoybkim.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.OnFilteredArticleListener {

    private final static String LOG_TAG = SearchActivity.class.getSimpleName();

    private EditText mEditTextQuery;
    private GridView mGridViewResults;
    private Button mButtonSearch;

    private ArrayList<Article> mArticles;
    private ArticleArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup views
        mEditTextQuery =  (EditText) findViewById(R.id.et_query);
        mGridViewResults = (GridView) findViewById(R.id.gv_result);
        mButtonSearch = (Button) findViewById(R.id.btn_search);

        mArticles = new ArrayList<>();
        mAdapter = new ArticleArrayAdapter(this, mArticles);
        mGridViewResults.setAdapter(mAdapter);

        // Setup click listener
        mGridViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create an intent to display the article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = mArticles.get(i);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.filter:
                FilterDialogFragment filterFragmentDialog = new FilterDialogFragment();
                filterFragmentDialog.newInstance("Filter Articles");
                filterFragmentDialog.setOnFilteredArticleListener(this);
                filterFragmentDialog.show(getSupportFragmentManager(), "fragment_filter");
                break;
            case R.id.action_settings:
                break;
            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        String query = mEditTextQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", getString(R.string.NYT_API_KEY));
        params.put("page", 0);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    mArticles.addAll(Article.fromJsonArray(articleJsonResults));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onFilteredArticleSearch(String filterDate, String filterOrder, boolean filterArts, boolean filterEducation, boolean filterSports) {
        String query = mEditTextQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", getString(R.string.NYT_API_KEY));
        params.put("page", 0);

        if (filterDate != "Any Time") {

        }

        if (filterOrder == "Oldest") {
            params.put("sort", "oldest");
        } else {
            params.put("sort", "newest");

        }

        if (filterArts) {
            params.put("fq", "news_desk:(\"Arts\")");
        }
        if (filterEducation) {
            params.put("fq", "news_desk:(\"Education\")");
        }
        if (filterSports) {
            params.put("fq", "news_desk:(\"Sports\")");
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    mArticles.addAll(Article.fromJsonArray(articleJsonResults));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onFilteredArticle(String filterDate, String filterOrder, boolean filterArts, boolean filterEducation, boolean filterSports) {
        // Call back method
        Toast.makeText(this, filterDate + " " + filterOrder + " " + filterArts, Toast.LENGTH_LONG).show();
        onFilteredArticleSearch(filterDate, filterOrder, filterArts, filterEducation, filterSports);
    }
}
