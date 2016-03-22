package c.proyecto.interfaces;

import c.proyecto.models.Usuario;

public interface RegistroPresenterOps {
    Usuario register(String user, String pass, String nombre, String apellidos);
    void checkUser(String user);
    void onCheckUserExist(boolean exist);
}
