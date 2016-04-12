package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.mvp_models.Usuario;


public interface InicioPresenterOps {
    void singInRequested(String email, String contra);
    void onSingInResponsed(Usuario u);
}
