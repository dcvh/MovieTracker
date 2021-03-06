package tcd.android.com.movietracker.Entities.Credit;

/**
 * Created by ADMIN on 28/10/2017.
 */

public class CrewMember extends Credit {

    private String mDepartment;
    private String mJob;

    public CrewMember(int id, String name, String profilePath, String department, String job) {
        super(id, name, profilePath);
        mDepartment = department;
        mJob = job;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public String getJob() {
        return mJob;
    }
}
