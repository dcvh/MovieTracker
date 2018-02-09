package tcd.android.com.movietracker.Utils;

import java.util.ArrayList;

import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Movie;

/**
 * Created by cpu10661 on 2/9/18.
 */

public class DataUtils {
    public static ArrayList<Movie> getDebugMovieList() {
        ArrayList<Movie> movies = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Movie movie = new Movie(335984, "Blade Runner 2049")
                    .addVote(7.4f, 3101)
                    .addImages("/gajva2L0rPYkEWjzgFlBXCAVBE5.jpg", "/mVr0UiqyltcfqxbAUcLl9zWL8ah.jpg")
                    .addOverview("Thirty years after the events of the first film, a new blade runner, LAPD Officer K, unearths a long-buried secret that has the potential to plunge what's left of society into chaos. K's discovery leads him on a quest to find Rick Deckard, a former LAPD blade runner who has been missing for 30 years.")
                    .addCast(new Actor[]{
                            new Actor(30614, "Ryan Gosling", "K", "/4X1wJo5xHm3YICPWZqVCPgLBEJ7.jpg"),
                            new Actor(3, "Harrison Ford", "Rick Deckard", "/7LOTdRfHU1H1qHBxpUv3jT04eWB.jpg")
                    });
            movies.add(movie);

            movie = new Movie(78, "Blade Runner")
                    .addVote(7.9f, 4988)
                    .addImages("/p64TtbZGCElxQHpAMWmDHkWJlH2.jpg", "/5hJ0XDCxE3qGfp1H3h7HQP9rLfU.jpg")
                    .addOverview("In the smog-choked dystopian Los Angeles of 2019, blade runner Rick Deckard is called out of retirement to terminate a quartet of replicants who have escaped to Earth seeking their creator for a way to extend their short life spans.")
                    .addCast(new Actor[]{
                            new Actor(3, "Harrison Ford", "Rick Deckard", "/7LOTdRfHU1H1qHBxpUv3jT04eWB.jpg"),
                            new Actor(585, "Rutger Hauer", "Roy Batty", "/2x1S2VAUvZXZuDjZ4E9iEKINvNu.jpg")
                    });
            movies.add(movie);

            movie = new Movie(550, "Fight Club")
                    .addVote(8.3f, 11289)
                    .addImages("/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg", "/87hTDiay2N2qWyX4Ds7ybXi9h8I.jpg")
                    .addOverview("A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.")
                    .addCast(new Actor[]{
                            new Actor(819, "Edward Norton", "The Narrator", "/eIkFHNlfretLS1spAcIoihKUS62.jpg"),
                            new Actor(287, "Brad Pitt", "Tyler Durden", "/kU3B75TyRiCgE270EyZnHjfivoq.jp")
                    });
            movies.add(movie);
        }

        return movies;
    }
}
