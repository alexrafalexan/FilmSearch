package gr.unipi.msdn.filmsearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSearch {

    String URL = "https://api.themoviedb.org/3/search/";

    @GET("movie?api_key=f888b70bdc55df2cceefdcf5523e691b")
    Call<MoviesDataModel> getMoviesSearch(@Query("query") String q);
}
