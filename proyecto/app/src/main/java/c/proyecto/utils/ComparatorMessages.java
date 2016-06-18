package c.proyecto.utils;

import java.util.Comparator;

import c.proyecto.pojo.MessagePojo;

public class ComparatorMessages implements Comparator<MessagePojo> {

    @Override
    public int compare(MessagePojo m1, MessagePojo m2) {
        return m2.getFecha().compareTo(m1.getFecha());
    }
}
