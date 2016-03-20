package c.proyecto.models;

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
public class Usuario {

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
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/usuarios/usuario-"+email.hashCode()+"/");
        Usuario u = new Usuario(email, contra);
        mFirebase.setValue(u);
        return u;
    }

    public static void signIn(String email, final String contra, final InicioPresenter presentador){
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/usuarios/usuario-"+email.hashCode()+"/");
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
}
