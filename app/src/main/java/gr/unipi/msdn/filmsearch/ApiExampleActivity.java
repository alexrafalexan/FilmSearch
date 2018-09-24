package gr.unipi.msdn.filmsearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiExampleActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_example);

        final ListView listView = findViewById(R.id.listView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<MoviesDataModel>> call = api.getMovies();

        call.enqueue(new Callback<List<MoviesDataModel>>() {
            @Override
            public void onResponse(Call<List<MoviesDataModel>> call, Response<List<MoviesDataModel>> response) {
                List<MoviesDataModel> moviesDataModels = response.body();

                String[] heroNames = new String[moviesDataModels.size()];

                for(int i = 0; i < moviesDataModels.size(); i++){
                    heroNames[i] = moviesDataModels.get(i).getName();
                }

                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, heroNames ));

//                for(MoviesDataModel h:moviesDataModels){
//                    Log.d("name", h.getName());
//                }

            }

            @Override
            public void onFailure(Call<List<MoviesDataModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
