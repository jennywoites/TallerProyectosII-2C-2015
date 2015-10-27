package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 16/10/2015.
 */
public enum FoundPetState {

    PUBLISHED("PUBLISHED"), CLAIMED("CLAIMED"), HIDDEN("OCULTA");;

    private String text;

    FoundPetState(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static FoundPetState getState(String text) {
        for (FoundPetState state : FoundPetState.values()) {
            if (state.text.equals(text)) {
                return state;
            }
        }

        return PUBLISHED;
    }
}
