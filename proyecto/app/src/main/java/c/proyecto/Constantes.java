package c.proyecto;


import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public final class Constantes {
    public static final String MONEDA = "€";
    public static final String UNIDAD = "m²";
    public static final String MENSUAL = "mes";
    public static final String FOTO_PRINCIPAL = "principal";

    //TIPO VIVIENDA
    public static final String CASA = "Casa";
    public static final String PISO = "Piso";
    public static final String HABITACION = "Habitación";
    public static final String CAMAS = "Camas";

    //Dialogo Prestaciones Detalladas
    public static final float PORCENTAJE_PANTALLA = 0.65f;

    //PREFERENCIAS
        //Internas
    public static final String NOMBRE_PREFERENCIAS = "Preferencias";
        //Keys
        public static final String KEY_USER = "keyUser";
        public static final String KEY_PASS = "keyPass";
        public static final String KEY_LOCATION_ACTIVED = "keyLocationActived";
        //Shared
        public static final int DEFAULT_RATIO_BUSQUEDA = 10;

    //Número de imagenes que contendrá los anuncios
    public static final int NUMERO_IMAGENES_ANUNCIO = 6;

    //Número por el que se divide/multiplica los SeekBar de las características.
    public static final int MULTIPLICADOR_SEEK_BAR = 10;

    //MAPS
    public static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds( new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
        //Zoom LocalizacionActivity cuando se abre el mapa con un anuncio que ya contiene una localización.
    public static final float ZOOM_ANUNCIO_CON_LOCALIZACION = 16f;
        //DetallesAnuncioFragment
    public static final int CIRCLE_COLOR = Color.argb(100, 255, 64, 129);
    public static final float CIRCLE_STROKE_WIDTH = 2;
    public static final double CIRCLE_RADIUS = 35;

    //IMAGE SLIDER
    public static final long DELAY_TIME = 8000;
    public static final long DURATION = 8000;

    public static final int MAX_LENGHT_TITULO_ANUNCIO = 60;

    // FIREBASE
    public static final String URL_MAIN_FIREBASE = "https://proyectofinaldam.firebaseio.com/";
    public static final String URL_USERS = URL_MAIN_FIREBASE + "usuarios/";


    //ESCALADO
    public static final int FACTOR_ESCALADO = 280;
}

