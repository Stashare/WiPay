package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 02/04/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties


public class CheckWifiExistance {

    private String hotspotId;
    private String hotspotName;

    public CheckWifiExistance(){
        //this constructor is required
    }


    public CheckWifiExistance(String hotspotId, String hotspotName) {
        this.hotspotId = hotspotId;
        this.hotspotName = hotspotName;
    }

    public void setHotspotId(String hotspotId) {
        this.hotspotId = hotspotId;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public String getHotspotId() {

        return hotspotId;
    }

    public String getHotspotName() {
        return hotspotName;
    }
}
