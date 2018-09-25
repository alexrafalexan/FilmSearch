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

import com.google.firebase.auth.FirebaseAuth;

public class SideBarMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public NavigationView mnavigationView;

    public  void SideBarMenu(int layout, int nav_view) {
        Log.i("int", "SideBarMenu: " + nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(layout);
        mnavigationView = (NavigationView) findViewById(nav_view);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mnavigationView.setNavigationItemSelectedListener(this);



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
//              startActivity(new Intent(this,SearchMovie.class));
        } else if (id == R.id.favorites) {
            //   startActivity(new Intent(this,FavoritesMovies.class));
        } else if (id == R.id.profile) {
            startActivity(new Intent(this, ProfilActivity.class));
        } else if (id == R.id.sign_out) {
            logout();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent logoutIntentDropDown = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logoutIntentDropDown);
    }
}
