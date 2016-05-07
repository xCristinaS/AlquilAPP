package c.proyecto.mvp_presenters_interfaces;

public interface InicioPresenterOps {
    void signInRequested(String email, String contra);
    void signInWithTwitterRequested(String email, String contra);
    void onSignInResponsed(Object o);
    void signInWithFacebookRequested(String email, String contra);
}
