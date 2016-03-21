package c.proyecto.pojo;


public class Prestacion {
    private int idDrawable;
    private String nombre;

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
}
