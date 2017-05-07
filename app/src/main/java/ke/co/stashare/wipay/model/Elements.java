package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 27/03/2017.
 */

public class Elements {

    private String hotspot;
    private String logo;
    private String title;
    private String equity;
    private String mpesa;
    private String desc;

    public void setHotspot(String hotspot) {
        this.hotspot = hotspot;
    }

    public void setEquity(String equity) {
        this.equity = equity;
    }

    public void setMpesa(String mpesa) {
        this.mpesa = mpesa;
    }

    public String getHotspot() {

        return hotspot;
    }

    public String getEquity() {
        return equity;
    }

    public String getMpesa() {
        return mpesa;
    }

    public Elements(String hotspot, String logo, String title, String equity, String mpesa, String desc) {

        this.hotspot = hotspot;
        this.logo = logo;
        this.title = title;
        this.equity = equity;
        this.mpesa = mpesa;
        this.desc = desc;
    }

    public String getLogo() {
        return logo;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
