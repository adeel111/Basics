package com.example.aydil.listviewwithcustomadapter.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aydil.listviewwithcustomadapter.R;

/**
 * Created by Adeel on 9/8/2017.
 */

public class MoviesAdapter extends ArrayAdapter {
    Context context;
    int resource;
    String[] movieNames;
    String[] directorNames;
    String[] likesText;
    String[] movieLikes;
    String[] disLikesText;
    String[] movieDislikes;
    int[] movieThumbnail;
    LayoutInflater myLayoutInflater;

    public MoviesAdapter(@NonNull Context context, @LayoutRes int resource, String[] movieNames, String[] directorNames,
                         String[] likesText, String[] movieLikes, String[] disLikesText, String[] movieDislikes,
                         int[] thumbnailImages)
    {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.movieNames = movieNames;
        this.directorNames = directorNames;
        this.likesText = likesText;
        this.movieLikes = movieLikes;
        this.disLikesText = disLikesText;
        this.movieDislikes = movieDislikes;
        this.movieThumbnail = thumbnailImages;
        myLayoutInflater = LayoutInflater.from(context);
    }
//    getView is the heart of ArrayAdapter...
//    getCount will count all the quantity of data(means how many movies we have)...

    @Override
    public int getCount() {
        return movieNames.length;
    }

    @NonNull
    @Override
//      convertView is used to reuse old view. Adapter enables you to reuse some view with new data.
//      parentView is the view which contained the item's view which getView() generates. Normally it is ListView or Gallery.

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = myLayoutInflater.inflate(R.layout.layout_for_adapter, null);
        ImageView moviesImageThumbnail = (ImageView) convertView.findViewById(R.id.movie_image_thumbnail);
        TextView moviesTitle = (TextView) convertView.findViewById(R.id.movie_title);
        TextView moviesSubtitle = (TextView) convertView.findViewById(R.id.movie_subtitle);
        TextView moviesLikesText = (TextView) convertView.findViewById(R.id.movie_likes_text);
        TextView moviesDislikesText = (TextView) convertView.findViewById(R.id.movie_dislikes_text);
        TextView moviesLikes = (TextView) convertView.findViewById(R.id.movie_likes);
        TextView moviesDislikes = (TextView) convertView.findViewById(R.id.movie_dislikes);

//      imgVuThumbnail.setImageDrawable(context.getResources().getDrawable(movieThumbnails[position]));

        moviesImageThumbnail.setImageResource(movieThumbnail[position]);
        moviesTitle.setText(movieNames[position]);
        moviesSubtitle.setText(directorNames[position]);
        moviesLikesText.setText(likesText[position]);
        moviesDislikesText.setText(disLikesText[position]);
        moviesLikes.setText(movieLikes[position]);
        moviesDislikes.setText(movieDislikes[position]);
        return convertView;
    }
}
