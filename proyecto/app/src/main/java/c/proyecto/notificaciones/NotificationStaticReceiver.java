package c.proyecto.notificaciones;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.NotificationCompat;

import c.proyecto.Constantes;
import c.proyecto.R;

public class NotificationStaticReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = context.getSharedPreferences(Constantes.NOMBRE_PREFERENCIAS, context.MODE_PRIVATE);
        pref.edit().putBoolean(Constantes.KEY_APP_INITIALIZED, true).apply();
        context.startService(new Intent(context, NotificationService.class));
    }
}
