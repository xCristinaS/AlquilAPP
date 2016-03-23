package c.proyecto.models;

import java.util.Date;

/**
 * Created by Cristina on 23/03/2016.
 */
public class Message {

    private long fecha;
    private String contenido;

    public Message(){

    }

    public Message(Date fecha, String contenido){
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
}
