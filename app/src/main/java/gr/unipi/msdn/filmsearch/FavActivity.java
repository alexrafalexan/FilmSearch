package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavActivity extends SideBarMenu {

    ListView favListView;
    ArrayList<MoviesDataModel> favList;
    ProgressBar progressBar;
    String TAG = "FavActivity";
    long numberOfFavoriteMovies;

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

        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDdreference = mDatabase.getReference("Stared/").child(mAuth.getUid());


        mDdreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberOfFavoriteMovies = getNumberOfFavoriteMovies(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDdreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberOfFavoriteMovies = getNumberOfFavoriteMovies(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDdreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getFavotiteMovies(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

    private Long getNumberOfFavoriteMovies(DataSnapshot dataSnapshot) {
        ArrayList<String> favArrayList = new ArrayList<>();
        DataSnapshot getSnap = dataSnapshot;
        for (DataSnapshot dataSnap: dataSnapshot.getChildren()) {
            FireBaseAdapter getData = getSnap.getValue(FireBaseAdapter.class);
            String JsonRD = dataSnap.getChildren().toString();
            Log.i(TAG,JsonRD);

        }


            long i = getSnap.getChildrenCount();
            Log.i(TAG, "Number of Favorite Movies: " + getSnap.getChildrenCount());
            Log.i(TAG, "Number of Favorite Movies: " + getSnap.getValue());
            return i;
    }


    private void getFavotiteMovies(DataSnapshot dataSnapshot) {
        ArrayList<String> favArrayList = new ArrayList<>();
//        for (DataSnapshot dataSnap: dataSnapshot.getChildren()){
        DataSnapshot getSnap = dataSnapshot;
//        Log.i(TAG, getSnap.getValue().toString());
////        Log.i(TAG, "Long Value" + getSnap.getChildrenCount());
////        Log.i(TAG, "Number of Favorite Movies: ");

        FireBaseAdapter getData = getSnap.getValue(FireBaseAdapter.class);
//            String backdropPath, overview, posterPath, title, voteAverage;

//            favArrayList.add(getData.getBackdropPath());
//            favArrayList.add(getData.getPosterPath());
//            favArrayList.add(getData.getTitle());
//            favArrayList.add(getData.getVoteAverage());
//            favArrayList.add(getData.getOverview());

//            Log.i(TAG, getData.getBackdropPath());
//            Log.i(TAG, getData.getOverview());
//            Log.i(TAG, getData.getPosterPath());
//            Log.i(TAG, getData.getTitle());
//            Log.i(TAG, getData.getVoteAverage());


        // --- TEST CODE --- //


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favArrayList);
        favListView.setAdapter(adapter);
//        }
    }

}

