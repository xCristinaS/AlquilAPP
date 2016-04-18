package c.proyecto.pojo;

import android.os.Parcel;

import java.util.Date;

public class MessagePojoWithoutAnswer extends MessagePojo {

    public static final int WITHOUT_ANSWER = 19;

    private Usuario receptor;

    public MessagePojoWithoutAnswer(){}

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
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
        dest.writeParcelable(this.receptor, 0);
    }

    protected MessagePojoWithoutAnswer(Parcel in) {
        super(in);
        this.receptor = in.readParcelable(Usuario.class.getClassLoader());
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
