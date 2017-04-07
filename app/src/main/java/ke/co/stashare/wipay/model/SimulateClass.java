package ke.co.stashare.wipay.model;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Ken Wainaina on 03/04/2017.
 */

public class SimulateClass  {

    private List<HotSpotDetails> results;

    public SimulateClass(List<HotSpotDetails> results) {
        this.results = results;
    }

    public List<HotSpotDetails> getResults() {
        return results;
    }

    public void setResults(List<HotSpotDetails> results) {
        this.results = results;
    }
}
