package c.proyecto.pojo;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by OmniumMac04 on 6/4/16.
 */
public class ImagePojo {
    private ImageView imgView;
    private ProgressBar prb;
    private String nameFile;
    private String url;
    private Bitmap bitmap;
    private int numImageView;

    public ImagePojo(ImageView imgView, ProgressBar prb, String nameFile, String url, int numImageView) {
        this.imgView = imgView;
        this.prb = prb;
        this.nameFile = nameFile;
        this.url = url;
        this.numImageView = numImageView;
    }

    public ImageView getImgView() {
        return imgView;
    }

    public void setImgView(ImageView imgView) {
        this.imgView = imgView;
    }

    public ProgressBar getPrb() {
        return prb;
    }

    public void setPrb(ProgressBar prb) {
        this.prb = prb;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getNumImageView() {
        return numImageView;
    }

    public void setNumImageView(int numImageView) {
        this.numImageView = numImageView;
    }
}
