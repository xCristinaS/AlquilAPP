package c.proyecto.interfaces;

import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 23/03/2016.
 */
public interface AdvertsDetailsPresenterOps {

    void advertPublisherRequested(String anunciante);
    void onAdvertPublisherRequestedResponsed(Usuario u);

}
