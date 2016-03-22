package c.proyecto.interfaces;

import c.proyecto.models.Usuario;


public interface InicioPresenterOps {
    Usuario createNewUser(String email, String contra, String nombre, String apellidos);
    void singInRequested(String email, String contra);
    void onSingInResponsed(Usuario u);
}
