package c.proyecto.utils;

import java.util.Comparator;

import c.proyecto.pojo.MessagePojo;

/**
 * Created by Cristina on 02/04/2016.
 */
public class ComparatorConver implements Comparator<MessagePojo> {
    @Override
    public int compare(MessagePojo m1, MessagePojo m2) {
        return m1.getFecha().compareTo(m2.getFecha());
    }
}
