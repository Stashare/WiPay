package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 01/04/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Upload {

    public String name;
    private String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String url) {
        this.name = name;
        this.url= url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

}
