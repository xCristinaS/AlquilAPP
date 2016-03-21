package c.proyecto.interfaces;

import java.util.ArrayList;

import c.proyecto.models.Anuncio;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainActivityOps {
    void onAdvertsRequestedResponsed(ArrayList<Anuncio> anuncios);
    void onUserSubsRequestedResponsed(ArrayList<Anuncio> anuncios);
    void onUserPublishedAdvertsRequestedResponsed(ArrayList<Anuncio> anuncios);
}
