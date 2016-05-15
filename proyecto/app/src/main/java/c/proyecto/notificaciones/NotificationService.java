package c.proyecto.notificaciones;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.pojo.Message;
import c.proyecto.pojo.Usuario;

public class NotificationService extends Service {

    public static final String EXTRA_USUARIO = "EXTRA_USER";
    private static final int ABRIR_NAV = 999;
    private static final int NC_TAREA = 888;

    private Usuario currentUser;
    private NotificationManager mGestor;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentUser = intent.getParcelableExtra(EXTRA_USUARIO);
        new Thread(new NotificationListeners()).start();
        return START_NOT_STICKY; // se reinicia automáticamente.
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyNewMessageReceived(String nombreEmisor, String contenido){
        mGestor = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
        b.setSmallIcon(R.drawable.ic_chat); // Icono pequeño.
        b.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.ic_chat)).getBitmap()); // Icono grande.
        b.setContentTitle(nombreEmisor); // Título (1ª línea).
        b.setContentText(contenido); // Texto (2º línea).
        b.setContentInfo("3"); // Info adicional (nº total tareas pendientes).
        b.setTicker("Has recibido un mensaje");  // Ticker.
        b.setAutoCancel(true); // para que se elimine al pinchar sobre la notificacion.
        // Para meterle la acción.
        Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
        b.setContentIntent(PendingIntent.getActivity(this, ABRIR_NAV, mIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        mGestor.notify(NC_TAREA, b.build());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class NotificationListeners implements Runnable {

        private Firebase fNotification;
        private ChildEventListener listenerMensajes;

        public NotificationListeners() {
            initializeFirebaseListeners();
        }

        @Override
        public void run() {

        }

        private void initializeFirebaseListeners() {
            fNotification = new Firebase(Constantes.URL_NOTIFICACIONES);

            listenerMensajes = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    final String keyMessage = dataSnapshot.getKey();
                    String keyEmisor = keyMessage.split("_")[0];
                    new Firebase(Constantes.URL_USERS).child(keyEmisor).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario u = dataSnapshot.getValue(Usuario.class);
                            final String nombreEmisor = u.getNombre() + " " + u.getApellidos();

                            new Firebase(Constantes.URL_NOTIFICACIONES).child(currentUser.getKey()).child(Constantes.CHILD_MENSAJES).child(keyMessage).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String contenido = dataSnapshot.getValue(Message.class).getContenido();
                                    notifyNewMessageReceived(nombreEmisor, contenido);
                                    new Firebase(Constantes.URL_NOTIFICACIONES).child(currentUser.getKey()).child(Constantes.CHILD_MENSAJES).child(keyMessage).setValue(null);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };

            fNotification.child(currentUser.getKey()).child(Constantes.CHILD_MENSAJES).addChildEventListener(listenerMensajes);
        }

        public void detachListeners() {

        }
    }
}
