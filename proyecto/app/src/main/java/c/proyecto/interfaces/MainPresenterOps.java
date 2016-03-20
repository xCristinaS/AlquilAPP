package c.proyecto.interfaces;

import java.util.ArrayList;

import c.proyecto.models.Anuncio;
import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainPresenterOps {
    ArrayList<Anuncio> getAdverts();
    ArrayList<Anuncio> getAllUserSubs(Usuario u);
    ArrayList<Anuncio> getAllUserPublishAdverts(Usuario u);
}
