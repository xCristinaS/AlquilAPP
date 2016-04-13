package c.proyecto.mvp_views_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;

/**
 * Created by Cristina on 23/03/2016.
 */
public interface AdvertsDetailsActivityOps {
    void onAdvertPublisherRequestedResponsed(Usuario u);
    void updateAdvert(Anuncio anuncio);
}
