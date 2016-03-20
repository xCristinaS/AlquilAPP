package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Cristina on 20/03/2016.
 */
public class Anuncio implements Parcelable {

    private String tipo_vivienda, anunciante, direccion, numero, poblacion, provincia, descripcion;
    private int hab_o_camas, numBanios, tamanio;
    private ArrayList<String> imagenes, prestaciones;
    private HashMap<String, Boolean> solicitantes;

	public Anuncio(){

	}

	public static ArrayList<Anuncio> getAdverts() {
		final ArrayList<Anuncio> anuncios = new ArrayList<>();
		Firebase mFirebase = new Firebase("https://proyectofinaldam.firebaseio.com/anuncios/");
		mFirebase.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Anuncio a = dataSnapshot.getValue(Anuncio.class);
				anuncios.add(a);
			}

			@Override
			public void onCancelled(FirebaseError firebaseError) {

			}
		});
		return anuncios;
	}

	public static ArrayList<Anuncio> getAllUserSubs(Usuario usuario) {
		return null;
	}

	public static ArrayList<Anuncio> getAllUserPublishAdverts(Usuario usuario) {
		return null;
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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

	public int getHab_o_camas() {
		return hab_o_camas;
	}

	public void setHab_o_camas(int hab_o_camas) {
		this.hab_o_camas = hab_o_camas;
	}

	public int getNumBanios() {
		return numBanios;
	}

	public void setNumBanios(int numBanios) {
		this.numBanios = numBanios;
	}

	public int getTamanio() {
		return tamanio;
	}

	public void setTamanio(int tamanio) {
		this.tamanio = tamanio;
	}

	public ArrayList<String> getImagenes() {
		return imagenes;
	}

	public void setImagenes(ArrayList<String> imagenes) {
		this.imagenes = imagenes;
	}

	public ArrayList<String> getPrestaciones() {
		return prestaciones;
	}

	public void setPrestaciones(ArrayList<String> prestaciones) {
		this.prestaciones = prestaciones;
	}

	public HashMap<String, Boolean> getSolicitantes() {
		return solicitantes;
	}

	public void setSolicitantes(HashMap<String, Boolean> solicitantes) {
		this.solicitantes = solicitantes;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.tipo_vivienda);
		dest.writeString(this.anunciante);
		dest.writeString(this.direccion);
		dest.writeString(this.numero);
		dest.writeString(this.poblacion);
		dest.writeString(this.provincia);
		dest.writeString(this.descripcion);
		dest.writeInt(this.hab_o_camas);
		dest.writeInt(this.numBanios);
		dest.writeInt(this.tamanio);
		dest.writeStringList(this.imagenes);
		dest.writeStringList(this.prestaciones);
		dest.writeSerializable(this.solicitantes);
	}

	protected Anuncio(Parcel in) {
		this.tipo_vivienda = in.readString();
		this.anunciante = in.readString();
		this.direccion = in.readString();
		this.numero = in.readString();
		this.poblacion = in.readString();
		this.provincia = in.readString();
		this.descripcion = in.readString();
		this.hab_o_camas = in.readInt();
		this.numBanios = in.readInt();
		this.tamanio = in.readInt();
		this.imagenes = in.createStringArrayList();
		this.prestaciones = in.createStringArrayList();
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
