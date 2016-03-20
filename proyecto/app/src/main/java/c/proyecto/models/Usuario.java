package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import c.proyecto.R;
import c.proyecto.presenters.InicioPresenter;

/**
 * Created by Cristina on 19/03/2016.
 */
public class Usuario implements Parcelable{

    private String email,contra, nombre, apellidos, nacionalidad, profesion, comentario_desc, foto, fecha_nacimiento;
    private int ordenado, fiestero, sociable, activo;
    private ArrayList<String> itemsDescriptivos, itemsHabitos;

    public Usuario(){
        foto = "default_user.png";
        ordenado = 0;
        fiestero = 0;
        sociable = 0;
        activo = 0;
        itemsDescriptivos = new ArrayList<>();
        itemsHabitos = new ArrayList<>();
    }

    public Usuario(String email, String contra) {
        this();
        this.email = email;
        this.contra = contra;
    }

    public static Usuario createNewUser(String email, String contra) {
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/usuarios/u-"+email.hashCode()+"/");
        Usuario u = new Usuario(email, contra);
        mFirebase.setValue(u);
        return u;
    }

    public static void signIn(String email, final String contra, final InicioPresenter presentador){
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/usuarios/u-"+email.hashCode()+"/");
        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                if (u.getContra().equals(contra))
                    presentador.onSingInSuccess(u);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
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

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
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

    public ArrayList<String> getItemsDescriptivos() {
        return itemsDescriptivos;
    }

    public void setItemsDescriptivos(ArrayList<String> itemsDescriptivos) {
        this.itemsDescriptivos = itemsDescriptivos;
    }

    public ArrayList<String> getItemsHabitos() {
        return itemsHabitos;
    }

    public void setItemsHabitos(ArrayList<String> itemsHabitos) {
        this.itemsHabitos = itemsHabitos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.contra);
        dest.writeString(this.nombre);
        dest.writeString(this.apellidos);
        dest.writeString(this.nacionalidad);
        dest.writeString(this.profesion);
        dest.writeString(this.comentario_desc);
        dest.writeString(this.foto);
        dest.writeString(this.fecha_nacimiento);
        dest.writeInt(this.ordenado);
        dest.writeInt(this.fiestero);
        dest.writeInt(this.sociable);
        dest.writeInt(this.activo);
        dest.writeStringList(this.itemsDescriptivos);
        dest.writeStringList(this.itemsHabitos);
    }

    protected Usuario(Parcel in) {
        this.email = in.readString();
        this.contra = in.readString();
        this.nombre = in.readString();
        this.apellidos = in.readString();
        this.nacionalidad = in.readString();
        this.profesion = in.readString();
        this.comentario_desc = in.readString();
        this.foto = in.readString();
        this.fecha_nacimiento = in.readString();
        this.ordenado = in.readInt();
        this.fiestero = in.readInt();
        this.sociable = in.readInt();
        this.activo = in.readInt();
        this.itemsDescriptivos = in.createStringArrayList();
        this.itemsHabitos = in.createStringArrayList();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
