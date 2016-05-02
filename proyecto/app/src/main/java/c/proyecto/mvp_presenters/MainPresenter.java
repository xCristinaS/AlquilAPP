package c.proyecto.mvp_presenters;

import android.app.Activity;

import com.firebase.geofire.GeoLocation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import c.proyecto.activities.MainActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_models.MessagesFirebaseManager;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.MainPresenterOps;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Prestacion;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;

public class MainPresenter implements MainPresenterOps, MyPresenter {

    private static WeakReference<MainActivity> activity;
    private static MainPresenter presentador;
    private AdvertsFirebaseManager advertsManager;
    private MessagesFirebaseManager messagesManager;
    private UsersFirebaseManager usersManager;

    private MainPresenter(Activity a) {
        activity = new WeakReference<>((MainActivity) a);
    }

    public static MainPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new MainPresenter(a);
        else
            activity = new WeakReference<>((MainActivity) a);
        return presentador;
    }

    @Override
    public void removeUserAdvert(Anuncio a) {
        advertsManager.removeUserAdvert(a);
    }

    @Override
    public void removeUserSub(Anuncio a) {
        advertsManager.removeUserSub(a);
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public void initializeFirebaseListeners(Usuario usuario) {
        advertsManager.initializeFirebaseListeners();
        usersManager.initializeOnUserChangedListener(usuario);
        messagesManager.initializeMessagesListeners();
    }

    @Override
    public void advertHasBeenObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().advertHasBeenObtained(a);
    }

    @Override
    public void adverHasBeenModified(Anuncio a) {
        if (activity.get() != null)
            activity.get().adverHasBeenModified(a);
    }

    @Override
    public void subHasBeenObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().subHasBeenObtained(a);
    }

    @Override
    public void subHasBeenModified(Anuncio a) {
        if (activity.get() != null)
            activity.get().subHasBeenModified(a);
    }

    @Override
    public void userAdvertHasBeenObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().userAdvertHasBeenObtained(a);
    }

    @Override
    public void userAdvertHasBeenModified(Anuncio a) {
        if (activity.get() != null)
            activity.get().userAdvertHasBeenModified(a);
    }

    @Override
    public void removeSub(Anuncio a) {
        if (activity.get() != null)
            activity.get().removeSub(a);
    }

    @Override
    public void removeAdvert(Anuncio a){
        if (activity.get() != null)
            activity.get().removeAdvert(a);
    }

    @Override
    public void detachListeners() {
        advertsManager.detachFirebaseListeners();
        usersManager.detachFirebaseListeners();
        messagesManager.detachMessagesListeners();
    }

    @Override
    public void requestUserMessages(Usuario user) {
        messagesManager.getUserMessages();
    }

    @Override
    public void userMessageHasBeenObtained(MessagePojo m) {
        if (activity.get() != null)
            activity.get().userMessageHasBeenObtained(m);
    }

    @Override
    public void userHasBeenModified(Usuario user) {
        if (activity.get() != null)
            activity.get().userAdvertHasBeenModified(user);
    }

    public void setAdvertsManager(AdvertsFirebaseManager advertsManager) {
        this.advertsManager = advertsManager;
    }

    public void setMessagesManager(MessagesFirebaseManager messagesManager) {
        this.messagesManager = messagesManager;
    }

    @Override
    public void sendAdvertHasBeenRemovedBroadcast() {
        if (activity.get() != null)
            activity.get().sendAdvertHasBeenRemovedBroadcast();
    }

    @Override
    public void filterRequest(String[] tipoVivienda, int minPrice, int maxPrice, int minSize, int maxSize, ArrayList<Prestacion> prestaciones, String provincia, String poblacion){
        advertsManager.filterRequest(tipoVivienda, minPrice, maxPrice, minSize, maxSize, prestaciones, provincia, poblacion);
    }

    @Override
    public void onFilterResponsed(ArrayList<Anuncio> filteredAdverts){
        if (activity.get() != null)
            activity.get().filteredAdvertsObtained(filteredAdverts);
    }

    @Override
    public void detachAdvertsListener() {
        advertsManager.detachFirebaseListeners();
    }

    @Override
    public void attachAdvertsListeners() {
        advertsManager.attachFirebaseListeners();
    }

    @Override
    public void getLocations(GeoLocation centerPosition, double radius) {
        advertsManager.getLocations(centerPosition, radius);
    }

    @Override
    public void detachGeoLocationListeners() {
        advertsManager.detachGeoLocationListener();
    }

    @Override
    public void locationObtained(Anuncio a, GeoLocation location) {
        if (activity.get() != null)
            activity.get().locationObtained(a, location);
    }

    @Override
    public void getAdvertClickedFromMap(String advertKey) {
        advertsManager.getAdvertClickedFromMap(advertKey);
    }

    @Override
    public void advertClickedFromMapObtained(Anuncio a) {
        if (activity.get() != null)
            activity.get().advertClickedFromMapObtained(a);
    }
}
