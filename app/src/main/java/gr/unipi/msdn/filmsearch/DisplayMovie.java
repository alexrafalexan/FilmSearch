package gr.unipi.msdn.filmsearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DisplayMovie extends AppCompatActivity {

    String TAG = "INFO";
    Context context;
    FirebaseAuth mAuth;


    String backdropPath, posterPath, title, voteAverage, overview;
    ImageView mbackdropPath, mposterPath;
    TextView mtitle, mvoteAverage, moverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);

        // Content Layout
        mbackdropPath = findViewById(R.id.backdroppath);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" +getValueBundle().get(0).toString()).into(mbackdropPath);
        mposterPath = findViewById(R.id.posterpath);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" +getValueBundle().get(1).toString()).into(mposterPath);
        mtitle = findViewById(R.id.title);
        mtitle.setText(getValueBundle().get(2).toString());
        mvoteAverage = findViewById(R.id.voteaverage);
        mvoteAverage.setText(getValueBundle().get(3).toString());
        moverview = findViewById(R.id.overview);
        moverview.setText(getValueBundle().get(4).toString());
        moverview.setMovementMethod(new ScrollingMovementMethod());
        // Display INFO at Logcat //
        for (int i = 0; i <=4; i++){
            Log.i(TAG, "Display Movie INFO " + getValueBundle().get(i).toString());
        }

        // Connection Firebase
        mAuth = FirebaseAuth.getInstance();
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

    // Get Value For Bundle //
    public List<String> getValueBundle() {
        List<String> list = new ArrayList<String>();
        String backdropPath = this.getIntent().getExtras().getString("BACKDROPPATH");
        String posterPath = this.getIntent().getExtras().getString("POSTERPATH");
        String title = this.getIntent().getExtras().getString("TITLE");
        String voteAverage = this.getIntent().getExtras().getString("VOTEAVERAGE");
        String overview = this.getIntent().getExtras().getString("OVERVIEW");
        list.add(backdropPath);
        list.add(posterPath);
        list.add(title);
        list.add(voteAverage);
        list.add(overview);
        return list;
    }

}
