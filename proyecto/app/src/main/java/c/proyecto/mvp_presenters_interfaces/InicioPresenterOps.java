package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.Usuario;


public interface InicioPresenterOps {
    void signInRequested(String email, String contra);
    void signInWithTwitterRequested(String email, String contra);
    void onSignInResponsed(Usuario u);
    void signInWithFacebookRequested(String email, String contra);
}
