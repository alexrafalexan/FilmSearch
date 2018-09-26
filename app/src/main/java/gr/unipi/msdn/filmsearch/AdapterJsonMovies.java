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

class AdapterJsonMovies extends ArrayAdapter<MoviesDataModel> {


    private ImageView movieImage;
    private ArrayList<MoviesDataModel> movies;
    private Context context;
    private int resource;

    public AdapterJsonMovies(Context context, int resource, ArrayList<MoviesDataModel> movies) {
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
        MoviesDataModel news = getItem(position);

        movieImage = (ImageView) convertView.findViewById(R.id.posterpath);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + news.getResults().get(position).getPosterPath()).into(movieImage);

        ((TextView) convertView.findViewById(R.id.moviestitle)).setText(news.getResults().get(position).getTitle());


        return convertView;
    }
}
