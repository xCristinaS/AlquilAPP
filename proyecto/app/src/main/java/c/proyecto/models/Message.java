package c.proyecto.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Date;

import c.proyecto.pojo.MessagePojo;
import c.proyecto.presenters.MainPresenter;

/**
 * Created by Cristina on 23/03/2016.
 */
public class Message implements Parcelable{

    private static final String URL_CONVERSACIONES = "https://proyectofinaldam.firebaseio.com/conversaciones/";
    private static final String URL_USERS = "https://proyectofinaldam.firebaseio.com/usuarios/";

    private long fecha;
    private String contenido;

    private static Firebase mFirebase;
    private static ValueEventListener mListener;


    public Message(){

    }

    public Message(Date fecha, String contenido){
        this.fecha = fecha.getTime();
        this.contenido = contenido;
    }

    public static void getUserMessages(final Usuario user, final MainPresenter presenter){
        mFirebase = new Firebase(URL_CONVERSACIONES).child(user.getKey());
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final MessagePojo mensaje = new MessagePojo();
                    final String emisor_titleAdvert = data.getKey();
                    String[] campos = emisor_titleAdvert.split("_");
                    String emisorKey = campos[0];
                    String titulo = "";
                    for (int i = 1; i < campos.length; i++)
                        titulo += campos[i] + " ";

                    mensaje.setTituloAnuncio(titulo);
                    new Firebase(URL_USERS).child(emisorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Usuario emisor = dataSnapshot.getValue(Usuario.class);
                            mensaje.setFotoEmisor(emisor.getFoto());
                            mensaje.setNombreEmisor(emisor.getNombre());

                            new Firebase(URL_CONVERSACIONES).child(user.getKey()).child(emisor_titleAdvert).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        Message m = data.getValue(Message.class);
                                        mensaje.setContenido(m.getContenido());
                                        mensaje.setFecha(new Date(m.getFecha()));
                                    }
                                    presenter.userMessageHasBeenObtained(mensaje);
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
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        mFirebase.addValueEventListener(mListener);
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.fecha);
        dest.writeString(this.contenido);
    }

    protected Message(Parcel in) {
        this.fecha = in.readLong();
        this.contenido = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
