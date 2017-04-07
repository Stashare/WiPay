package ke.co.stashare.wipay.model;

/**
 * Created by Ken Wainaina on 06/04/2017.
 */

public class Home {

    private int image;
    private int image_bcg;
    private String title;
    private String desc;


    public Home(int image, int image_bcg,String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc= desc;
        this.image_bcg = image_bcg;

    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {

        return desc;
    }

    public void setImage_bcg(int image_bcg) {
        this.image_bcg = image_bcg;
    }

    public int getImage_bcg() {

        return image_bcg;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
