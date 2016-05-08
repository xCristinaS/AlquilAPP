package c.proyecto.mvp_views_interfaces;

import com.firebase.geofire.GeoLocation;

import c.proyecto.pojo.Anuncio;

public interface MapBrowserActivityOps {
    void advertLocationObtained(Anuncio a, GeoLocation location);
    void advertClickedFromMapObtained(Anuncio a);
}
