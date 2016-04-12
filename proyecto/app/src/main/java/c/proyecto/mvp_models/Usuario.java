package c.proyecto.mvp_models;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import c.proyecto.interfaces.MyModel;
import c.proyecto.mvp_presenters.AdvertsDetailsPresenter;
import c.proyecto.mvp_presenters.InicioPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.mvp_presenters.RegistroPresenter;


public class Usuario implements Parcelable, MyModel {

    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";

    private String key, email, contra, nombre, apellidos, nacionalidad, profesion, comentario_desc, foto;
    private int ordenado, fiestero, sociable, activo;
    private long fecha_nacimiento;
    private static Firebase mFirebase;
    private static ValueEventListener listener;
    private ArrayList<String> itemsHabitos;
    private ArrayList<Integer> idDrawItemsDescriptivos;


    public Usuario() {
        itemsHabitos = new ArrayList<>();
        idDrawItemsDescriptivos = new ArrayList<>();
    }

    public Usuario(String email, String contra, String nombre, String apellidos, String key) {
        this();
        this.email = email;
        this.contra = contra;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.key = key;
    }


    public static void initializeOnUserChangedListener(final MainPresenter presenter, Usuario usuario) {
        if (listener != null)
            mFirebase.removeEventListener(listener);
        if (mFirebase == null)
            mFirebase = new Firebase(URL_USERS + usuario.getKey());
        if (listener == null)
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    presenter.userHasBeenModified(dataSnapshot.getValue(Usuario.class));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
        mFirebase.addValueEventListener(listener);
    }

    //Comprueba si existe algún usuario con este usuario.
    public static void amIRegistrered(final String user, final RegistroPresenter presenter) {
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

    public static void getAdvertPublisher(String anunciante, final AdvertsDetailsPresenter presenter) {
        Firebase mFirebase = new Firebase(URL_USERS).child(anunciante);
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                presenter.onAdvertPublisherRequestedResponsed(dataSnapshot.getValue(Usuario.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void updateUserProfile(Usuario u) {
        Firebase mFirebase = new Firebase(URL_USERS + u.key + "/");
        mFirebase.setValue(u);
    }


    public static void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        listener = null;
        mFirebase = null;
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

    public ArrayList<Integer> getIdDrawItemsDescriptivos() {
        return idDrawItemsDescriptivos;
    }

    public void setIdDrawItemsDescriptivos(ArrayList<Integer> idDrawItemsDescriptivos) {
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
        dest.writeString(this.contra);
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
        dest.writeList(this.idDrawItemsDescriptivos);
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
        this.ordenado = in.readInt();
        this.fiestero = in.readInt();
        this.sociable = in.readInt();
        this.activo = in.readInt();
        this.fecha_nacimiento = in.readLong();
        this.itemsHabitos = in.createStringArrayList();
        this.idDrawItemsDescriptivos = new ArrayList<Integer>();
        in.readList(this.idDrawItemsDescriptivos, Integer.class.getClassLoader());
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
