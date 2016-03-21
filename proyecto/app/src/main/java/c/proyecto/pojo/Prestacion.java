package c.proyecto.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class Prestacion implements Parcelable{

    private int idDrawable;
    private String nombre;

    public Prestacion(){

    }
    public Prestacion(int idDrawable, String nombre) {
        this.idDrawable = idDrawable;
        this.nombre = nombre;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idDrawable);
        dest.writeString(this.nombre);
    }

    protected Prestacion(Parcel in) {
        this.idDrawable = in.readInt();
        this.nombre = in.readString();
    }

    public static final Creator<Prestacion> CREATOR = new Creator<Prestacion>() {
        public Prestacion createFromParcel(Parcel source) {
            return new Prestacion(source);
        }

        public Prestacion[] newArray(int size) {
            return new Prestacion[size];
        }
    };
}
