package com.example.shailu.movieslisting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by shailu on 2/8/16.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    private static MainActivity mobject;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, adutl, release_date;
        public ImageView movie_image;
        private View v;

        public MyViewHolder(View view) {
            super(view);
            v=view;
            view.setOnClickListener(this);
            movie_image = (ImageView) view.findViewById(R.id.movie_card_image);
            title = (TextView) view.findViewById(R.id.movie_title);
            adutl = (TextView) view.findViewById(R.id.adult_time);
            release_date = (TextView) view.findViewById(R.id.release_date_tv);

        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Movie movie= moviesList.get(getPosition());
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("movie_id",movie.id);
            context.startActivity(intent);

        }
    }


    public MoviesAdapter(List<Movie> moviesList,MainActivity object) {
        this.moviesList = moviesList;
        mobject=object;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_list_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        if (movie.poster_path!= null)
            Glide.with(mobject).load(movie.poster_path).asBitmap().centerCrop().placeholder(R.drawable.placeholderxx4).into(holder.movie_image);
        holder.title.setText(movie.getTitle());
        if(movie.getAdult().equals("false")) {
            holder.adutl.setText("(U/A)");
        } else
        {
            holder.adutl.setText("(A)");
        }
        holder.release_date.setText(movie.getRelease_date());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
