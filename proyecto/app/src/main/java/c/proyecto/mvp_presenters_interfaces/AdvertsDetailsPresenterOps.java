package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;

public interface AdvertsDetailsPresenterOps {
    void advertPublisherRequested(String anunciante);
    void onAdvertPublisherRequestedResponsed(Usuario u);
    void updateAdvert(Anuncio anuncio);
    void userNewSubRequested(Anuncio anuncio);
    void unSubRequested(Anuncio a);
    void sendNewMessage(MessagePojo m, String keyReceptor);
    void getMessageIfConverExist(Anuncio anuncio);
    void messageIfConverExistObtained(MessagePojo m);
}
