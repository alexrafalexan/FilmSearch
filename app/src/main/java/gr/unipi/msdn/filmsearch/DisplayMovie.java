package gr.unipi.msdn.filmsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayMovie extends AppCompatActivity {

    private String TAG = "DisplayMovie";
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private String favActivityCheck;

    private String backdropPath, posterPath, title, voteAverage, overview;
    private ImageView mbackdropPath, mposterPath,fav,favend;
    private TextView mtitle, mvoteAverage, moverview;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);

        // Content Layout
        progressBar =findViewById(R.id.progressdisplaymovie);

        favActivityCheck = getValueBundle().get(5).toString();


        if(favActivityCheck.contains("FavActivity")){
            backdropPath = getValueBundle().get(0).toString();
            posterPath = getValueBundle().get(1).toString();
        }else{
            backdropPath = "http://image.tmdb.org/t/p/w185/" +getValueBundle().get(0).toString();
            posterPath = "http://image.tmdb.org/t/p/w185/" +getValueBundle().get(1).toString();
        }

        mbackdropPath = findViewById(R.id.backdroppath);
        Picasso.with(context).load(backdropPath).into(mbackdropPath);

        mposterPath = findViewById(R.id.posterpath);
        Picasso.with(context).load(posterPath).into(mposterPath);

        mtitle = findViewById(R.id.title);
        title = getValueBundle().get(2).toString();
        mtitle.setText(title);
        mvoteAverage = findViewById(R.id.voteaverage);
        voteAverage = getValueBundle().get(3).toString();
        mvoteAverage.setText(voteAverage);
        moverview = findViewById(R.id.overview);
        overview = getValueBundle().get(4).toString();
        moverview.setText(overview);
        moverview.setMovementMethod(new ScrollingMovementMethod());

        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("Stared/").child(mAuth.getUid());

        // Favorite ICO
        fav = (ImageView) findViewById(R.id.imagefav);
        favend = (ImageView) findViewById(R.id.imagefavadded);
        // ADD FAVORITE //
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavoriteMovie(backdropPath, posterPath, title, voteAverage, overview);
                fav.setVisibility(View.GONE);
                favend.setVisibility(View.VISIBLE);
            }
        });
        // REMOVE FAVORITE __ NOT WORKING __ //
        favend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.setVisibility(View.VISIBLE);
                favend.setVisibility(View.GONE);
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
        }else{
            checkFavoriteMovie();
        }
    }


    // Get Value For Bundle //
    public List<String> getValueBundle() {
        List<String> list = new ArrayList<String>();
        String backdropPath = this.getIntent().getExtras().getString("BACKDROPPATH");
        String posterPath = this.getIntent().getExtras().getString("POSTERPATH");
        String title = this.getIntent().getExtras().getString("TITLE");
        String voteAverage = this.getIntent().getExtras().getString("VOTEAVERAGE");
        String overview = this.getIntent().getExtras().getString("OVERVIEW");
        String favActivityCheck = this.getIntent().getExtras().getString("FAVACTIVITY");
        list.add(backdropPath);
        list.add(posterPath);
        list.add(title);
        list.add(voteAverage);
        list.add(overview);
        list.add(favActivityCheck);
        Log.e(TAG, favActivityCheck );
        return list;
    }

    private void checkFavoriteMovie() {
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DataSnapshot post = dataSnapshot;
                FavMoviesDB dataretrieval = post.getValue(FavMoviesDB.class);

                // Disable Stared if Article is Favorite //
                String favTitleRec = dataretrieval.getTitle().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (favTitleRec.contains(title)) {
                    fav.setVisibility(View.GONE);
                    favend.setVisibility(View.VISIBLE);
                    // Disable Push Button favend //
                    favend.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                }
                else{
                    progressBar.setVisibility(View.GONE);
                }
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

    public void addFavoriteMovie(String backdropPath,String posterPath,String title,String voteAverage,String overview) {
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("backdropPath", backdropPath);
        taskMap.put("posterPath", posterPath);
        taskMap.put("title", title);
        taskMap.put("voteAverage", voteAverage);
        taskMap.put("overview", overview);
        dbRef.push().setValue(taskMap);
    }
}
