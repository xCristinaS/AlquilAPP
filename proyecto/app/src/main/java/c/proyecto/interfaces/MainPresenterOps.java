package c.proyecto.interfaces;

import java.util.ArrayList;

import c.proyecto.models.Anuncio;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainPresenterOps {
    ArrayList<Anuncio> getAdverts();
    ArrayList<Anuncio> getAllUserSubs(String userEmail);
    ArrayList<Anuncio> getAllUserPublishAdverts(String userEmail);
}
