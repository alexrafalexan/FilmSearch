package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavActivity extends SideBarMenu {

    private ListView favListView;
    private ProgressBar progressBar;
    private String TAG = "FavActivity";
    private long numberOfFavoriteMovies;
    private ArrayList<FirebaseMovieDataModel> movieList;

    // Firebase
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDdreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create Layout
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressmain);
        favListView = (ListView) findViewById(R.id.listmovies);
        SideBarMenu(R.id.listmovieslayout, R.id.nav_view);
        movieList = new ArrayList<>();

        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDdreference = mDatabase.getReference("Stared/").child(mAuth.getUid());


        mDdreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getFavoriteMovies(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
        }
    }

    private void getFavoriteMovies(DataSnapshot dataSnapshot) {
        movieList.clear();
        for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
            FirebaseMovieDataModel firebaseMovieDataModel = movieSnapshot.getValue(FirebaseMovieDataModel.class);
            Log.i(TAG, movieSnapshot.toString());
            movieList.add(firebaseMovieDataModel);
            AdapterMoviesFirebase adapter = new AdapterMoviesFirebase(this, R.layout.list_movie_layout, movieList);
            favListView.setAdapter(adapter);
        }
    }
}

