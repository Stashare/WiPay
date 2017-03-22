package ke.co.stashare.wipay.helper;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Ken Wainaina on 19/03/2017.
 */

public class User {

    private List<ScanResult> results;

    public User(List<ScanResult> results) {
        this.results = results;
    }

    public List<ScanResult> getResults() {
        return results;
    }

    public void setResults(List<ScanResult> results) {
        this.results = results;
    }
}
