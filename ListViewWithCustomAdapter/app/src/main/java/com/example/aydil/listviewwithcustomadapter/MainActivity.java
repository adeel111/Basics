package com.example.aydil.listviewwithcustomadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.aydil.listviewwithcustomadapter.Adapter.MoviesAdapter;

public class MainActivity extends AppCompatActivity {
    ListView listViewMovies;
    MoviesAdapter moviesAdapter;
    String movieNames[] = {
            "john hancock", "Sucide Squad", "ESABELL",
            "TRANSITION", "DEATH NOTE",
            "HARRY PORTER", "I FRANKINSTIAN",
            "TWILIGHT", "FF8", "INCEPTION", "Speed"
    };
    String directorNames[] = {"john hathons",
            "esavell silly", "ROSAMUND PIKE",
            "NETFLIXER", "IFLIXER", "Ashfaq",
            "JOHNSON", "FRANKI",
            "RIZWAN", "TEHLEEL", "ADEEL"
    };
    String likesText[] = {
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes",
            "Likes"
    };
    String movieLikes[] = {
            "20k",
            "20k",
            "20k",
            "20k",
            "20k",
            "20k",
            "20k",
            "20k",
            "20k",
            "20k",
            "20k"
    };
    String disLikesText[] = {
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes",
            "DisLikes"
    };
    String moviesDislikes[] = {
            "60k",
            "60k",
            "60k",
            "60k",
            "60k",
            "60k",
            "60k",
            "60k",
            "60k",
            "60k",
            "60k"
    };

    int thumbnailImages[] = {
            R.drawable.first_pic,
            R.drawable.second_pic,
            R.drawable.fifth_pic,
            R.drawable.second_pic,
            R.drawable.fifth_pic,
            R.drawable.first_pic,
            R.drawable.second_pic,
            R.drawable.fifth_pic,
            R.drawable.second_pic,
            R.drawable.fifth_pic,
            R.drawable.second_pic
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewMovies = findViewById(R.id.list_view_movies);
        moviesAdapter = new MoviesAdapter(MainActivity.this, R.layout.layout_for_adapter,
                movieNames,
                directorNames,
                likesText,
                movieLikes,
                disLikesText,
                moviesDislikes,
                thumbnailImages);
        listViewMovies.setAdapter(moviesAdapter);
    }
}
