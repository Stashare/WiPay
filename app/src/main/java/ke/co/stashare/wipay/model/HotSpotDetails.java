package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 01/04/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties


public class HotSpotDetails {

    private String hotspotId;
    private String hotspotName;
    private String mpesa_paybill;
    private String equity_acc;
    private String airtel_paybill;
    private String orange_paybill;
    private String location;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public HotSpotDetails(){
        //this constructor is required
    }

    public HotSpotDetails(String hotspotId, String hotspotName, String mpesa_paybill,
                          String equity_acc, String airtel_paybill, String orange_paybill,String location,String url ) {

        this.hotspotId = hotspotId;
        this.hotspotName = hotspotName;
        this.mpesa_paybill = mpesa_paybill;
        this.equity_acc = equity_acc;
        this.airtel_paybill = airtel_paybill;
        this.orange_paybill = orange_paybill;
        this.location = location;
        this.url= url;
    }

    public String getLocation() {
        return location;
    }

    public String getHotspotId() {
        return hotspotId;
    }

    public String getHotspotName() {
        return hotspotName;
    }

    public String getMpesa_paybill() {
        return mpesa_paybill;
    }

    public String getEquity_acc() {
        return equity_acc;
    }

    public String getAirtel_paybill() {
        return airtel_paybill;
    }

    public String getOrange_paybill() {
        return orange_paybill;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public void setHotspotId(String hotspotId) {
        this.hotspotId = hotspotId;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public void setMpesa_paybill(String mpesa_paybill) {
        this.mpesa_paybill = mpesa_paybill;
    }

    public void setEquity_acc(String equity_acc) {
        this.equity_acc = equity_acc;
    }

    public void setAirtel_paybill(String airtel_paybill) {
        this.airtel_paybill = airtel_paybill;
    }

    public void setOrange_paybill(String orange_paybill) {
        this.orange_paybill = orange_paybill;
    }
}
