package c.proyecto.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;

import c.proyecto.interfaces.MyModel;

public class Anuncio implements Parcelable, MyModel {

    private String key, titulo, tipo_vivienda, anunciante, direccion, poblacion, provincia, descripcion, numero;
    private int habitaciones_o_camas, numero_banios, tamanio, precio;
    private HashMap<String, String> imagenes;
    private ArrayList<Prestacion> prestaciones;
    private HashMap<String, Boolean> solicitantes;
    private LatLng lats;

    public Anuncio() {
        imagenes = new HashMap<>();
        prestaciones = new ArrayList<>();
        solicitantes = new HashMap<>();
    }

    public String generateKey() {
        if (key == null)
            return anunciante + "_" + String.valueOf(titulo.trim().hashCode() + direccion.trim().hashCode() + numero.hashCode() + poblacion.trim().hashCode() + provincia.trim().hashCode());
        else
            return key;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo_vivienda() {
        return tipo_vivienda;
    }

    public void setTipo_vivienda(String tipo_vivienda) {
        this.tipo_vivienda = tipo_vivienda;
    }

    public String getAnunciante() {
        return anunciante;
    }

    public void setAnunciante(String anunciante) {
        this.anunciante = anunciante;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getHabitaciones_o_camas() {
        return habitaciones_o_camas;
    }

    public void setHabitaciones_o_camas(int habitaciones_o_camas) {
        this.habitaciones_o_camas = habitaciones_o_camas;
    }

    public int getNumero_banios() {
        return numero_banios;
    }

    public void setNumero_banios(int numero_banios) {
        this.numero_banios = numero_banios;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public HashMap<String, String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(HashMap<String, String> imagenes) {
        this.imagenes = imagenes;
    }

    public ArrayList<Prestacion> getPrestaciones() {
        return prestaciones;
    }

    public void setPrestaciones(ArrayList<Prestacion> prestaciones) {
        this.prestaciones = prestaciones;
    }

    public HashMap<String, Boolean> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(HashMap<String, Boolean> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LatLng getLats() {
        return lats;
    }

    public void setLats(LatLng lats) {
        this.lats = lats;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.titulo);
        dest.writeString(this.tipo_vivienda);
        dest.writeString(this.anunciante);
        dest.writeString(this.direccion);
        dest.writeString(this.poblacion);
        dest.writeString(this.provincia);
        dest.writeString(this.descripcion);
        dest.writeString(this.numero);
        dest.writeInt(this.habitaciones_o_camas);
        dest.writeInt(this.numero_banios);
        dest.writeInt(this.tamanio);
        dest.writeInt(this.precio);
        dest.writeSerializable(this.imagenes);
        dest.writeTypedList(prestaciones);
        dest.writeSerializable(this.solicitantes);
        dest.writeParcelable(this.lats, flags);
    }

    protected Anuncio(Parcel in) {
        this.key = in.readString();
        this.titulo = in.readString();
        this.tipo_vivienda = in.readString();
        this.anunciante = in.readString();
        this.direccion = in.readString();
        this.poblacion = in.readString();
        this.provincia = in.readString();
        this.descripcion = in.readString();
        this.numero = in.readString();
        this.habitaciones_o_camas = in.readInt();
        this.numero_banios = in.readInt();
        this.tamanio = in.readInt();
        this.precio = in.readInt();
        this.imagenes = (HashMap<String, String>) in.readSerializable();
        this.prestaciones = in.createTypedArrayList(Prestacion.CREATOR);
        this.solicitantes = (HashMap<String, Boolean>) in.readSerializable();
        this.lats = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Anuncio> CREATOR = new Creator<Anuncio>() {
        @Override
        public Anuncio createFromParcel(Parcel source) {
            return new Anuncio(source);
        }

        @Override
        public Anuncio[] newArray(int size) {
            return new Anuncio[size];
        }
    };
}

