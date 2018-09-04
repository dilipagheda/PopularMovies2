package com.example.android.popularmovies2.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.database.Movie;
import com.example.android.popularmovies2.model.MovieDetailsParcelable;
import com.example.android.popularmovies2.model.MovieDetailsViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieDetailsActivity extends AppCompatActivity {

   private static final String TAG="MovieDetailsActivity";

   TextView titleView;
   ImageButton imageButton;
   MovieDetailsViewModel movieDetailsViewModel;
   TextView releaseDateView;
   TextView voteView;
   ImageView imageView;
   TextView plotView;

   int id;
   String title;
   String imageURL;
   String plot;
   String rating;
   String releaseDate;
   List<Movie> latestFavoriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Log.d(TAG,"onCreate");
        Intent intent = getIntent();
        MovieDetailsParcelable movieDetailsParcelable = intent.getParcelableExtra("selected_movie");
        id = movieDetailsParcelable.getId();
        title = movieDetailsParcelable.getTitle();
        imageURL = movieDetailsParcelable.getImageURL();
        plot = movieDetailsParcelable.getPlot();
        rating =  movieDetailsParcelable.getRating();
        releaseDate = movieDetailsParcelable.getReleaseDate();

        titleView = findViewById(R.id.title);
        titleView.setText(title);

        releaseDateView = (TextView) findViewById(R.id.release_date);
        releaseDateView.setText(releaseDate);

        voteView = (TextView) findViewById(R.id.vote);
        voteView.setText(rating);

        imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this)
                .load(imageURL)
                .into(imageView);

        plotView = (TextView) findViewById(R.id.plot);
        plotView.setText(plot);

        imageButton = findViewById(R.id.favoriteButton);

        //find view model
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);

        //build movie entity object
        final Movie movieEntity = new Movie();
        movieEntity.setId(id);
        movieEntity.setTitle(title);
        movieEntity.setImageURL(imageURL);
        movieEntity.setPlot(plot);
        movieEntity.setRating(rating);
        movieEntity.setReleaseDate(releaseDate);



        movieDetailsViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                 updateFavoriteStatus(movies,movieEntity);
                 latestFavoriteMovies = movies;
            }
        });
        //handle favorite button
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isFavorite(latestFavoriteMovies,movieEntity)){
                    Log.d(TAG, "Delete:"+movieEntity.getTitle());
                    movieDetailsViewModel.deleteFavoriteMovie(movieEntity);
                }else{
                    Log.d(TAG, "Insert:"+movieEntity.getTitle());
                    movieDetailsViewModel.insertFavoriteMovie(movieEntity);
                }

            }
        });
    }

    private boolean isFavorite(List<Movie> movies, Movie m){
        boolean flag=false;

        if(movies.contains(m)){
            flag=true;
        }else{
            flag=false;
        }
        return flag;
    }

    //check the status of favorite and show the star image accordingly
    private void updateFavoriteStatus(List<Movie> movies, Movie m){
        if(isFavorite(movies, m)){
            imageButton.setImageResource(R.drawable.ic_star_black_24dp);
        }else{
            imageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

}
