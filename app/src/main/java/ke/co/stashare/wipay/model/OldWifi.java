package ke.co.stashare.wipay.model;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Ken Wainaina on 23/03/2017.
 */

public class OldWifi {

    private List<ScanResult> results;

    public OldWifi(List<ScanResult> results) {
        this.results = results;
    }

    public List<ScanResult> getResults() {
        return results;
    }

    public void setResults(List<ScanResult> results) {
        this.results = results;
    }
}
