package gr.unipi.msdn.filmsearch;

public class FireBaseAdapterMovie {

    private String backdropPath;
    private String overview;
    private String posterPath;
    private String title;
    private String voteAverage;

    public FireBaseAdapterMovie() {
    }

    public FireBaseAdapterMovie(String backdropPath, String overview, String posterPath, String title, String voteAverage) {
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.posterPath = posterPath;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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
}
