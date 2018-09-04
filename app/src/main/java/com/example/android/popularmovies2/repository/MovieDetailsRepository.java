package com.example.android.popularmovies2.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies2.BuildConfig;
import com.example.android.popularmovies2.database.AppDatabase;
import com.example.android.popularmovies2.database.Movie;
import com.example.android.popularmovies2.database.MovieDao;

import java.util.List;

public class MovieDetailsRepository {
    private static final String API_KEY = BuildConfig.MoviesApiKey;
    public static final String TAG = "MovieDetailsRepository";

    private MovieDao movieDao;
    private AppDatabase appDatabase;
    private LiveData<List<Movie>> favoritesMovies;

    public MovieDetailsRepository(Application application){

        //Get the database reference
        appDatabase = AppDatabase.getDatabase(application);
        movieDao = appDatabase.movieDao();
        favoritesMovies = movieDao.getAll();
    }


    ////Below methods are database related for favorites database
    public LiveData<List<Movie>> getFavoritesMovies() {
        return favoritesMovies;
    }

    public void insertNewFavoriteMovie(Movie m){
        new InsertAsyncTask(movieDao).execute(m);
    }

    public void deleteFavoriteMovie(Movie m){
        new DeleteAsyncTask(movieDao).execute(m);
    }

    private static class InsertAsyncTask extends AsyncTask<Movie,Void,Void>{
        private MovieDao movieDao;

        public InsertAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insert(movies[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG,"insertion done!");

        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Movie,Void,Void>{
        private MovieDao movieDao;

        public DeleteAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG,"deletion done!");
        }
    }

}
