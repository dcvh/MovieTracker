package tcd.android.com.movietracker.Utils;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tcd.android.com.movietracker.Entities.Credit.Actor;
import tcd.android.com.movietracker.Entities.Credit.CrewMember;
import tcd.android.com.movietracker.Entities.FullMovie;
import tcd.android.com.movietracker.Entities.MovieExtra;
import tcd.android.com.movietracker.Entities.Movie;

import tcd.android.com.movietracker.Utils.Utils.TimeUtils;

/**
 * Created by cpu10661 on 1/18/18.
 */

@SuppressWarnings("WeakerAccess")
public class TmdbUtils {

    // TODO: 1/19/18 secure this key before production 
    private static final String TMDB_API_KEY = "0d951f4ca93b21c3c15c14da2763be8e";
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final int READ_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(10);
    private static final int CONNECT_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(15);

    private static String getMovieQueryUrl(String title) {
        return Uri.parse("https://api.themoviedb.org/3/search/movie?").buildUpon()
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("page", "1")
                .appendQueryParameter("include_adult", "true")
                .appendQueryParameter("query", title)
                .toString();
    }

    private static String getCreditQueryUrl(int movieId) {
        String baseUrl = String.format(Locale.getDefault(),
                "https://api.themoviedb.org/3/movie/%d/credits?", movieId);
        return Uri.parse(baseUrl)
                .buildUpon()
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .toString();
    }

    private static String getExtraQueryUrl(int movieId) {
        String baseUrl = String.format(Locale.getDefault(),
                "https://api.themoviedb.org/3/movie/%d?", movieId);
        return Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .appendQueryParameter("language", "en-US")
                .toString();
    }

    private static String getFullInfoQueryUrl(int movieId) {
        String baseUrl = String.format(Locale.getDefault(),
                "http://api.themoviedb.org/3/movie/%d?", movieId);
        String[] fields = new String[] {"credits", "images", "videos", "similar_movies", "release_dates"};

        return Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("api_key", TMDB_API_KEY)
                .appendQueryParameter("append_to_response", TextUtils.join(",", fields))
                .toString();
    }

    @Nullable
    private static String getJsonResponse(@NonNull String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        String json = null;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                json = body.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static ArrayList<Movie> findMoviesByTitle(@NonNull String title) {
        // get json response
        String queryUrl = getMovieQueryUrl(title);
        String json = getJsonResponse(queryUrl);

        // convert to usable data
        ArrayList<Movie> movies = null;
        if (json != null) {
            movies = extractMoviesFromJson(json);
        }
        return movies;
    }

    @NonNull
    private static ArrayList<Movie> extractMoviesFromJson(@NonNull String json) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray results = root.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);

                // basic information
                int id = result.getInt("id");
                String title = result.getString("title");
                float averageVote = (float) result.getDouble("vote_average");
                int voteCount = result.getInt("vote_count");
                String posterPath = result.getString("poster_path");
                String backdropPath = result.getString("backdrop_path");
                String overview = result.getString("overview");

                // release date
                String releaseDate = result.getString("release_date");
                long releaseDateMillis = TimeUtils.getMillis(releaseDate, "YYYY-MM-dd");

                // genre IDs
                JSONArray genreIdsArray = result.getJSONArray("genre_ids");
                int[] genreIds = new int[genreIdsArray.length()];
                for (int genreIndex = 0; genreIndex < genreIdsArray.length(); genreIndex++) {
                    genreIds[genreIndex] = genreIdsArray.getInt(genreIndex);
                }

                // casts
                Actor[] casts = null;
                if (i < 1) {
                    ArrayList<Actor> castArrayList = findCastById(id);
                    casts = castArrayList.toArray(new Actor[castArrayList.size()]);
                }

