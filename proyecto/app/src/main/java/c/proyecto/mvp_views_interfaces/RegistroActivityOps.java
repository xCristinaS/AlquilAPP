package c.proyecto.mvp_views_interfaces;


import c.proyecto.pojo.Usuario;

public interface RegistroActivityOps {
    void createUser(boolean exist);
    void userHasBeenRegistered(Usuario u);
}
