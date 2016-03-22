package c.proyecto.interfaces;

import c.proyecto.models.Usuario;

/**
 * Created by aleja on 22/03/2016.
 */
public interface RegistroPresenterOps {
    Usuario register(String user, String pass, String nombre, String apellidos);
    boolean checkUser(String user);
}
