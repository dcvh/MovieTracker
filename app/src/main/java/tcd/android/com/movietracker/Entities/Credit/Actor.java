package tcd.android.com.movietracker.Entities.Credit;

/**
 * Created by ADMIN on 28/10/2017.
 */

public class Actor extends Credit {

    private String mCharacter;

    public Actor(int id, String name, String character, String profilePath) {
        super(id, name, profilePath);
        this.mCharacter = character;
    }

    public String getCharacter() {
        return mCharacter;
    }
}
