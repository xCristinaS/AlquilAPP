package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.EditProfileActivity;
import c.proyecto.activities.VerPerfilActivity;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.ProfilePresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public class ProfilePresenter implements ProfilePresenterOps, MyPresenter {

    private static WeakReference<Activity> activity;
    private static ProfilePresenter presentador;
    private UsersFirebaseManager usersManager;
    private MessagesFirebaseManager messagesManager;

    private ProfilePresenter(Activity a) {
        activity = new WeakReference<>(a);
    }

    public static ProfilePresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new ProfilePresenter(a);
        else if (a != null)
            activity = new WeakReference<>(a);
        return presentador;
    }

    public void setMessagesManager(MessagesFirebaseManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public void updateUserProfile(Usuario u) {
        usersManager.updateUserProfile(u);
    }

    @Override
    public void getMessageIfConverExist(Anuncio anuncio, String keySolicitante) {
        messagesManager.getMessageIfConverExist(anuncio, keySolicitante);
    }

    @Override
    public void messageIfConverExistObtained(MessagePojo m) {
        if (activity.get() != null && activity.get() instanceof VerPerfilActivity)
            ((VerPerfilActivity) activity.get()).messageIfConverExistObtained(m);
    }

    @Override
    public void sendNewMessage(MessagePojo messagePojo, String keyReceptor) {
        messagesManager.sendMessage(messagePojo, keyReceptor, true);
    }
}
