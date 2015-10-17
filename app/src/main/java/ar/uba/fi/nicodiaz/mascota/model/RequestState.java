package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 16/10/2015.
 */
public enum RequestState {

    PENDING("PENDIENTE"), ACCEPTED("ACEPTADA"), IGNORED("IGNORADA"), REJECTED("RECHAZADA");

    private String text;

    RequestState(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static RequestState getState(String text) {
        for (RequestState state : RequestState.values()) {
            if (state.text.equals(text)) {
                return state;
            }
        }

        return PENDING;
    }
}
