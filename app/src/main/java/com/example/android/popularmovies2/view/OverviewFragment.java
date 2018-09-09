package com.example.android.popularmovies2.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.database.Movie;
import com.example.android.popularmovies2.databinding.MovieOverviewBinding;
import com.example.android.popularmovies2.model.Helper;
import com.example.android.popularmovies2.model.MovieDetailsParcelable;
import com.example.android.popularmovies2.model.MovieDetailsViewModel;
import com.example.android.popularmovies2.service.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    private static final String TAG="OverviewFragment";
    private static final String MOVIE_DETAILS = "movie_details";

    private MovieDetailsParcelable movieDetailsParcelable;

    ImageButton imageButton;
    MovieDetailsViewModel movieDetailsViewModel;
    Movie movieEntity;

    List<Movie> latestFavoriteMovies;
    ImageView shareButton;
    String firstTrailer;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(MovieDetailsParcelable movieDetailsParcelable) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_DETAILS, movieDetailsParcelable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieDetailsParcelable = getArguments().getParcelable(MOVIE_DETAILS);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View view = inflater.inflate(R.layout.movie_overview, container, false);
        MovieOverviewBinding bindings = MovieOverviewBinding.inflate(inflater,container,false);
        View view = bindings.getRoot();


        imageButton = view.findViewById(R.id.favoriteButton);
        shareButton = view.findViewById(R.id.shareButton);

        //find view model
        movieDetailsViewModel = ViewModelProviders.of(this).get(MovieDetailsViewModel.class);


        //build movie entity object
        movieEntity = new Movie();
        movieEntity.setId(movieDetailsParcelable.getId());
        movieEntity.setTitle(movieDetailsParcelable.getTitle());
        movieEntity.setImageURL(movieDetailsParcelable.getImageURL());
        movieEntity.setPlot(movieDetailsParcelable.getPlot());
        movieEntity.setRating(movieDetailsParcelable.getRating());
        movieEntity.setReleaseDate(movieDetailsParcelable.getReleaseDate());

        //Bind data to view here
        bindings.setMovie(movieEntity);


        movieDetailsViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                 updateFavoriteStatus(movies,movieEntity);
                 latestFavoriteMovies = movies;
            }
        });
        movieDetailsViewModel.loadTrailers(movieEntity.getId());
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

        movieDetailsViewModel.getTrailers().observe(this, new Observer<ArrayList<Trailer>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Trailer> trailers) {
                if(trailers!=null && trailers.size()>0){
                    firstTrailer = Helper.getWebUri(trailers.get(0)).toString();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstTrailer.isEmpty())
                    return;
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject)+movieEntity.getTitle());
                share.putExtra(Intent.EXTRA_TEXT, firstTrailer);

                startActivity(Intent.createChooser(share, getString(R.string.share_title)));
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

}
