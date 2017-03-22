package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 20/03/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Hotspots {
    private String hotspotId;
    private String hotspotName;
    private String paybill;

    public Hotspots(){
        //this constructor is required
    }

    public Hotspots(String hotspotId, String hotspotName,String paybill ) {
        this.hotspotId = hotspotId;
        this.hotspotName = hotspotName;
        this.paybill= paybill;
    }

    public String getHotspotId() {
        return hotspotId;
    }

    public String getHotspotName() {
        return hotspotName;
    }

    public String getPaybill() {
        return paybill;
    }

    public void setHotspotId(String hotspotId) {
        this.hotspotId = hotspotId;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public void setPaybill(String paybill) {
        this.paybill = paybill;
    }
}
