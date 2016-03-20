package c.proyecto.models;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Cristina on 19/03/2016.
 */
public class Usuario {
    private String id, email,contra, nombre, apellidos, nacionalidad, profesion, comentario_desc, foto, fecha_nacimiento;
    private int ordenado, fiestero, sociable, activo;
    private ArrayList<String> itemsDescriptivos, itemsHabitos;

    public Usuario(){
        String sin_esp = "Sin especificar";
        email = sin_esp;
        contra = sin_esp;
        nombre = sin_esp;
        apellidos = sin_esp;
        nacionalidad = sin_esp;
        profesion = sin_esp;
        comentario_desc = sin_esp;
        foto = sin_esp;
        ordenado = 0;
        fiestero = 0;
        sociable = 0;
        activo = 0;
        fecha_nacimiento = sin_esp;
        itemsDescriptivos = new ArrayList<>();
        itemsHabitos = new ArrayList<>();
    }

    public Usuario(String email, String contra) {
        this();
        this.email = email;
        this.contra = contra;
    }

    public static boolean createNewUser(String email, String contra) {
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/usuarios/usuario_"+email.hashCode()+"/");
        Usuario u = new Usuario(email, contra);
        mFirebase.setValue(u);
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
