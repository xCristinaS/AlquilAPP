package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import c.proyecto.presenters.InicioPresenter;
import c.proyecto.presenters.RegistroPresenter;

/**
 * Created by Cristina on 19/03/2016.
 */
public class Usuario implements Parcelable{

    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";
    private static boolean registered;

    private String key, email,contra, nombre, apellidos, nacionalidad, profesion, comentario_desc, foto, fecha_nacimiento;
    private int ordenado, fiestero, sociable, activo;
    private ArrayList<String> itemsDescriptivos, itemsHabitos;

    public Usuario(){
        itemsDescriptivos = new ArrayList<>();
        itemsHabitos = new ArrayList<>();
        //paBorrarPruebas();
    }

    private void paBorrarPruebas(){
        foto = "default_user.png";
        ordenado = 0;
        fiestero = 0;
        sociable = 0;
        activo = 0;
    }

    public Usuario(String email, String contra, String nombre, String apellidos, String key) {
        this();
        this.email = email;
        this.contra = contra;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.key = key;
    }

    public static Usuario createNewUser(String email, String contra, String nombre, String apellidos) {
        String key = String.valueOf(email.hashCode()+contra.hashCode()+nombre.hashCode()+apellidos.hashCode());
        Firebase mFirebase = new Firebase(URL_USERS+key+"/");
        Usuario u = new Usuario(email, contra, nombre, apellidos, key);
        mFirebase.setValue(u);
        return u;
    }

    public static void signIn(final String email, final String contra, final InicioPresenter presentador){
        Firebase firebase = new Firebase(URL_USERS);
        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean stop = false;
                Iterator i = dataSnapshot.getChildren().iterator();
                //Recorre todos los usuarios comprobando que el pasado por parámetro existe
                while (i.hasNext() && !stop) {
                    Usuario u = ((DataSnapshot) i.next()).getValue(Usuario.class);
                    if (u.getContra().equals(contra) && u.getEmail().equals(email)) {
                        stop = true;
                        presentador.onSingInResponsed(u);
                    }
                }
                //Si no ha encontrado ninguna semejanza con los usuarios de la BDD devolverá null
                if (!stop)
                    presentador.onSingInResponsed(null);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    //Comprueba si existe algún usuario con este usuario.
    public static void amIRegistered(final String user, final RegistroPresenter presenter){
        Firebase firebase = new Firebase(URL_USERS);

        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean existe = false;
                Iterator i = dataSnapshot.getChildren().iterator();
                //Recorre todos los usuarios comprobando si existe un usuario con ese Email
                while (i.hasNext() && !existe) {
                    Usuario u = ((DataSnapshot) i.next()).getValue(Usuario.class);
                    if (u.getEmail().equals(user))
                        existe = true;
                }

                presenter.onCheckUserExist(existe);
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
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
        this.key = in.readString();
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
