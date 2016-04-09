package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.mvp_models.Usuario;

public interface RegistroPresenterOps {
    Usuario register(String user, String pass, String nombre, String apellidos);
    void checkUser(String user);
    void onCheckUserExist(boolean exist);
}
