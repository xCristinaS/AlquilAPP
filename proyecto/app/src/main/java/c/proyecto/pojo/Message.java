package c.proyecto.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import c.proyecto.interfaces.MyModel;

public class Message implements Parcelable, MyModel {

    private long fecha;
    private String contenido;

    public Message() {

    }

    public Message(Date fecha, String contenido) {
        this.fecha = fecha.getTime();
        this.contenido = contenido;
    }


    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.fecha);
        dest.writeString(this.contenido);
    }

    protected Message(Parcel in) {
        this.fecha = in.readLong();
        this.contenido = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
