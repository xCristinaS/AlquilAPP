package c.proyecto.interfaces;

/**
 * Created by Cristina on 19/03/2016.
 */
public interface InicioPresenterOps {
    public boolean createNewUser(String email, String contra);
    public void singInRequested(String email, String contra);
    public void onSingInSuccess();
}
