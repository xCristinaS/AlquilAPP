package c.proyecto.pojo;

import java.util.Date;

/**
 * Created by Cristina on 23/03/2016.
 */
public class MessagePojo {

    private String nombreEmisor, fotoEmisor, tituloAnuncio, contenido;
    private Date fecha;

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
}
