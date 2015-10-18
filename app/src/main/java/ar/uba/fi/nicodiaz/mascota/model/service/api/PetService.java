package ar.uba.fi.nicodiaz.mascota.model.service.api;

import java.util.List;
import java.util.Map;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.Pet;

/**
 * Created by Juan Manuel Romera on 17/10/2015.
 */
public abstract class PetService {

    protected static Integer LIMIT = 4;
    protected static String CAT = "Gato";
    protected static String DOG = "Perro";

    public abstract void saveAdoptionPet(AdoptionPet adoptionPet);

    public abstract void saveMissingPet(MissingPet missingPet);

    public abstract List<? extends Pet> getAdoptionPets(int page);

    public abstract List<? extends Pet> getAdoptionPets(int page, Map<String, List<String>> filters);

    public abstract List<? extends Pet> getAdoptionPetsByUser(int page);

    public abstract List<? extends Pet> getMissingPets(int page);

    public abstract List<? extends Pet> getMissingPetsByUser(int page);

    public abstract List<MissingPet> getMissingPets(int page, Map<String, List<String>> filters);

    public int getIconPet(String petKind) {
        if (petKind.equals(DOG)) {
            return R.drawable.ic_dog;
        } else if (petKind.equals(CAT)) {
            return R.drawable.ic_cat;
        }
        return -1;
    }

    public abstract Pet getSelectedPet();

    public abstract void setSelectedPet(Pet pet);
}
