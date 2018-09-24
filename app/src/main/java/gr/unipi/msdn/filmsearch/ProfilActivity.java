package gr.unipi.msdn.filmsearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.net.URI;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private static final int CHOOSE_IMAGE = 101;
    ImageView photoImage;
    ImageView saveButton;
    EditText editText;
    Uri uriProfilImage;
    ProgressBar progressBar;
    String profileImageUrl;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mAuth = FirebaseAuth.getInstance();

        photoImage = (ImageView) findViewById(R.id.photo);
        saveButton = (ImageView) findViewById(R.id.btsave);
        editText = (EditText) findViewById(R.id.displayname);

        progressBar =(ProgressBar) findViewById(R.id.progressphoto);

        progressBar.setVisibility(View.GONE);

        findViewById(R.id.photo).setOnClickListener(this);
        findViewById(R.id.btsave).setOnClickListener(this);
        
        // Load user profile info
        loadUserInfo();
        
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user.getPhotoUrl() != null){
            Glide.with(this)
                    .load(user.getPhotoUrl().toString())
                    .into(photoImage);
        }
        if(user.getDisplayName() != null){
            editText.setText(user.getDisplayName());
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photo:
                showImageChooser();
                Log.i("Content", "Press Choose Photo");
                break;
            case R.id.btsave:
                saveUserInformation();
                Log.i("Content", "Press Save");
                Intent iMain = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(iMain);
                break;
        }
    }

    private void saveUserInformation() {
        String displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Display Name is required");
            editText.requestFocus();
            Log.i("ERROR", "EMAIL DISPLAY");
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null && profileImageUrl !=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(Uri.parse(profileImageUrl)).build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfilActivity.this,"Profile Update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData() != null){
               uriProfilImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfilImage);
                photoImage.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profilImageRef = FirebaseStorage.getInstance().getReference("profilfilm/"+System.currentTimeMillis()+".jpg");
        if(uriProfilImage !=null){
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            profilImageRef.putFile(uriProfilImage).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Toast.makeText(ProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profil Image"), CHOOSE_IMAGE);
    }


    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_first_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();
        } else if (id == R.id.nav_second_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SecondFragment()).commit();
        } else if (id == R.id.nav_third_layout) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_signout) {
            signout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent logoutIntentDropDown = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(logoutIntentDropDown);
    }
}
