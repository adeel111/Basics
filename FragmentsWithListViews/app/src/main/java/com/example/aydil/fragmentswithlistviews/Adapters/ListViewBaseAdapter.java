package com.example.aydil.fragmentswithlistviews.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aydil.fragmentswithlistviews.R;

import java.util.ArrayList;
import java.util.List;

class SingleRow {
    String movieNames;
    String directorNames;
    String likesText;
    String movieLikes;
    String disLikesText;
    String movieDislikes;
    int movieThumbnail;

    public SingleRow(String movieNames, String directorNames, String likeText, String movieLikes,
                     String disLikesText, String movieDislikes, int movieThumbnail) {
        this.movieNames = movieNames;
        this.directorNames = directorNames;
        this.likesText = likeText;
        this.movieLikes = movieLikes;
        this.disLikesText = disLikesText;
        this.movieDislikes = movieDislikes;
        this.movieThumbnail = movieThumbnail;
    }
}

public class ListViewBaseAdapter extends BaseAdapter {

    ArrayList<SingleRow> arrayList;
    Context context;
    Resources resources;
    LayoutInflater myLayoutInflater;

    public ListViewBaseAdapter(Context context) {

        arrayList = new ArrayList<SingleRow>();
        this.context = context;
        resources = context.getResources();
        myLayoutInflater = LayoutInflater.from(context);
        String[] movieNames = resources.getStringArray(R.array.movieNames);
        String[] directorNames = resources.getStringArray(R.array.directorNames);
        String[] likesText = resources.getStringArray(R.array.likeText);
        String[] movieLikes = resources.getStringArray(R.array.movieLikes);
        String[] disLikesText = resources.getStringArray(R.array.disLikesText);
        String[] movieDislikes = resources.getStringArray(R.array.movieDisLikes);
        int[] movieThumbnail = {R.drawable.first_pic, R.drawable.second_pic, R.drawable.third_pic,
                R.drawable.fourth_pic, R.drawable.fifth_pic, R.drawable.first_pic, R.drawable.second_pic,
                R.drawable.third_pic, R.drawable.fourth_pic, R.drawable.fifth_pic};
        for (int i = 0; i < movieNames.length; i++) {
            arrayList.add(new SingleRow(movieNames[i], directorNames[i], likesText[i], movieLikes[i], disLikesText[i],
                    movieDislikes[i], movieThumbnail[i]));
        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View convertView1 = myLayoutInflater.inflate(R.layout.list_view_adapter_layout, null);
        ImageView moviesImageThumbnail = (ImageView) convertView1.findViewById(R.id.movie_image_thumbnail);
        final TextView moviesTitle = (TextView) convertView1.findViewById(R.id.movie_title);
        TextView moviesDirectorName = (TextView) convertView1.findViewById(R.id.movie_subtitle);
        TextView moviesLikesText = (TextView) convertView1.findViewById(R.id.movie_likes_text);
        TextView moviesDislikesText = (TextView) convertView1.findViewById(R.id.movie_dislikes_text);
        TextView moviesLikes = (TextView) convertView1.findViewById(R.id.movie_likes);
        TextView moviesDislikes = (TextView) convertView1.findViewById(R.id.movie_dislikes);

//      imgVuThumbnail.setImageDrawable(context.getResources().getDrawable(movieThumbnails[position]));

        SingleRow temp = arrayList.get(position);

        moviesImageThumbnail.setImageResource(temp.movieThumbnail);
        moviesTitle.setText(temp.movieNames);
        moviesDirectorName.setText(temp.directorNames);
        moviesLikesText.setText(temp.likesText);
        moviesLikes.setText(temp.movieLikes);
        moviesDislikesText.setText(temp.disLikesText);
        moviesDislikes.setText(temp.movieDislikes);

//        For Navigation Purpose...
        moviesTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, moviesTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView1;
    }
}
