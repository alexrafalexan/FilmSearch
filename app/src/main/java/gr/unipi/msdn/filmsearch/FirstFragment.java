package gr.unipi.msdn.filmsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstFragment extends Fragment{

    View myView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        myView = inflater.inflate(R.layout.activity_profil, container, false);

        Intent intent = new Intent(getActivity(), ProfilActivity.class);
        startActivity(intent);

        return myView;
    }
}
