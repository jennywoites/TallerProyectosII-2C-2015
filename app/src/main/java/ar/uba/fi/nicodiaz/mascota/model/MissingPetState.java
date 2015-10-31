package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 16/10/2015.
 */
public enum MissingPetState {

    POSSIBLE("POSSIBLE"), PUBLISHED("PUBLICADA"), HIDDEN("OCULTA"), FOUND("ENCONTRADA");

    private String text;

    MissingPetState(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static MissingPetState getState(String text) {
        for (MissingPetState state : MissingPetState.values()) {
            if (state.text.equals(text)) {
                return state;
            }
        }

        return PUBLISHED;
    }
}
