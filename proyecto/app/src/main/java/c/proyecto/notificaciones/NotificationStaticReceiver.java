package c.proyecto.notificaciones;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.NotificationCompat;

import c.proyecto.R;

public class NotificationStaticReceiver extends BroadcastReceiver {

    private static final int ABRIR_NAV = 999;
    private static final int NC_TAREA = 888;
    public static final String EXTRA_NOMBRE_EMISOR = "nombre emisor";
    public static final String EXTRA_CONTENIDO = "contenido";

    private NotificationManager mGestor;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        mGestor = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String nombreEmisor = intent.getStringExtra(EXTRA_NOMBRE_EMISOR);
        String contenido = intent.getStringExtra(EXTRA_CONTENIDO);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setSmallIcon(R.drawable.ic_chat); // Icono pequeño.
        b.setLargeIcon(((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_chat)).getBitmap()); // Icono grande.
        b.setContentTitle(nombreEmisor); // Título (1ª línea).
        b.setContentText(contenido); // Texto (2º línea).
        b.setContentInfo("3"); // Info adicional (nº total tareas pendientes).
        b.setTicker("Has recibido un mensaje");  // Ticker.
        b.setAutoCancel(true); // para que se elimine al pinchar sobre la notificacion.
        // Para meterle la acción.
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        b.setContentIntent(PendingIntent.getActivity(context, ABRIR_NAV, mIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        mGestor.notify(NC_TAREA, b.build());
        abortBroadcast();
    }
}
