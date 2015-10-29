package ar.uba.fi.nicodiaz.mascota.model;

/**
 * Created by Juan Manuel Romera on 16/10/2015.
 */
public enum AdoptionPetState {

    RETAINED("RETENIDA"), PUBLISHED("PUBLICADA"), HIDDEN("OCULTA"), ADOPTED("ADOPTADA");

    private String text;

    AdoptionPetState(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static AdoptionPetState getState(String text) {
        for (AdoptionPetState state : AdoptionPetState.values()) {
            if (state.text.equals(text)) {
                return state;
            }
        }

        return PUBLISHED;
    }
}
