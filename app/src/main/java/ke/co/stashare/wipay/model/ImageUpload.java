package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 02/04/2017.
 */

public class ImageUpload {


    private String hotspotName;
    private String url;

    public ImageUpload(){
        //this constructor is required
    }

    public ImageUpload( String hotspotName, String url) {

        this.hotspotName = hotspotName;
        this.url = url;
    }


    public String getHotspotName() {
        return hotspotName;
    }

    public String getUrl() {
        return url;
    }


    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
