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
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import c.proyecto.R;
import c.proyecto.pojo.Prestacion;
import c.proyecto.presenters.MainPresenter;


/**
 * Created by Cristina on 20/03/2016.
 */
public class Anuncio implements Parcelable {

    private static final String URL_ANUNCIOS = "https://proyectofinaldam.firebaseio.com/anuncios/";
    private static final String URL_SOLICITUDES = "https://proyectofinaldam.firebaseio.com/solicitudes/";

    private String key, titulo, tipo_vivienda, anunciante, direccion, poblacion, provincia, descripcion, numero;
    private int habitaciones_o_camas, numero_banios, tamanio, precio;
    private ArrayList<String> imagenes;
    private ArrayList<Prestacion> prestaciones;
    private HashMap<String, Boolean> solicitantes;
    private static boolean userSubRemoved = false;
    private static Firebase mFirebase;
    private static ChildEventListener listener;

    public Anuncio() {
        imagenes = new ArrayList<>();
        prestaciones = new ArrayList<>();
        solicitantes = new HashMap<>();
        //paNuevoAnuncioPruebas();
    }

    public Anuncio(String key) {
        this();
        this.key = key;
    }

    private void paNuevoAnuncioPruebas() {
        titulo = "tituloAnuncio";
        tipo_vivienda = "casa";
        anunciante = "u-101010";
        direccion = "direccion vivienda";
        poblacion = "poblacion vivienda";
        provincia = "provincia";
        descripcion = "muy confortable";
        habitaciones_o_camas = 3;
        numero_banios = 2;
        tamanio = 100;
        prestaciones.add(new Prestacion(R.drawable.apto_mascotas, "apto mascotas"));
        prestaciones.add(new Prestacion(R.drawable.aire_acondicionado, "aire acondicionado"));
        prestaciones.add(new Prestacion(R.drawable.ascensor, "ascensor"));
        prestaciones.add(new Prestacion(R.drawable.calefaccion, "calefacción"));
        solicitantes.put("12052659", true);
        precio = 315;
    }

    public static Anuncio createNewAnuncio(String anunciante, String titulo, String direccion, String numero, String poblacion, String provincia) {
        String key = anunciante + "_" + String.valueOf(titulo.hashCode() + direccion.hashCode() + numero.hashCode() + poblacion.hashCode() + provincia.hashCode());
        Anuncio a = new Anuncio(key);
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + key + "/");
        mFirebase.setValue(a);
        return a;
    }

    public String generateKey(){
        String r = this.anunciante + "_" + String.valueOf(this.titulo.hashCode() + this.direccion.hashCode() + this.numero.hashCode() + this.poblacion.hashCode() + this.provincia.hashCode());
        return r;
    }

    public static void publishNewAdvert(Anuncio a){
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.key + "/");
        mFirebase.setValue(a);
    }

    public static void initializeFirebaseListeners(final MainPresenter presentador, final Usuario u) {
        Firebase firebaseSubs = new Firebase(URL_SOLICITUDES).child(u.getKey());
        final Firebase firebaseAdvertSub = new Firebase(URL_ANUNCIOS);
        firebaseSubs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String subKey = data.getKey();
                    firebaseAdvertSub.child(subKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            presentador.subHasBeenObtained(dataSnapshot.getValue(Anuncio.class));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (listener == null && mFirebase == null) {
            mFirebase = new Firebase(URL_ANUNCIOS);
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(u.getKey()))
                        presentador.userAdvertHasBeenObtained(a);
                    else if (!a.solicitantes.containsKey(u.getKey()))
                        presentador.advertHasBeenObtained(a);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(u.getKey()))
                        presentador.userAdvertHasBeenModified(a);
                    else if (!userSubRemoved && a.solicitantes.containsKey(u.getKey()))
                        presentador.subHasBeenModified(a);
                    else if (userSubRemoved)
                        presentador.advertHasBeenObtained(a);
                    else
                        presentador.adverHasBeenModified(a);

                    userSubRemoved = false;
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (a.solicitantes.containsKey(u.getKey()))
                        presentador.removeSub(a);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
        }
        mFirebase.addChildEventListener(listener);
    }

    public static void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        listener = null;
        mFirebase = null;
    }

    public static void removeUserAdvert(Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(null);
    }

    public static void removeUserSub(Anuncio a, Usuario u) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey()).child("solicitantes").child(u.getKey());
        mFirebase.setValue(null);
        mFirebase = new Firebase(URL_SOLICITUDES).child(u.getKey()).child(a.getKey());
        mFirebase.setValue(null);
        userSubRemoved = true;
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

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
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
        dest.writeStringList(this.imagenes);
        dest.writeTypedList(prestaciones);
        dest.writeSerializable(this.solicitantes);
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
        this.imagenes = in.createStringArrayList();
        this.prestaciones = in.createTypedArrayList(Prestacion.CREATOR);
        this.solicitantes = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Creator<Anuncio> CREATOR = new Creator<Anuncio>() {
        public Anuncio createFromParcel(Parcel source) {
            return new Anuncio(source);
        }

        public Anuncio[] newArray(int size) {
            return new Anuncio[size];
        }
    };
}

