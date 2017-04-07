package ke.co.stashare.wipay.model;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by Ken Wainaina on 04/04/2017.
 */

public class WooiLEVEL {
    private List<HotSpotDetails> results;
    private int url;

    public WooiLEVEL(List<HotSpotDetails> results, int url) {
        this.results = results;
        this.url = url;
    }

    public List<HotSpotDetails> getResults() {
        return results;
    }

    public int getUrl() {
        return url;
    }

    public void setResults(List<HotSpotDetails> results) {
        this.results = results;
    }

    public void setUrl(int url) {
        this.url = url;
    }
}
