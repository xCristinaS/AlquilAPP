package c.proyecto.mvp_views_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public interface AdvertsDetailsActivityOps {
    void onAdvertPublisherRequestedResponsed(Usuario u);
    void updateAdvert(Anuncio anuncio);
    void messageIfConverExistObtained(MessagePojo m);
}
