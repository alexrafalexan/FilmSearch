package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SideBarMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public NavigationView mNavigationView;
    ImageView imagePic;
    TextView textView;

//    public TextView textview = headerview.findViewById(R.id.profilname);


    FirebaseAuth mAuth;

    public void SideBarMenu(int layout, int nav_view) {
        Log.i("int", "SideBarMenu: " + nav_view);

        mDrawerLayout = (DrawerLayout) findViewById(layout);
        mNavigationView = (NavigationView) findViewById(nav_view);
        View headerview = mNavigationView.getHeaderView(0);
        imagePic = (ImageView) headerview.findViewById(R.id.sidebarpimage);
        textView = (TextView) headerview.findViewById(R.id.profilname);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mNavigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        LoadUserInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.top_movies) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(this, SearchMovieActivity.class));
        } else if (id == R.id.favorites) {
               startActivity(new Intent(this,FavActivity.class));
        } else if (id == R.id.profile) {
            startActivity(new Intent(this, ProfilActivity.class));
        } else if (id == R.id.sign_out) {
            logout();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void LoadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl().toString())
                    .into(imagePic);
        }
        if (user.getDisplayName() != null) {
            textView.setText(user.getDisplayName());
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent logoutIntentDropDown = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logoutIntentDropDown);
    }
}
