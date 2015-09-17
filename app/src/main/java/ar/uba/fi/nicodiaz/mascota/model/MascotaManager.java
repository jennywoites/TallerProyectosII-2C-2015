package ar.uba.fi.nicodiaz.mascota.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicolas on 13/09/15.
 */
public class MascotaManager {
    private static String[] mascotaArray = {"Bulbasaur", "Charmander", "Squirtle", "Pikachu", "Totodile", "Cyndaquil", "Chikorita"};
    private static String[] descriptionArray = {"This is Bulbasaur.", "This is Charmander.", "This is Squirtle.", "This is Pikachu.", "This is Totodile.", "This is Cyndaquil.", "This is Chikorita."};

    private static MascotaManager instance;
    private List<Mascota> mascotas;

    public static MascotaManager getInstance() {
        if (instance == null) {
            instance = new MascotaManager();
        }
        return instance;
    }

    public List<Mascota> getMascotas() {
        if (mascotas == null) {
            mascotas = new ArrayList<>();
            for (int i = 0; i < mascotaArray.length; i++) {
                Mascota mascota = new Mascota();
                mascota.name = mascotaArray[i];
                mascota.description = descriptionArray[i];
                mascota.imageName = mascotaArray[i].replaceAll("\\s+", "").toLowerCase();
                mascotas.add(mascota);
            }
        }
        return mascotas;
    }
}
