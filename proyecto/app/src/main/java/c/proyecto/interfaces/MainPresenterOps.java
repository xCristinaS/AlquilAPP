package c.proyecto.interfaces;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainPresenterOps {
    void getAdverts(Usuario u);
    void getAllUserSubs(Usuario u);
    void getAllUserPublishedAdverts(Usuario u);
    void onAdvertsRequestedResponsed(ArrayList<Anuncio> anuncios);
    void onUserSubsRequestedResponsed(ArrayList<Anuncio> anuncios);
    void onUserPublishedAdvertsRequestedResponsed(ArrayList<Anuncio> anuncios);
    void removeUserAdvert(Anuncio a);
    void removeUserSub(Anuncio a, Usuario u);
}
