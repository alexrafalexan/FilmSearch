package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends SideBarMenu {

    ListView favListView;
    ArrayList<MoviesDataModel> favList;
    ProgressBar progressBar;
    String TAG = "FavActivity";
    long numberOfFavoriteMovies;
    List<FireBaseAdapterMovie> movieList;

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
            FireBaseAdapterMovie fireBaseAdapterMovie = movieSnapshot.getValue(FireBaseAdapterMovie.class);
            Log.i(TAG, movieSnapshot.toString());
            movieList.add(fireBaseAdapterMovie);
            Log.e(TAG, "BackDropPath :" + movieSnapshot.getValue());
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, movieList);
            favListView.setAdapter(adapter);
        }
    }

}

