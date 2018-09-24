package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.bumptech.glide.load.model.FileLoader;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<MoviesDataModel> topMoviesList;

    // Firebase
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
        topMoviesList = new ArrayList<>();
        listView = findViewById(R.id.toplistmovies);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
        } else {
            getMovies();
        }
    }

    private void getMovies() {

        // Create Connection get Api Result
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // API Interface
        Api api = retrofit.create(Api.class);

        api.getMovies().enqueue(new Callba<MoviesDataModel>() {
            @Override
            public void onResponse(Call<MoviesDataModel> call, Response<MoviesDataModel> response) {
                if (response.isSuccessful()) {
                    final MoviesDataModel topMovies = response.body();
                    for (int i = 0; i < topMovies.getResults().size(); i++) {
                        topMoviesList.add(new MoviesDataModel(
                                        topMovies.getPage(),
                                        topMovies.getTotalResults(),
                                        topMovies.getTotalPages(),
                                        topMovies.getResults()
                                )
                        );

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                String voteCount = topMoviesList.get(position).getResults().get(position).getVoteCount().toString();
//                                String idm = topMoviesList.get(position).getResults().get(position).getId().toString();
//                                String video = topMoviesList.get(position).getResults().get(position).getVideo().toString();
//                                String voteAverage = topMoviesList.get(position).getResults().get(position).getVoteAverage().toString();
//                                String title = topMoviesList.get(position).getResults().get(position).getTitle().toString();
//                                String popularity = topMoviesList.get(position).getResults().get(position).getPopularity().toString();
//                                String posterPath = topMoviesList.get(position).getResults().get(position).getPosterPath().toString();
//                                String originalLanguage = topMoviesList.get(position).getResults().get(position).getOriginalLanguage().toString();
//                                String originalTitle = topMoviesList.get(position).getResults().get(position).getOriginalTitle().toString();
//                                String backdropPath = topMoviesList.get(position).getResults().get(position).getBackdropPath().toString();
//                                String adult = topMoviesList.get(position).getResults().get(position).getAdult().toString();
//                                String overview = topMoviesList.get(position).getResults().get(position).getOverview().toString();
//                                String releaseDate = topMoviesList.get(position).getResults().get(position).getReleaseDate().toString();
//
//                                Bundle bundle = new Bundle();
//                                bundle.putString("VOTE_COUNT", voteCount);
//                                bundle.putString("IDM", idm);
//                                bundle.putString("VIDEO", video);
//                                bundle.putString("VOTEAVERAGE", voteAverage);
//                                bundle.putString("TITLE", title);
//                                bundle.putString("POPULARITY", popularity);
//                                bundle.putString("POSTERPATH", posterPath);
//                                bundle.putString("ORIGINALLANGUAGE", originalLanguage);
//                                bundle.putString("ORIGINALTITLE", originalTitle);
//                                bundle.putString("BACKDROPPATH", backdropPath);
//                                bundle.putString("ADULT", adult);
//                                bundle.putString("OVERVIEW", overview);
//                                bundle.putString("RELEASEDATE", releaseDate);
//                                Intent newsDisplayActivity = new Intent(MainActivity.this, DisplayMovie.class);
//                                newsDisplayActivity.putExtras(bundle);
//                                startActivity(newsDisplayActivity);
                            }
                        });
                    }
                } else {
                    Log.e("Fail:", "Fail" + response.code());
                }
                AdapterJsonMovies adapter = new AdapterJsonMovies(getApplicationContext(), R.layout.list_movie_layout, topMoviesList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MoviesDataModel> call, Throwable t) {
                Log.e("ERROR", "ERROR MESSAGE:" + t.getMessage());
            }
        });
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent logoutIntentDropDown = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logoutIntentDropDown);
    }
}

