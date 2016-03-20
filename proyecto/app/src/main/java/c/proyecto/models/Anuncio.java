package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;

import c.proyecto.R;
import c.proyecto.presenters.MainPresenter;


/**
 * Created by Cristina on 20/03/2016.
 */
public class Anuncio implements Parcelable {

    private String titulo, tipo_vivienda, anunciante, direccion, poblacion, provincia, descripcion;
    private int habitaciones_o_camas, numero_banios, tamanio, numero;
    private ArrayList<String> imagenes;
    private ArrayList<Integer> prestaciones;
    private HashMap<String, Boolean> solicitantes;
    private float precio;

    public Anuncio() {
        titulo = "mi2Anuncio";
        anunciante = "u-141414";
        tipo_vivienda = "piso";
        direccion = "C/ clavel";
        poblacion = "la linea";
        provincia = "Sevilla";
        descripcion = "un piso precioso";
        habitaciones_o_camas = 2;
        numero = 121;
        numero_banios = 2;
        tamanio = 54;
        imagenes = new ArrayList<>();
        prestaciones = new ArrayList<>();
        solicitantes = new HashMap<>();
        imagenes.add("foto1.png");
        imagenes.add("foto2.png");
        prestaciones.add(R.drawable.ascensor);
        prestaciones.add(R.drawable.parking);
        prestaciones.add(R.drawable.lavadora);
        prestaciones.add(R.drawable.prohibido_fumar);
        prestaciones.add(R.drawable.wifi);
        solicitantes.put("u-2483914", true);
        precio = 350;
    }

    public static Anuncio createNewAnuncio() {
        Anuncio a = new Anuncio();
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/anuncios/" + a.anunciante + "_" + a.titulo + "/");
        mFirebase.setValue(a);
        return a;
    }

    public static void getAdverts(final MainPresenter presentador) {
        Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/anuncios/");

        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Anuncio> anuncios = new ArrayList<>();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Anuncio a = data.getValue(Anuncio.class);
                    anuncios.add(a);
                }
                presentador.onAdvertsRequestedResponsed(anuncios);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public ArrayList<Integer> getPrestaciones() {
        return prestaciones;
    }

    public void setPrestaciones(ArrayList<Integer> prestaciones) {
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

    public void setPrecio(float precio) {
        this.precio = precio;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.tipo_vivienda);
        dest.writeString(this.anunciante);
        dest.writeString(this.direccion);
        dest.writeString(this.poblacion);
        dest.writeString(this.provincia);
        dest.writeString(this.descripcion);
        dest.writeInt(this.habitaciones_o_camas);
        dest.writeInt(this.numero_banios);
        dest.writeInt(this.tamanio);
        dest.writeInt(this.numero);
        dest.writeStringList(this.imagenes);
        dest.writeList(this.prestaciones);
        dest.writeSerializable(this.solicitantes);
        dest.writeFloat(this.precio);
    }

    protected Anuncio(Parcel in) {
        this.titulo = in.readString();
        this.tipo_vivienda = in.readString();
        this.anunciante = in.readString();
        this.direccion = in.readString();
        this.poblacion = in.readString();
        this.provincia = in.readString();
        this.descripcion = in.readString();
        this.habitaciones_o_camas = in.readInt();
        this.numero_banios = in.readInt();
        this.tamanio = in.readInt();
        this.numero = in.readInt();
        this.imagenes = in.createStringArrayList();
        this.prestaciones = new ArrayList<Integer>();
        in.readList(this.prestaciones, Integer.class.getClassLoader());
        this.solicitantes = (HashMap<String, Boolean>) in.readSerializable();
        this.precio = in.readFloat();
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
