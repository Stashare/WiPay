package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 02/04/2017.
 */

public class ExistingHoti {

    private String hotspotId;
    private String hotspotName;
    private String url;

    public ExistingHoti() {
    }

    public void setHotspotId(String hotspotId) {
        this.hotspotId = hotspotId;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHotspotId() {

        return hotspotId;
    }

    public String getHotspotName() {
        return hotspotName;
    }

    public String getUrl() {
        return url;
    }

    public ExistingHoti(String hotspotId, String hotspotName, String url) {

        this.hotspotId = hotspotId;
        this.hotspotName = hotspotName;
        this.url = url;
    }
}
