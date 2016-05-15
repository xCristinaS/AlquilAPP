package c.proyecto.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.interfaces.MyModel;

public class Usuario implements Parcelable, MyModel {

    private String key, email, nombre, apellidos, nacionalidad, profesion, comentario_desc, foto;
    private int ordenado, fiestero, sociable, activo;
    private long fecha_nacimiento;
    private ArrayList<String> itemsHabitos;
    private ArrayList<String> idDrawItemsDescriptivos;

    public Usuario() {
        itemsHabitos = new ArrayList<>();
        idDrawItemsDescriptivos = new ArrayList<>();
        comentario_desc = "";
    }

    public Usuario(String email, String nombre, String apellidos, String key) {
        this();
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getComentario_desc() {
        return comentario_desc;
    }

    public void setComentario_desc(String comentario_desc) {
        this.comentario_desc = comentario_desc;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public long getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(long fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getOrdenado() {
        return ordenado;
    }

    public void setOrdenado(int ordenado) {
        this.ordenado = ordenado;
    }

    public int getFiestero() {
        return fiestero;
    }

    public void setFiestero(int fiestero) {
        this.fiestero = fiestero;
    }

    public ArrayList<String> getIdDrawItemsDescriptivos() {
        return idDrawItemsDescriptivos;
    }

    public void setIdDrawItemsDescriptivos(ArrayList<String> idDrawItemsDescriptivos) {
        this.idDrawItemsDescriptivos = idDrawItemsDescriptivos;
    }

    public int getSociable() {
        return sociable;
    }

    public void setSociable(int sociable) {
        this.sociable = sociable;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }



    public ArrayList<String> getItemsHabitos() {
        return itemsHabitos;
    }

    public void setItemsHabitos(ArrayList<String> itemsHabitos) {
        this.itemsHabitos = itemsHabitos;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //      PARCELABLE


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.email);
        dest.writeString(this.nombre);
        dest.writeString(this.apellidos);
        dest.writeString(this.nacionalidad);
        dest.writeString(this.profesion);
        dest.writeString(this.comentario_desc);
        dest.writeString(this.foto);
        dest.writeInt(this.ordenado);
        dest.writeInt(this.fiestero);
        dest.writeInt(this.sociable);
        dest.writeInt(this.activo);
        dest.writeLong(this.fecha_nacimiento);
        dest.writeStringList(this.itemsHabitos);
        dest.writeStringList(this.idDrawItemsDescriptivos);
    }

    protected Usuario(Parcel in) {
        this.key = in.readString();
        this.email = in.readString();
        this.nombre = in.readString();
        this.apellidos = in.readString();
        this.nacionalidad = in.readString();
        this.profesion = in.readString();
        this.comentario_desc = in.readString();
        this.foto = in.readString();
        this.ordenado = in.readInt();
        this.fiestero = in.readInt();
        this.sociable = in.readInt();
        this.activo = in.readInt();
        this.fecha_nacimiento = in.readLong();
        this.itemsHabitos = in.createStringArrayList();
        this.idDrawItemsDescriptivos = in.createStringArrayList();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
