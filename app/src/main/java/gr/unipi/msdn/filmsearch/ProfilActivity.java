package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.IOException;

public class ProfilActivity extends SideBarMenu  implements View.OnClickListener{

    private static final int CHOOSE_IMAGE = 101;
    private ImageView photoImage;
    private ImageView saveButton;
    private EditText editText;
    private Uri uriProfilImage;
    private ProgressBar progressBar;
    private String profileImageUrl;
    private FirebaseAuth mAuth;

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


        SideBarMenu(R.id.profillayout, R.id.nav_view);
        LoadUserInfo();

    }

    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
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

    private void LoadUserInfo() {
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

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profil Image"), CHOOSE_IMAGE);
    }


    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent logoutIntentDropDown = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(logoutIntentDropDown);
    }
}
