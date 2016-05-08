package c.proyecto.mvp_presenters_interfaces;

import com.firebase.geofire.GeoLocation;

import c.proyecto.pojo.Anuncio;

public interface MapBrowserPresenterOps {
    void getLocations(GeoLocation centerPosition, double radius);
    void advertLocationObtained(Anuncio a, GeoLocation location);
    void detachGeolocationListener();
    void getAdvertClickedFromMap(String advertKey);
    void advertClickedFromMapObtained(Anuncio a);
}
