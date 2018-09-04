package com.example.android.popularmovies2.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmovies2.database.Movie;
import com.example.android.popularmovies2.repository.MovieDetailsRepository;

import java.util.List;

public class MovieDetailsViewModel extends AndroidViewModel {
    public static final String TAG = "MovieDetailsViewModel";

    MovieDetailsRepository movieDetailsRepository;
    LiveData<List<Movie>> favoriteMovies;
    Application application;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        Log.d(TAG,"Constructor");
    }

    public LiveData<List<Movie>> getFavoriteMovies() {

        if(movieDetailsRepository==null){
            movieDetailsRepository = new MovieDetailsRepository(application);

        }
        favoriteMovies = movieDetailsRepository.getFavoritesMovies();

        return favoriteMovies;
    }

    public void insertFavoriteMovie(Movie m){
        movieDetailsRepository.insertNewFavoriteMovie(m);
    }

    public void deleteFavoriteMovie(Movie m){
        movieDetailsRepository.deleteFavoriteMovie(m);
    }


}
