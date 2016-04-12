package c.proyecto.mvp_views_interfaces;


import c.proyecto.mvp_models.Usuario;

public interface RegistroActivityOps {
    void createUser(boolean exist);
    void userHasBeenRegistered(Usuario u);
}
