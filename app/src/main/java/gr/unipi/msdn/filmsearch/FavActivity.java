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

import java.util.ArrayList;

public class FavActivity extends SideBarMenu  {

    ListView favListView;
    ArrayList<MoviesDataModel> favList;
    ProgressBar progressBar;
    String TAG = "FavActivity";

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
        favListView =  (ListView) findViewById(R.id.toplistmovies);
        SideBarMenu(R.id.listmovieslayout, R.id.nav_view);

        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDdreference = mDatabase.getReference("Stared/").child(mAuth.getUid());

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

    private void getFavotiteMovies(DataSnapshot dataSnapshot) {
        ArrayList<String> favArrayList = new ArrayList<>();
        for (DataSnapshot dataSnap: dataSnapshot.getChildren()){
            DataSnapshot getSnap = dataSnapshot;
            FireBaseAdapterMovie getData = getSnap.getValue(FireBaseAdapterMovie.class);
            favArrayList.add(getData.getBackdropPath());
            favArrayList.add(getData.getPosterPath());
            favArrayList.add(getData.getTitle());
            favArrayList.add(getData.getVoteAverage());
            favArrayList.add(getData.getOverview());

            Log.i(TAG, getData.getBackdropPath());
            Log.i(TAG, getData.getOverview());
            Log.i(TAG, getData.getPosterPath());
            Log.i(TAG, getData.getTitle());
            Log.i(TAG, getData.getVoteAverage());


            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favArrayList);
            favListView.setAdapter(adapter);
        }

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favArrayList);
//        favListView.setAdapter(adapter);
    }

}

