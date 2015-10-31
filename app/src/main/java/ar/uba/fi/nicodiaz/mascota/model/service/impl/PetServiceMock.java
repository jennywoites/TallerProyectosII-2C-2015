package ar.uba.fi.nicodiaz.mascota.model.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.service.FoundPet;
import ar.uba.fi.nicodiaz.mascota.model.service.api.DatabasePetState;
import ar.uba.fi.nicodiaz.mascota.model.service.api.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.Filter;
import ar.uba.fi.nicodiaz.mascota.utils.service.MockPetFactory;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PetServiceMock extends PetService {

    private static PetServiceMock ourInstance = new PetServiceMock();
    private static Pet selectedPet;

    private static DatabasePetState databaseAdoptionPetState;
    private static DatabasePetState databaseMissingPetState;


    private List<AdoptionPet> adoptionPets;
    private List<AdoptionPet> dogAdoptionPets;
    private List<AdoptionPet> catAdoptionPets;

    private List<MissingPet> missingPets;
    private List<MissingPet> dogMissingPets;
    private List<MissingPet> catMissingPets;


    public static PetServiceMock getInstance() {
        return ourInstance;
    }

    public DatabasePetState getDatabaseAdoptionPetState() {
        return databaseAdoptionPetState;
    }

    public DatabasePetState getDatabaseMissingPetState() {
        return databaseMissingPetState;
    }

    public void loadAdoptionPets(DatabasePetState databaseAdoptionPetState) {
        this.databaseAdoptionPetState = databaseAdoptionPetState;
        switch (databaseAdoptionPetState) {
            case EMPTY:
                adoptionPets.clear();
                break;
            case DOGS:
                adoptionPets.clear();
                adoptionPets.addAll(dogAdoptionPets);
                break;
            case CATS:
                adoptionPets.clear();
                adoptionPets.addAll(catAdoptionPets);
                break;
            case ALL:
                adoptionPets.clear();
                adoptionPets.addAll(dogAdoptionPets);
                adoptionPets.addAll(catAdoptionPets);
                break;
        }
    }

    public void loadMissingPets(DatabasePetState databaseMissingPetState) {
        this.databaseMissingPetState = databaseMissingPetState;
        switch (databaseMissingPetState) {
            case EMPTY:
                missingPets.clear();
                break;
            case DOGS:
                missingPets.clear();
                missingPets.addAll(dogMissingPets);
                break;
            case CATS:
                missingPets.clear();
                missingPets.addAll(catMissingPets);
                break;
            case ALL:
                missingPets.clear();
                missingPets.addAll(dogMissingPets);
                missingPets.addAll(catMissingPets);
                break;
        }
    }

    private PetServiceMock() {
        this.adoptionPets = new ArrayList<>();
        this.missingPets = new ArrayList<>();
        createAdoptionDog();
        createAdoptionCats();
        createMissingDog();
        createMissingCats();
        loadAdoptionPets(DatabasePetState.ALL);
        loadMissingPets(DatabasePetState.ALL);
    }

    private void createAdoptionDog() {
        dogAdoptionPets = new ArrayList<>();

        //Menos de 1Km
        List<AdoptionPet> result = MockPetFactory.createMaleDogAdoption(5, "Richie McCaw", "Richie McCaw", "4-6 meses", new Address("", -34.579567, -58.434171, "", "Palermo"));
        dogAdoptionPets.addAll(result);

        //1-5 Km
        result = MockPetFactory.createMaleDogAdoption(5, "Dan Carter", "Dan Carter", "0-3 meses", new Address("", -34.583577, -58.446402, "", "Villa Crespo"));
        dogAdoptionPets.addAll(result);

        //5-10Km
        result = MockPetFactory.createMaleDogAdoption(5, "Julian Savea", "Julian Savea", "7-12 meses", new Address("", -34.614927, -58.484718, "", "Chacarita"));
        dogAdoptionPets.addAll(result);

        //10-15Km
        result = MockPetFactory.createFemaleDogAdoption(5, "Snny Bill Williams", "Snny Bill Williams", "1-3 años", new Address("", -34.671206, -58.484203, "", "Parque Chas"));
        dogAdoptionPets.addAll(result);

        //Mas 15
        result = MockPetFactory.createFemaleDogAdoption(5, "Ma'a Nonu", "Ma'a Nonu", "8-15 años", new Address("", -34.750441, -58.507206, "", "Mataderos"));
        dogAdoptionPets.addAll(result);

        result = MockPetFactory.createFemaleDogAdoption(5, "", "Israel Dagg", "4-7 años", new Address("", -34.580616, -58.4316696, "", "Palermo"));
        dogAdoptionPets.addAll(result);

    }

    private void createAdoptionCats() {
        catAdoptionPets = new ArrayList<>();

        //Menos de 1Km
        List<AdoptionPet> result = MockPetFactory.createMaleCatsAdoption(5, "Tigger", "Tigger", "4-6 meses", new Address("", -34.579567, -58.434171, "", "Palermo"));
        catAdoptionPets.addAll(result);

        //1-5 Km
        result = MockPetFactory.createFemaleCatsAdoption(5, "Bella", "Bella", "0-3 meses", new Address("", -34.583577, -58.446402, "", "Villa Crespo"));
        catAdoptionPets.addAll(result);

        //5-10Km
        result = MockPetFactory.createFemaleCatsAdoption(5, "Chloe", "Chloe", "7-12 meses", new Address("", -34.614927, -58.484718, "", "Chacarita"));
        catAdoptionPets.addAll(result);

        //10-15Km
        result = MockPetFactory.createMaleCatsAdoption(5, "Shadow", "Shadow", "1-3 años", new Address("", -34.671206, -58.484203, "", "Parque Chas"));
        catAdoptionPets.addAll(result);

        //Mas 15
        result = MockPetFactory.createMaleCatsAdoption(5, "Oliver", "Oliver", "8-15 años", new Address("", -34.750441, -58.507206, "", "Mataderos"));
        catAdoptionPets.addAll(result);

        result = MockPetFactory.createFemaleCatsAdoption(5, "", "Lucy", "4-7 años", new Address("", -34.580616, -58.4316696, "", "Palermo"));
        catAdoptionPets.addAll(result);
    }

    private void createMissingDog() {
        dogMissingPets = new ArrayList<>();

        //Menos de 1Km
        List<MissingPet> result = MockPetFactory.createMaleDogsMissing(2, "Pepe", "Pepe", "4-6 meses", new Address("", -34.579567, -58.434171, "", "Palermo"), new Date());
        dogMissingPets.addAll(result);

        //1-5 Km
        result = MockPetFactory.createMaleDogsMissing(2, "Ron", "Ron", "0-3 meses", new Address("", -34.583577, -58.446402, "", "Villa Crespo"), new Date());
        dogMissingPets.addAll(result);

        //5-10Km
        result = MockPetFactory.createMaleDogsMissing(2, "Ame", "Ame", "7-12 meses", new Address("", -34.614927, -58.484718, "", "Chacarita"), new Date());
        dogMissingPets.addAll(result);

        //10-15Km
        result = MockPetFactory.createFemaleDogsMissing(2, "Gris", "Gris", "1-3 años", new Address("", -34.671206, -58.484203, "", "Parque Chas"), new Date());
        dogMissingPets.addAll(result);

        //Mas 15
        result = MockPetFactory.createFemaleDogsMissing(2, "Lola", "Lola", "8-15 años", new Address("", -34.750441, -58.507206, "", "Mataderos"), new Date());
        dogMissingPets.addAll(result);

        result = MockPetFactory.createFemaleDogsMissing(2, "", "Guau", "4-7 años", new Address("", -34.580616, -58.4316696, "", "Palermo"), new Date());
        dogMissingPets.addAll(result);

    }

    private void createMissingCats() {
        catMissingPets = new ArrayList<>();

        //Menos de 1Km
        List<MissingPet> result = MockPetFactory.createMaleCatsMissing(2, "Ri", "Ri", "4-6 meses", new Address("", -34.579567, -58.434171, "", "Palermo"), new Date());
        catMissingPets.addAll(result);

        //1-5 Km
        result = MockPetFactory.createMaleCatsMissing(2, "Gregor", "Gregor", "0-3 meses", new Address("", -34.583577, -58.446402, "", "Villa Crespo"), new Date());
        catMissingPets.addAll(result);

        //5-10Km
        result = MockPetFactory.createMaleCatsMissing(2, "Harry", "Harry", "7-12 meses", new Address("", -34.614927, -58.484718, "", "Chacarita"), new Date());
        catMissingPets.addAll(result);

        //10-15Km
        result = MockPetFactory.createFemaleCatsMissing(2, "Luna", "Luna", "1-3 años", new Address("", -34.671206, -58.484203, "", "Parque Chas"), new Date());
        catMissingPets.addAll(result);

        //Mas 15
        result = MockPetFactory.createFemaleCatsMissing(2, "Her", "Her", "8-15 años", new Address("", -34.750441, -58.507206, "", "Mataderos"), new Date());
        catMissingPets.addAll(result);

        result = MockPetFactory.createFemaleCatsMissing(2, "", "Bea", "4-7 años", new Address("", -34.580616, -58.4316696, "", "Palermo"), new Date());
        catMissingPets.addAll(result);

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
    public void saveFoundPet(FoundPet foundPet) {

    }

    @Override
    public List<? extends Pet> getAdoptionPets(int page) {
        return getPets(page, adoptionPets);
    }

    @Override
    public List<? extends Pet> getAdoptionPets(int page, Map<String, List<String>> filters) {
        List<AdoptionPet> pets = getPets(page, adoptionPets, filters);
        return pets;
    }

    @Override
    public List<? extends Pet> getAdoptionPetsByUser(int page) {
        return getPetsByUser(page, adoptionPets);
    }

    @Override
    public List<? extends Pet> getAdoptionPetsByUser(int page, String selectedFilter) {
        return getPetsByUser(page, adoptionPets);
    }


    @Override
    public List<? extends Pet> getMissingPets(int page) {
        return getPets(page, missingPets);
    }

    @Override
    public List<? extends Pet> getMissingPetsByUser(int page) {
        return getPetsByUser(page, missingPets);
    }

    @Override
    public List<? extends Pet> getMissingPetsByUser(int page, String selectedFilter) {
        return getPetsByUser(page, missingPets);
    }

    @Override
    public List<MissingPet> getMissingPets(int page, Map<String, List<String>> filters) {
        return getPets(page, missingPets, filters);
    }

    @Override
    public List<? extends Pet> getFoundPets(int page) {
        return null;
    }

    @Override
    public List<? extends Pet> getFoundPetsByUser(int page) {
        return null;
    }

    @Override
    public List<? extends Pet> getFoundPetsByUser(int page, String selectedFilter) {
        return null;
    }

    @Override
    public List<FoundPet> getFoundPets(int page, Map<String, List<String>> filters) {
        return null;
    }


    private List<? extends Pet> getPets(int page, List<? extends Pet> pets) {

        User user = UserService.getInstance().getUser();
        List<Pet> filterResult = new ArrayList<>();
        for (Pet pet : pets) {

            String ownerID = pet.getOwner().getParseUser().getObjectId();
            if (ownerID != null && ownerID.equals(user.getParseUser().getObjectId())) {
                continue;
            }
            filterResult.add(pet);
        }

        int start = page * LIMIT;
        if (start > filterResult.size())
            return Collections.emptyList();
        int end = ((start + LIMIT) > filterResult.size()) ? filterResult.size() : start + LIMIT;
        return filterResult.subList(start, end);

    }


    private <T extends Pet> List<T> getPets(int page, List<T> pets, Map<String, List<String>> filters) {
        User user = UserService.getInstance().getUser();
        Address addressUser = user.getAddress();

        List<T> filterResult = new ArrayList<>();
        for (T pet : pets) {

            String ownerID = pet.getOwner().getParseUser().getObjectId();
            if (ownerID != null && ownerID.equals(user.getParseUser().getObjectId())) {
                continue;
            }

            Boolean isOK = Boolean.TRUE;
            for (String keyFilter : filters.keySet()) {
                List<String> values = filters.get(keyFilter);
                if (keyFilter.equals(Filter.ESPECIE)) {
                    if (!values.isEmpty() && !values.contains(pet.getKind())) {
                        isOK = Boolean.FALSE;
                        continue;
                    }
                } else if (keyFilter.equals(Filter.SEXO)) {
                    if (!values.isEmpty() && !values.contains(pet.getGender())) {
                        isOK = Boolean.FALSE;
                        continue;
                    }
                } else if (keyFilter.equals(Filter.EDAD)) {
                    if (!values.isEmpty() && !values.contains(pet.getAgeRange())) {
                        isOK = Boolean.FALSE;
                        continue;
                    }
                } else if (keyFilter.equals(Filter.DISTANCIA)) {

                    if (!values.isEmpty()) {

                        double distanceInKilometers = addressUser.getLocation().distanceInKilometersTo(pet.getAddress().getLocation());
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
                filterResult.add(pet);
            }
        }

        int start = page * LIMIT;
        if (start > filterResult.size())
            return Collections.emptyList();
        int end = ((start + LIMIT) > filterResult.size()) ? filterResult.size() : start + LIMIT;
        return filterResult.subList(start, end);

    }

    private List<? extends Pet> getPetsByUser(int page, List<? extends Pet> pets) {
        List<Pet> filterResult = new ArrayList<>();
        User user = UserService.getInstance().getUser();
        for (Pet pet : pets) {
            String ownerID = pet.getOwner().getParseUser().getObjectId();
            if (ownerID != null && ownerID.equals(user.getParseUser().getObjectId())) {
                filterResult.add(pet);
            }
        }
        int start = page * LIMIT;
        if (start > filterResult.size())
            return Collections.emptyList();
        int end = ((start + LIMIT) > filterResult.size()) ? filterResult.size() : start + LIMIT;
        return filterResult.subList(start, end);
    }

    @Override
    public Pet getSelectedPet() {
        return selectedPet;
    }

    @Override
    public void setSelectedPet(Pet pet) {
        this.selectedPet = pet;
    }

    @Override
    public Pet getAdoptionPet(String petId) {
        return null;
    }

    @Override
    public Pet getMissingPet(String petId) {
        return null;
    }

    @Override
    public Pet getFoundPet(String petId) {
        return null;
    }
}