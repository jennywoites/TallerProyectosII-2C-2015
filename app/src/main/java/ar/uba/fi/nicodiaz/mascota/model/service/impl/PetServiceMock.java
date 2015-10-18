package ar.uba.fi.nicodiaz.mascota.model.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.service.api.DatabaseAdoptionPetState;
import ar.uba.fi.nicodiaz.mascota.model.service.api.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.Filter;
import ar.uba.fi.nicodiaz.mascota.utils.service.MockDogFactory;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PetServiceMock extends PetService {

    private static PetServiceMock ourInstance = new PetServiceMock();
    private static Pet selectedPet;
    private static DatabaseAdoptionPetState databaseAdoptionPetState;


    private List<AdoptionPet> adoptionPets;
    private List<AdoptionPet> dogAdoptionPets;
    private List<AdoptionPet> catAdoptionPets;


    public static PetServiceMock getInstance() {
        return ourInstance;
    }

    public DatabaseAdoptionPetState getDatabaseAdoptionPetState() {
        return databaseAdoptionPetState;
    }

    public void loadAdoptionPets(DatabaseAdoptionPetState databaseAdoptionPetState) {
        this.databaseAdoptionPetState = databaseAdoptionPetState;
        switch (databaseAdoptionPetState) {
            case EMPTY:
                adoptionPets = Collections.emptyList();
                break;
            case DOGS:
                adoptionPets.clear();
                adoptionPets = dogAdoptionPets;
                break;
            case CATS:
                adoptionPets.clear();
                adoptionPets = catAdoptionPets;
                break;
            case ALL:
                adoptionPets.clear();
                adoptionPets.addAll(dogAdoptionPets);
                adoptionPets.addAll(catAdoptionPets);
                break;
        }
    }

    private PetServiceMock() {
        this.adoptionPets = new ArrayList<>();
        createAdoptionDogPets();
        createAdoptionDogCats();
        loadAdoptionPets(DatabaseAdoptionPetState.ALL);
    }

    private void createAdoptionDogPets() {
        dogAdoptionPets = new ArrayList<>();

        //Menos de 1Km
        List<AdoptionPet> result = MockDogFactory.createMaleDogPets(5, "Richie McCaw", "Richie McCaw", "4-6 meses", new Address("", -34.579567, -58.434171, "", "Palermo"));
        dogAdoptionPets.addAll(result);

        //1-5 Km
        result = MockDogFactory.createMaleDogPets(5, "Dan Carter", "Dan Carter", "0-3 meses", new Address("", -34.583577, -58.446402, "", "Villa Crespo"));
        dogAdoptionPets.addAll(result);

        //5-10Km
        result = MockDogFactory.createMaleDogPets(5, "Julian Savea", "Julian Savea", "7-12 meses", new Address("", -34.614927, -58.484718, "", "Chacarita"));
        dogAdoptionPets.addAll(result);

        //10-15Km
        result = MockDogFactory.createMaleDogPets(5, "Snny Bill Williams", "Snny Bill Williams", "1-3 años", new Address("", -34.671206, -58.484203, "", "Parque Chas"));
        dogAdoptionPets.addAll(result);

        //Mas 15
        result = MockDogFactory.createMaleDogPets(5, "Ma'a Nonu", "Ma'a Nonu", "8-15 años", new Address("", -34.750441, -58.507206, "", "Mataderos"));
        dogAdoptionPets.addAll(result);

        result = MockDogFactory.createMaleDogPets(5, "", "Israel Dagg", "4-7 años", new Address("", -34.580616, -58.4316696, "", "Palermo"));
        dogAdoptionPets.addAll(result);

    }

    private void createAdoptionDogCats() {
        catAdoptionPets = new ArrayList<>();
    }


    @Override
    public void saveAdoptionPet(AdoptionPet adoptionPet) {
        adoptionPets.add(adoptionPet);
    }

    @Override
    public void saveMissingPet(MissingPet missingPet) {
        missingPet.saveInBackground();
    }

    @Override
    public List<? extends Pet> getAdoptionPets(int page) {
        User user = UserService.getInstance().getUser();
        List<AdoptionPet> filterResult = new ArrayList<>();
        for (AdoptionPet adoptionPet : adoptionPets) {

            String ownerID = adoptionPet.getOwner().getParseUser().getObjectId();
            if (ownerID != null && ownerID.equals(user.getParseUser().getObjectId())) {
                continue;
            }
            filterResult.add(adoptionPet);
        }

        int start = page * LIMIT;
        if (start > filterResult.size())
            return Collections.emptyList();
        int end = ((start + LIMIT) > filterResult.size()) ? filterResult.size() : start + LIMIT;
        return filterResult.subList(start, end);
    }

    @Override
    public List<? extends Pet> getAdoptionPets(int page, Map<String, List<String>> filters) {
        User user = UserService.getInstance().getUser();
        Address addressUser = user.getAddress();

        List<AdoptionPet> filterResult = new ArrayList<>();
        for (AdoptionPet adoptionPet : adoptionPets) {

            String ownerID = adoptionPet.getOwner().getParseUser().getObjectId();
            if (ownerID != null && ownerID.equals(user.getParseUser().getObjectId())) {
                continue;
            }

            Boolean isOK = Boolean.TRUE;
            for (String keyFilter : filters.keySet()) {
                List<String> values = filters.get(keyFilter);
                if (keyFilter.equals(Filter.ESPECIE)) {
                    if (!values.isEmpty() && !values.contains(adoptionPet.getKind())) {
                        isOK = Boolean.FALSE;
                        continue;
                    }
                } else if (keyFilter.equals(Filter.SEXO)) {
                    if (!values.isEmpty() && !values.contains(adoptionPet.getGender())) {
                        isOK = Boolean.FALSE;
                        continue;
                    }
                } else if (keyFilter.equals(Filter.EDAD)) {
                    if (!values.isEmpty() && !values.contains(adoptionPet.getAgeRange())) {
                        isOK = Boolean.FALSE;
                        continue;
                    }
                } else if (keyFilter.equals(Filter.DISTANCIA)) {

                    if (!values.isEmpty()) {

                        double distanceInKilometers = addressUser.getLocation().distanceInKilometersTo(adoptionPet.getAddress().getLocation());
                        if (distanceInKilometers < 1 && !values.contains(Filter.MENOS_1)) {
                            isOK = Boolean.FALSE;
                            continue;
                        } else if ((distanceInKilometers >= 1 && distanceInKilometers < 5) && !values.contains(Filter.D_ENTRE_1_5)) {
                            isOK = Boolean.FALSE;
                            continue;
                        } else if ((distanceInKilometers >= 5 && distanceInKilometers < 10) && !values.contains(Filter.D_ENTRE_5_10)) {
                            isOK = Boolean.FALSE;
                            continue;
                        } else if ((distanceInKilometers >= 10 && distanceInKilometers < 15) && !values.contains(Filter.D_ENTRE_10_15)) {
                            isOK = Boolean.FALSE;
                            continue;
                        } else if ((distanceInKilometers >= 15) && !values.contains(Filter.MAS_15)) {
                            isOK = Boolean.FALSE;
                            continue;
                        }
                    }


                }
            }

            if (isOK) {
                filterResult.add(adoptionPet);
            }
        }

        int start = page * LIMIT;
        if (start > filterResult.size())
            return Collections.emptyList();
        int end = ((start + LIMIT) > filterResult.size()) ? filterResult.size() : start + LIMIT;
        return filterResult.subList(start, end);

    }

    @Override
    public List<? extends Pet> getAdoptionPetsByUser(int page) {
        List<AdoptionPet> filterResult = new ArrayList<>();
        User user = UserService.getInstance().getUser();
        for (AdoptionPet adoptionPet : adoptionPets) {
            String ownerID = adoptionPet.getOwner().getParseUser().getObjectId();
            if (ownerID != null && ownerID.equals(user.getParseUser().getObjectId())) {
                filterResult.add(adoptionPet);
            }
        }
        int start = page * LIMIT;
        if (start > filterResult.size())
            return Collections.emptyList();
        int end = ((start + LIMIT) > filterResult.size()) ? filterResult.size() : start + LIMIT;
        return filterResult.subList(start, end);
    }

    @Override
    public List<? extends Pet> getMissingPets(int page) {
        return Collections.emptyList();
    }

    @Override
    public List<MissingPet> getMissingPets(int page, Map<String, List<String>> filters) {
        return Collections.emptyList();
    }


    @Override
    public Pet getSelectedPet() {
        return selectedPet;
    }

    @Override
    public void setSelectedPet(Pet pet) {
        this.selectedPet = pet;
    }
}