package c.proyecto.mvp_presenters;

import android.app.Activity;

import com.firebase.geofire.GeoLocation;

import java.lang.ref.WeakReference;

import c.proyecto.activities.MapBrowserActivity;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_models.AdvertsFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.MapBrowserPresenterOps;
import c.proyecto.pojo.Anuncio;

public class MapBrowserPresenter implements MyPresenter, MapBrowserPresenterOps {

    private static WeakReference<MapBrowserActivity> activity;
    private static MapBrowserPresenter presentador;
    private AdvertsFirebaseManager advertsManager;

    private MapBrowserPresenter (Activity a) {
        activity = new WeakReference<>((MapBrowserActivity) a);
    }

    public static MapBrowserPresenter getPresentador(Activity a) {
        if (presentador == null)
            presentador = new MapBrowserPresenter(a);
        else if (a != null)
            activity = new WeakReference<>((MapBrowserActivity) a);
        return presentador;
    }

    public void setAdvertsManager(AdvertsFirebaseManager advertsManager) {
        this.advertsManager = advertsManager;
    }

    @Override
    public void getLocations(GeoLocation centerPosition, double radius) {
        advertsManager.getLocations(centerPosition, radius);
    }

    @Override
    public void advertLocationObtained(Anuncio a, GeoLocation location) {
        if (activity.get() != null)
            activity.get().advertLocationObtained(a, location);
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

    @Override
    public void detachGeolocationListener() {
        advertsManager.detachGeolocationMapListener();
    }

    @Override
    public void liberarMemoria() {
        activity = null;
        presentador = null;
    }
}
