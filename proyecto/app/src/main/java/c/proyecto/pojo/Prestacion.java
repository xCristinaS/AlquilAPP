package c.proyecto.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Prestacion implements Parcelable{

    private String nameDrawable;
    private String nombre;

    public Prestacion(){

    }
    public Prestacion(String nameDrawable, String nombre) {
        this.nameDrawable = nameDrawable;
        this.nombre = nombre;
    }

    public String getNameDrawable() {
        return nameDrawable;
    }

    public void setNameDrawable(String nameDrawable) {
        this.nameDrawable = nameDrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        boolean r = false;
        if (o instanceof  Prestacion && nombre.equals(((Prestacion) o).getNombre()) && nameDrawable.equals(((Prestacion) o).getNameDrawable()))
            r = true;
        return r;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameDrawable);
        dest.writeString(this.nombre);
    }

    protected Prestacion(Parcel in) {
        this.nameDrawable = in.readString();
        this.nombre = in.readString();
    }

    public static final Creator<Prestacion> CREATOR = new Creator<Prestacion>() {
        @Override
        public Prestacion createFromParcel(Parcel source) {
            return new Prestacion(source);
        }

        @Override
        public Prestacion[] newArray(int size) {
            return new Prestacion[size];
        }
    };
}
