package gr.unipi.msdn.filmsearch;

public class FavMoviesDB {
    String  backdropPath, posterPath, title, voteAverage, overview;

    public FavMoviesDB() {
    }

    public FavMoviesDB(String backdropPath, String posterPath, String title, String voteAverage, String overview) {
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
