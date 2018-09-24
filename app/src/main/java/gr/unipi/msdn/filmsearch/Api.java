package gr.unipi.msdn.filmsearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String URL = "https://api.themoviedb.org/3/movie/";

    @GET("top_rated?api_key=f888b70bdc55df2cceefdcf5523e691b&language=en-US")
    Call<List<MoviesDataModel>> getMovies();
}