                Movie movie = new Movie(id, title)
                        .addVote(averageVote, voteCount)
                        .addImages(posterPath, backdropPath)
                        .addGenreIds(genreIds)
                        .addOverview(overview)
                        .addReleaseDate(releaseDateMillis)
                        .addCast(casts);

                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @NonNull
    public static ArrayList<Actor> findCastById(int movieId) {
        String queryUrl = getCreditQueryUrl(movieId);
        String json = getJsonResponse(queryUrl);

        ArrayList<Actor> cast = new ArrayList<>();
        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);
                cast = extractCastFromJson(root);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return cast;
    }

    @NonNull
    private static ArrayList<Actor> extractCastFromJson(@NonNull JSONObject root)
            throws JSONException {
        JSONArray castArray = root.getJSONArray("cast");
        Actor[] cast = new Actor[castArray.length()];
        for (int i = 0; i < castArray.length(); i++) {
            JSONObject actor = castArray.getJSONObject(i);
            int id = actor.getInt("id");
            String name = actor.getString("name");
            String character = actor.getString("character");
            String profilePath = actor.getString("profile_path");

            cast[i] = new Actor(id, name, character, profilePath);
        }

        return new ArrayList<>(Arrays.asList(cast));
    }

    @NonNull
    public static ArrayList<CrewMember> findCrewById(int movieId) {
        String queryUrl = getCreditQueryUrl(movieId);
        String json = getJsonResponse(queryUrl);

        ArrayList<CrewMember> crew = new ArrayList<>();
        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);
                crew = extractCrewFromJson(root);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return crew;
    }

    @NonNull
    private static ArrayList<CrewMember> extractCrewFromJson(@NonNull JSONObject root)
            throws JSONException {

        JSONArray castArray = root.getJSONArray("crew");
        CrewMember[] crew = new CrewMember[castArray.length()];
        for (int i = 0; i < castArray.length(); i++) {
            JSONObject actor = castArray.getJSONObject(i);
            int id = actor.getInt("id");
            String name = actor.getString("name");
            String profilePath = actor.getString("profile_path");
            String department = actor.getString("department");
            String job = actor.getString("job");

            crew[i] = new CrewMember(id, name, profilePath, department, job);
        }

        return new ArrayList<>(Arrays.asList(crew));
    }

    @Nullable
    public static MovieExtra findExtraById(int movieId) {
        String queryUrl = getExtraQueryUrl(movieId);
        String json = getJsonResponse(queryUrl);

        MovieExtra movieExtra = null;
        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);
                movieExtra = extractExtraFromJson(root);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movieExtra;
    }

    @NonNull
    private static MovieExtra extractExtraFromJson(@NonNull JSONObject root)
            throws JSONException {
        String tagline = root.getString("tagline");
        int runtime = root.getInt("runtime");

        // genres
        JSONArray genresArray = root.getJSONArray("genres");
        String[] genres = new String[genresArray.length()];
        for (int i = 0; i < genresArray.length(); i++) {
            JSONObject genre = genresArray.getJSONObject(i);
            genres[i] = genre.getString("name");
            if (genres[i].equals("Science Fiction")) {
                genres[i] = "Sci-Fi";
            }
        }

        // production countries
        JSONArray countriesArray = root.getJSONArray("production_countries");
        String[] countries = new String[countriesArray.length()];
        for (int i = 0; i < countriesArray.length(); i++) {
            JSONObject genre = countriesArray.getJSONObject(i);
            countries[i] = genre.getString("name");
        }

        // spoken languages
        JSONArray languagesArray = root.getJSONArray("spoken_languages");
        String[] languages = new String[languagesArray.length()];
        for (int i = 0; i < languagesArray.length(); i++) {
            JSONObject genre = languagesArray.getJSONObject(i);
            languages[i] = genre.getString("name");
        }

        return  new MovieExtra(runtime, tagline, genres, countries, languages);
    }

    @Nullable
    public static FullMovie findFullMovieById(int movieId) {
        String queryUrl = getFullInfoQueryUrl(movieId);
        String json = getJsonResponse(queryUrl);

        FullMovie fullMovie = null;
        if (json != null) {
            try {
                JSONObject root = new JSONObject(json);

                // TODO: 2/7/18 why do we have to use ArrayList?
                JSONObject creditsRoot = root.getJSONObject("credits");
                ArrayList<Actor> cast = extractCastFromJson(creditsRoot);
                ArrayList<CrewMember> crew = extractCrewFromJson(creditsRoot);

                MovieExtra movieExtra = extractExtraFromJson(root);
                String classification = extractClassificationFromJson(root);
                movieExtra.setClassification(classification);

                String[] backdrops = extractBackdropsFromJson(root);

                fullMovie = new FullMovie()
                        .addCast(cast)
                        .addCrew(crew)
                        .addExtra(movieExtra)
                        .addBackdrops(backdrops);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return fullMovie;
    }

    @NonNull
    private static String extractClassificationFromJson(@NonNull JSONObject root)
            throws JSONException {
        JSONObject releaseDates = root.getJSONObject("release_dates");
        JSONArray results = releaseDates.getJSONArray("results");
        String nationCode = Locale.getDefault().getCountry().toUpperCase();

        String certification = "";
        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            String iso_3166_1 = result.getString("iso_3166_1");
            if (iso_3166_1.equals(nationCode) ||
                    (iso_3166_1.equals("US") && certification == null)) {
                certification = result.getJSONArray("release_dates")
                        .getJSONObject(0).getString("certification");
            }
        }

        return certification;
    }

    @NonNull
    private static String[] extractBackdropsFromJson(@NonNull JSONObject root)
            throws JSONException {
        JSONArray backdrops = root.getJSONObject("images").getJSONArray("backdrops");
        int totalBackdrops = Math.min(backdrops.length(), 4);
        String[] paths = new String[totalBackdrops];
        for (int i = 0; i < totalBackdrops; i++) {
            JSONObject backdrop = backdrops.getJSONObject(i);
            paths[i] = backdrop.getString("file_path");
        }
        return paths;
    }

    @NonNull
    public static String getImageUrl(@NonNull String path) {
        // TODO: 1/19/18 do something with static resolution
        return TmdbUtils.IMAGE_URL + "w500" + path;
    }
}
