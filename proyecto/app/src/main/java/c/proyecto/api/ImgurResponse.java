package c.proyecto.api;

/**
 * Created by aleja on 24/03/2016.
 */
public class ImgurResponse {
    private int data;
    private int status;
    private boolean success;

    public ImgurResponse() {
    }

    public ImgurResponse(int data, int status, boolean success) {
        this.data = data;
        this.status = status;
        this.success = success;
    }

    public int isData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
