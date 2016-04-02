package c.proyecto.pojo;

import java.util.ArrayList;

import c.proyecto.interfaces.IMessageAdapter;

/**
 * Created by Cristina on 31/03/2016.
 */
public class MessageAdapterHeader implements IMessageAdapter {

    public static final int TIPO_CABECERA = 8;

    private String name;
    private ArrayList<MessagePojo> hiddenChildren;

    public MessageAdapterHeader(String name) {
        this.name = name;
    }

    @Override
    public int getType(String keyCurrentUser) {
        return TIPO_CABECERA;
    }

    public ArrayList<MessagePojo> getHiddenChildren() {
        return hiddenChildren;
    }

    public void setHiddenChildren(ArrayList<MessagePojo> hiddenChildren) {
        this.hiddenChildren = hiddenChildren;
    }

    public String getName() {
        return name;
    }
}
