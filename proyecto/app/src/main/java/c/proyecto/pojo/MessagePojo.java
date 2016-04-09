package c.proyecto.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import c.proyecto.interfaces.IMessageAdapter;
import c.proyecto.mvp_models.Usuario;

/**
 * Created by Cristina on 23/03/2016.
 */
public class MessagePojo implements Parcelable, IMessageAdapter {

    public static final int TIPO_ENVIADO = 2;
    public static final int TIPO_RECIBIDO = 4;

    private String key, tituloAnuncio, contenido, keyReceptor;
    private Usuario emisor;
    private Date fecha;

    public MessagePojo(){

    }

    public MessagePojo(Usuario emisor, String tituloAnuncio, String contenido, Date fehca){
        this.fecha = fehca;
        this.emisor = emisor;
        this.tituloAnuncio = tituloAnuncio;
        this.contenido = contenido;
    }

    @Override
    public int getType(String keyCurrentUser){
        if (emisor.getKey().equals(keyCurrentUser))
            return TIPO_ENVIADO;
        else
            return TIPO_RECIBIDO;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
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

    public String getKeyReceptor() {
        return keyReceptor;
    }

    public void setKeyReceptor(String keyReceptor) {
        this.keyReceptor = keyReceptor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.tituloAnuncio);
        dest.writeString(this.contenido);
        dest.writeString(this.keyReceptor);
        dest.writeParcelable(this.emisor, 0);
        dest.writeLong(fecha != null ? fecha.getTime() : -1);
    }

    protected MessagePojo(Parcel in) {
        this.key = in.readString();
        this.tituloAnuncio = in.readString();
        this.contenido = in.readString();
        this.keyReceptor = in.readString();
        this.emisor = in.readParcelable(Usuario.class.getClassLoader());
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
