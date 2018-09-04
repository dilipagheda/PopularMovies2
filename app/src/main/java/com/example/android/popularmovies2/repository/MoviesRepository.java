package com.example.android.popularmovies2.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.popularmovies2.BuildConfig;
import com.example.android.popularmovies2.model.MovieDetailsParcelable;
import com.example.android.popularmovies2.service.Movies;
import com.example.android.popularmovies2.service.MoviesService;
import com.example.android.popularmovies2.service.Result;
import com.example.android.popularmovies2.service.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {
    private static final String API_KEY = BuildConfig.MoviesApiKey;
    public static final String TAG = "MoviesRepository";

    private MutableLiveData<ArrayList<Result>> movieItemsFromResponse;
    private MoviesService moviesService;
    private int page=1;



    public MoviesRepository(Application application){
        movieItemsFromResponse = new MutableLiveData<ArrayList<Result>>();
        movieItemsFromResponse.setValue(new ArrayList<Result>());
        this.page = 1;


    }

    //Below methods are for retrieving data from the network
    private void loadTitlesFromNetwork(int page){

        Log.d(TAG,"loadTitlesFromNetwork:"+page);
        moviesService = RetrofitInstance.getService();
        Call<Movies> callBackend;
        callBackend = moviesService.getMoviesByPopularity(API_KEY,page);

        Log.d(TAG,callBackend.request().url().toString());


            callBackend.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Log.d(TAG, "Response:" + response.message());
                    if(response.isSuccessful() && response.body().getResults().size()>0){
                        ArrayList<Result> currentResult = movieItemsFromResponse.getValue();
                        currentResult.addAll(response.body().getResults());
                        movieItemsFromResponse.setValue(currentResult);
                    }

                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    //TODO: Handle network errors
                    Log.d(TAG,"Network error:"+t.getMessage() );
                }});
    }

    public LiveData<ArrayList<Result>> getLiveDataObject(){
        return movieItemsFromResponse;
    }

    public void loadData(){
        //TODO: implement a logic for validating current page is not greater than total pages
        loadTitlesFromNetwork(page++);

    }

    public MovieDetailsParcelable getMovieDetailsParcelableAt(int position){
        Result result = movieItemsFromResponse.getValue().get(position);
        int id = result.getId();
        String title = result.getTitle();
        String imageURL = result.getAbsolutePosterPath();
        String plot = result.getOverview();
        String rating = String.valueOf(result.getVoteAverage());
        String releaseDate = result.getReleaseDate();


        MovieDetailsParcelable movieDetailsParcelable = new MovieDetailsParcelable(id,title,releaseDate,rating,plot,imageURL);
        return movieDetailsParcelable;
    }


}
