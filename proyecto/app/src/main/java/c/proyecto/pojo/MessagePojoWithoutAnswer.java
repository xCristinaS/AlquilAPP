package c.proyecto.pojo;

import android.os.Parcel;

import java.util.Date;

import c.proyecto.interfaces.IMessageAdapter;
import c.proyecto.models.Usuario;

/**
 * Created by Cristina on 01/04/2016.
 */
public class MessagePojoWithoutAnswer extends MessagePojo {

    public static final int WITHOUT_ANSWER = 19;

    public MessagePojoWithoutAnswer(){}

    public MessagePojoWithoutAnswer(Usuario emisor, String tituloAnuncio, String contenido, Date fehca){
        super(emisor, tituloAnuncio, contenido, fehca);
    }

    @Override
    public int getType(String keyCurrentUser) {
        return WITHOUT_ANSWER;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected MessagePojoWithoutAnswer(Parcel in) {
        super(in);
    }

    public static final Creator<MessagePojoWithoutAnswer> CREATOR = new Creator<MessagePojoWithoutAnswer>() {
        public MessagePojoWithoutAnswer createFromParcel(Parcel source) {
            return new MessagePojoWithoutAnswer(source);
        }

        public MessagePojoWithoutAnswer[] newArray(int size) {
            return new MessagePojoWithoutAnswer[size];
        }
    };
}
