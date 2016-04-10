package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.mvp_models.Usuario;


public interface InicioPresenterOps {
    Usuario createNewUser(String email, String contra, String nombre, String apellidos);
    void singInRequested(String email, String contra);
    void onSingInResponsed(Usuario u);
}
