package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public interface ProfilePresenterOps {

    void updateUserProfile(Usuario usuario);
    void getMessageIfConverExist(Anuncio anuncio, String keySolicitante);
    void messageIfConverExistObtained(MessagePojo m);
    void sendNewMessage(MessagePojo messagePojo, String keyReceptor);
}
