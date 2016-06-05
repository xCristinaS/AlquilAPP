package c.proyecto.mvp_views_interfaces;

import android.view.View;
import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;

public interface MainActivityOps {
    void advertHasBeenObtained(Anuncio a);
    void adverHasBeenModified(Anuncio a);
    void subHasBeenObtained(Anuncio a);
    void subHasBeenModified(Anuncio a);
    void userAdvertHasBeenObtained(Anuncio a);
    void userAdvertHasBeenModified(Anuncio a);
    void removeSub(Anuncio a);
    void userMessageHasBeenObtained(MessagePojo m);
    void userAdvertHasBeenModified(Usuario user);
    void removeAdvert(Anuncio a);
    void sendAdvertHasBeenRemovedBroadcast(Anuncio anuncio);
    void filteredAdvertsObtained(ArrayList<Anuncio> filteredAdverts);
    void solicitantesObtained(View itemView, ArrayList<Usuario> listaSolicitantes, Anuncio anuncio);
    void allMessagesObtained();
    void sendAdvertHasBeenModifiedBroadcast(Anuncio a);
}
