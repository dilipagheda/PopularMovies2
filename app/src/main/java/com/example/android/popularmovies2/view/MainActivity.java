package com.example.android.popularmovies2.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.popularmovies2.R;
import com.example.android.popularmovies2.adapter.ItemClickListener;
import com.example.android.popularmovies2.adapter.MoviesAdapter;
import com.example.android.popularmovies2.model.MovieDetailsParcelable;
import com.example.android.popularmovies2.model.MoviesViewModel;
import com.example.android.popularmovies2.service.Result;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener
{
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycle_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MoviesAdapter();
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        // specify an adapter (see also next example)
        viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.loadData();

        viewModel.getLiveDataObject().observe(this, new Observer<ArrayList<Result>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Result> titles) {
                if(titles!=null && titles.size()>0){
                    mAdapter.setData(titles);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


        //set up scroll listener for pagination
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                //if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            ) {
                        //loadMoreItems();
                        viewModel.loadData();
                        Log.d(TAG,"First visible Item:"+firstVisibleItemPosition+"  Total Item count:"+totalItemCount+ " Visible Item count:"+visibleItemCount);
                    }
                //}


            }
        });

    }


    @Override
    public void onClick(View view, int position) {
        Intent i = new Intent(this, MovieDetailsActivity.class);
        MovieDetailsParcelable movieDetailsParcelable = viewModel.getMovieDetailsParcelableAt(position);
        i.putExtra("selected_movie", movieDetailsParcelable);
        startActivity(i);
    }
}
