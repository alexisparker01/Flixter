package com.example.flixter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;
    String youtubeKey;
    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivMovieDetails;
    Button playBtn;
    TextView tvDateMovieDetails;
    public static final String movie_url = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF0000"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        ivMovieDetails = (ImageView) findViewById(R.id.ivMovieDetails);
        playBtn = (Button) findViewById(R.id.playBtn);
        tvDateMovieDetails = (TextView) findViewById(R.id.tvDateMovieDetails);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvDateMovieDetails.setText("Release Date: " + movie.getReleaseDate());



        ivMovieDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                intent.putExtra("videoID", youtubeKey);
                startActivity(intent);
            }
        });


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                intent.putExtra("videoID", youtubeKey);
                startActivity(intent);
            }
        });

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);


        Glide.with(this)
                .load(movie.getPosterPath())
                .placeholder(R.drawable.flicks_movie_placeholder)
                .transform(new CenterInside(),new RoundedCornersTransformation(30,10))
                .into(ivMovieDetails);


        // Movie Trailer Asynch Call
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(movie_url, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");

                    if (results.length() == 0) {
                        return;
                    }
                    youtubeKey = results.getJSONObject(0).getString("key");

                    Log.i("MovieDetailsActivity", "Video ID:" + youtubeKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d("MovieDetailsActivity", "Error trying to get YTKey");
            }
        });



    }
}
