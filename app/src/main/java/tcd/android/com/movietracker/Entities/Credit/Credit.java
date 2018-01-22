package tcd.android.com.movietracker.Entities.Credit;

import java.io.Serializable;

/**
 * Created by cpu10661 on 1/19/18.
 */

public class Credit implements Serializable {
    private int mId;
    private String mName;
    private String mProfilePath;

    Credit(int id, String name, String profilePath) {
        this.mId = id;
        this.mName = name;
        this.mProfilePath = profilePath;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getProfilePath() {
        return mProfilePath;
    }
}
