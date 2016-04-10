package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.mvp_models.Usuario;
import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 23/03/2016.
 */
public interface AdvertsDetailsPresenterOps {

    void advertPublisherRequested(String anunciante);
    void onAdvertPublisherRequestedResponsed(Usuario u);
    void updateAdvert(Anuncio anuncio);
    void userNewSubRequested(Anuncio anuncio);
    void unSubRequested(Anuncio a);
    void sendNewMessage(MessagePojo m, String keyReceptor);
}
