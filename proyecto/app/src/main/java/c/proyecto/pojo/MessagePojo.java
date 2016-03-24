package c.proyecto.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Cristina on 23/03/2016.
 */
public class MessagePojo implements Parcelable {

    private String nombreEmisor, fotoEmisor, tituloAnuncio, contenido;
    private Date fecha;

    public MessagePojo(){

    }

    public MessagePojo(String nombreEmisor, String fotoEmisor, String tituloAnuncio, String contenido, Date fehca){
        this.fecha = fehca;
        this.nombreEmisor = nombreEmisor;
        this.fotoEmisor = fotoEmisor;
        this.tituloAnuncio = tituloAnuncio;
        this.contenido = contenido;
    }

    public String getNombreEmisor() {
        return nombreEmisor;
    }

    public void setNombreEmisor(String nombreEmisor) {
        this.nombreEmisor = nombreEmisor;
    }

    public String getFotoEmisor() {
        return fotoEmisor;
    }

    public void setFotoEmisor(String fotoEmisor) {
        this.fotoEmisor = fotoEmisor;
    }

    public String getTituloAnuncio() {
        return tituloAnuncio;
    }

    public void setTituloAnuncio(String tituloAnuncio) {
        this.tituloAnuncio = tituloAnuncio;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombreEmisor);
        dest.writeString(this.fotoEmisor);
        dest.writeString(this.tituloAnuncio);
        dest.writeString(this.contenido);
        dest.writeLong(fecha != null ? fecha.getTime() : -1);
    }

    protected MessagePojo(Parcel in) {
        this.nombreEmisor = in.readString();
        this.fotoEmisor = in.readString();
        this.tituloAnuncio = in.readString();
        this.contenido = in.readString();
        long tmpFecha = in.readLong();
        this.fecha = tmpFecha == -1 ? null : new Date(tmpFecha);
    }

    public static final Creator<MessagePojo> CREATOR = new Creator<MessagePojo>() {
        public MessagePojo createFromParcel(Parcel source) {
            return new MessagePojo(source);
        }

        public MessagePojo[] newArray(int size) {
            return new MessagePojo[size];
        }
    };
}
