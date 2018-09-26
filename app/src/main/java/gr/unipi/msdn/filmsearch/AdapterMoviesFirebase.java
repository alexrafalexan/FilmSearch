package gr.unipi.msdn.filmsearch;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class AdapterMoviesFirebase extends ArrayAdapter<FirebaseMovieDataModel> {


    ImageView movieImage;

    ArrayList<FirebaseMovieDataModel> movies;
    Context context;
    int resource;

    public AdapterMoviesFirebase(Context context, int resource, ArrayList<FirebaseMovieDataModel> movies) {
        super(context, resource, movies);
        this.movies = movies;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_movie_layout, null, true);

        }
        FirebaseMovieDataModel news = getItem(position);

        movieImage = (ImageView) convertView.findViewById(R.id.posterpath);
        Picasso.with(context).load(news.getPosterPath()).into(movieImage);

        ((TextView) convertView.findViewById(R.id.moviestitle)).setText(news.getTitle());


        return convertView;
    }
}
