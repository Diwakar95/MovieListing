package com.example.shailu.movieslisting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG="MainActivity";
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    public static TextView no_movies_tonight;
    public static LinearLayout linlaHeaderProgress;
    ProgressBar progressBar;
    public static LinearLayout moviesList;
    public static RelativeLayout no_movies_rl;
    public static Button retry_movies;
    public static ImageView no_internet_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initviews();;
        mAdapter = new MoviesAdapter(movieList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        fetchMoviesList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_munu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.information:
                Intent intent2  = new Intent(MainActivity.this,InformationActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void initviews()
    {
        recyclerView = (RecyclerView) findViewById(R.id.movies_rv);
        no_movies_tonight = (TextView) findViewById(R.id.txt_no_internet);
        linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        no_internet_img = (ImageView) findViewById(R.id.no_internet_img);
        moviesList = (LinearLayout)findViewById(R.id.movie_list_ll);
        retry_movies = (Button) findViewById(R.id.retry_movies);
        no_movies_rl = (RelativeLayout) findViewById(R.id.no_movies_rl);
    }
    private void fetchMoviesList() {

        String url = URLConstant.BASE_URL + "upcoming" + URLConstant.API_KEY;
        JsonObjectRequest req = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                linlaHeaderProgress.setVisibility(View.GONE);
                movieList.clear();
                try {

                    JSONArray jArray =response.getJSONArray("results");

                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObject = jArray.getJSONObject(i);
                        String image_url = URLConstant.IMAGE_BASE_URL + "/w300" + jObject.getString("poster_path");
                        Movie movie = new Movie(
                                jObject.getInt("id"),
                                image_url,
                                jObject.getString("title"),
                                jObject.getString("release_date"),
                                jObject.getString("adult")
                        );

                        movieList.add(movie);
                        moviesList.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(req);
    }


}
