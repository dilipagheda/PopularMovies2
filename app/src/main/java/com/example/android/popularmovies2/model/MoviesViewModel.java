package com.example.android.popularmovies2.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.popularmovies2.repository.MoviesRepository;
import com.example.android.popularmovies2.service.Result;

import java.util.ArrayList;

public class MoviesViewModel extends AndroidViewModel{

    MoviesRepository moviesRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        moviesRepository = new MoviesRepository(application);
    }

    public LiveData<ArrayList<Result>> getLiveDataObject(){
        return moviesRepository.getLiveDataObject();
    }

    public void loadData(){
        moviesRepository.loadData();
    }

    public MovieDetailsParcelable getMovieDetailsParcelableAt(int position){

        return moviesRepository.getMovieDetailsParcelableAt(position);
    }
}
