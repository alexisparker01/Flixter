package com.example.flixter.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel // annotation indicates class is Parcelable
// class is going to encapsulate the idea of a movie
public class Movie {

    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAvg;
    Integer id;
    String date;

    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAvg = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        date = jsonObject.getString("release_date");
        Log.i("Movie", "Release date: " + date);


    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++)  {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
        // 20:27 - episode 2
        // fetching all the available sizes, appending that to the base url, and then adding the relative path
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() { return voteAvg; }

    public Integer getId() {
        return id;
    }

    public String getReleaseDate() {
        return date;
    }
}

