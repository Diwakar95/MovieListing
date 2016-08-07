package com.example.shailu.movieslisting;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by shailu on 6/8/16.
 */

public class DotImageAdaper extends PagerAdapter {

    private ArrayList<Movie> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private ImageView imageView;


    public DotImageAdaper(Context context,ArrayList<Movie> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.movie_list_row, view, false);

        assert imageLayout != null;
         imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        Movie movie = IMAGES.get(position);
        if (movie.imageGallery!= null)
            Glide.with(context).load(movie.imageGallery).asBitmap().centerCrop().placeholder(R.drawable.placeholderxx4).into(imageView);
        
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
