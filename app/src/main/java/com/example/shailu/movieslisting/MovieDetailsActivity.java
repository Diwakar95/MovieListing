package com.example.shailu.movieslisting;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shailu on 2/8/16.
 */
public class MovieDetailsActivity extends AppCompatActivity{

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    public static TextView no_internet_movie;
    public static LinearLayout scroll_ll;
    public static AppBarLayout appBarLayout;
    public static ImageButton backButtonMovie;
    public static LinearLayout movieDetailsLoader;
    public static CoordinatorLayout coordLayoutMovie;
    public static Button retryMovieDetail;
    public static ImageView no_internet_img;
    private TextView dateTextView;
    private TextView descriptionTextView;
    private TextView titleTextView;
    private TextView ratingtTextView;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressDialog progressDialog;
    private static ViewPager mPager;
    private  int currentPage ;
    private  int NUM_PAGES =0;
    private int imagesize;
    private ArrayList<Movie> ImagesArray = new ArrayList<Movie>();
    private DotImageAdaper mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datail_movie);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Movie Name");

        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#3c3c3c"));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Hold on for a moment.");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.name().equals(State.COLLAPSED.name())) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_grey);
                } else if (state.name().equals(State.IDLE.name())) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_white);
                }
            }
        });

        new Async().execute();

        setProgressBarIndeterminateVisibility(true);

    }
    private void initViews() {

        no_internet_movie = (TextView) findViewById(R.id.no_internet_tv);
        scroll_ll = (LinearLayout) findViewById(R.id.main_rl);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        retryMovieDetail = (Button) findViewById(R.id.retry_movie_details);
        no_internet_img = (ImageView) findViewById(R.id.no_internet_img);
        backButtonMovie = (ImageButton) findViewById(R.id.backButtonEvent);
        movieDetailsLoader = (LinearLayout) findViewById(R.id.linlaHeaderProgressEventDetail);
        ((ProgressBar) findViewById(R.id.pbHeaderProgress)).getIndeterminateDrawable().setColorFilter(Color.parseColor("#3F51B5"), PorterDuff.Mode.SRC_ATOP);
        mPager = (ViewPager) findViewById(R.id.pager);
        dateTextView = (TextView) findViewById(R.id.date_tv);
        descriptionTextView = (TextView) findViewById(R.id.description_tv);
        titleTextView = (TextView) findViewById(R.id.title_tv);
        ratingtTextView = (TextView) findViewById(R.id.rating_tv);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        coordLayoutMovie = (CoordinatorLayout) findViewById(R.id.coordLayoutEvent);
        mPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new DotImageAdaper(this,ImagesArray);
        mPager.setAdapter(mAdapter);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);
        currentPage=0;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                mPager.setCurrentItem(currentPage % 10, true);
                currentPage++;
            }
        };

        int delay = 500;
        int period = 1000;
        Timer swipeTimer = new Timer();
        swipeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },delay, period);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }
    public void fetchMovie(final int id) {

        String url = URLConstant.BASE_URL + id + URLConstant.API_KEY;
        JsonObjectRequest req = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                progressDialog.hide();
                try {

                      Movie movie = new Movie();
                               movie.title =response.getString("original_title");
                               movie.popularity =response.getString("popularity");
                               movie.overview=response.getString("overview");
                               movie.release_date=response.getString("release_date");
                    ((TextView)findViewById(R.id.title_tv)).setText(movie.title);
                    collapsingToolbarLayout.setTitle(movie.title);
                    dateTextView.setText(getDate(movie.release_date));
                    descriptionTextView.setText(movie.overview);
                    Log.e("rating","="+Float.parseFloat(movie.popularity));
                    ratingBar.setEnabled(true);
                    ratingBar.setStepSize(Float.parseFloat(movie.popularity));
                    ratingBar.setRating(Float.parseFloat(movie.popularity));
                    ratingBar.invalidate();
                    TextResizable.makeTextViewResizable(descriptionTextView,5, "+More", true);
                    scroll_ll.setVisibility(View.VISIBLE);
                    appBarLayout.setVisibility(View.VISIBLE);
                    no_internet_movie.setVisibility(View.GONE);
                    retryMovieDetail.setVisibility(View.GONE);
                    no_internet_img.setVisibility(View.GONE);
                    backButtonMovie.setVisibility(View.GONE);
                    movieDetailsLoader.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
                scroll_ll.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
                no_internet_movie.setVisibility(View.VISIBLE);
                retryMovieDetail.setVisibility(View.VISIBLE);
                no_internet_img.setVisibility(View.VISIBLE);
                backButtonMovie.setVisibility(View.VISIBLE);
                movieDetailsLoader.setVisibility(View.GONE);
            }
        });

        AppController.getInstance().addToRequestQueue(req);



    }

    public void fetchMovieImages(final int id) {

        String url =URLConstant.BASE_URL + id + "/images" + URLConstant.API_KEY;
        JsonObjectRequest req = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                progressDialog.hide();
                try {

                    JSONArray imageArray = response.getJSONArray("posters");
                    for( int i=0;i<imageArray.length();i++)
                    {
                        JSONObject object = imageArray.getJSONObject(i);
                        String image_urls = URLConstant.IMAGE_BASE_URL+  "/w780" + object.getString("file_path");
                        Movie movie = new Movie();
                        movie.imageGallery = image_urls;
                        ImagesArray.add(movie);
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
                progressDialog.hide();
                scroll_ll.setVisibility(View.GONE);
                appBarLayout.setVisibility(View.GONE);
                no_internet_movie.setVisibility(View.VISIBLE);
                retryMovieDetail.setVisibility(View.VISIBLE);
                no_internet_img.setVisibility(View.VISIBLE);
                backButtonMovie.setVisibility(View.VISIBLE);
                movieDetailsLoader.setVisibility(View.GONE);
            }
        });

        AppController.getInstance().addToRequestQueue(req);



    }



    private String getDate(String date) {
        SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new SimpleDateFormat("dd MMM, yyyy").format(source.parse(date)).toString();
        } catch (ParseException e) {
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);

        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }
    }

    private class Async extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            int id = getIntent().getIntExtra("movie_id", -1);
            if (id == -1) {
                //Log.e(TAG, "Error fetching id");
            } else {
                fetchMovie(id);
                fetchMovieImages(id);

            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            retryMovieDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Async().execute();
                }
            });
            backButtonMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initViews();


        }
    }
}

