package c.proyecto.mvp_presenters;

import android.app.Activity;
import android.view.View;

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
        else if (a != null)
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
    public void sendAdvertHasBeenRemovedBroadcast(Anuncio anuncio) {
        if (activity.get() != null)
            activity.get().sendAdvertHasBeenRemovedBroadcast(anuncio);
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
    public void getSolicitantes(View itemView, Anuncio anuncio) {
        usersManager.getSolicitantes(itemView, anuncio);
    }

    @Override
    public void solicitantesObtained(View itemView, ArrayList<Usuario> listaSolicitantes, Anuncio anuncio) {
        if (activity.get() != null)
            activity.get().solicitantesObtained(itemView, listaSolicitantes, anuncio);
    }

    @Override
    public void getAdvertsByLocation(GeoLocation centerPosition, double radius) {
        advertsManager.getAdvertsByLocation(centerPosition, radius);
    }

    @Override
    public void updateAdvert(Anuncio anuncio) {
        advertsManager.publishNewAdvert(anuncio);
    }

    @Override
    public void detachGeoAdvertsLocationListener(){
        advertsManager.detachGeoAdvertsLocationListener();
    }

    @Override
    public void allMessagesObtained() {
        if (activity.get() != null)
            activity.get().allMessagesObtained();
    }

    public void sendAdvertHasBeenModifiedBroadcast(Anuncio a) {
        if (activity.get() != null)
            activity.get().sendAdvertHasBeenModifiedBroadcast(a);
    }

    @Override
    public void liberarMemoria() {
        presentador = null;
    }
}
