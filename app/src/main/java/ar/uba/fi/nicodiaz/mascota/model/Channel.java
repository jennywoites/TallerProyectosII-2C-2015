package ar.uba.fi.nicodiaz.mascota.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Juan Manuel Romera on 23/10/2015.
 */
public enum Channel {

    REQUEST("request"), COMMENTS("comments");

    private String field;

    Channel(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return field;
    }

    public static List<Channel> getChannels() {
        return Arrays.asList(Channel.values());
    }
}
