package c.proyecto.mvp_presenters_interfaces;

public interface InicioPresenterOps {
    void signInRequested(String email, String contra);
    void onSignInResponsed(Object o);
}
