package ke.co.stashare.wipay.model;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Ken Wainaina on 02/04/2017.
 */

public class PayMethList {

    private List<String> results;

    public PayMethList(List<String> results) {
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
