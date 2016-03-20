package c.proyecto.interfaces;

import java.util.ArrayList;

import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainPresenterOps {
    void getAdverts();
    void getAllUserSubs(Usuario u);
    void getAllUserPublishAdverts(Usuario u);
    void onAdvertsRequestedResponsed(ArrayList<Anuncio> anuncios);
}
