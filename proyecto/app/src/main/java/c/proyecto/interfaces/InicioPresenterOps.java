package c.proyecto.interfaces;

import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 19/03/2016.
 */
public interface InicioPresenterOps {
    public Usuario createNewUser(String email, String contra, String nombre, String apellidos);
    public void singInRequested(String email, String contra);
    public void onSingInSuccess(Usuario u);
}
