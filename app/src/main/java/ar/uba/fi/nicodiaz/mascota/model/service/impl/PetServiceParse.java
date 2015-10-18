package ar.uba.fi.nicodiaz.mascota.model.service.impl;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPetState;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.service.api.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.Filter;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PetServiceParse extends PetService {

    private static PetServiceParse ourInstance = new PetServiceParse();
    private static Pet selectedPet;


    public static PetServiceParse getInstance() {
        return ourInstance;
    }

    private PetServiceParse() {
    }

    @Override
    public void saveAdoptionPet(AdoptionPet adoptionPet) {
        adoptionPet.saveInBackground();
    }

    @Override
    public void saveMissingPet(MissingPet missingPet) {
        missingPet.saveInBackground();
    }

    @Override
    public List<? extends Pet> getAdoptionPets(int page) {
        List<AdoptionPet> pets = getPets(page, AdoptionPet.class);
        return pets;
    }

    @Override
    public List<? extends Pet> getAdoptionPets(int page, Map<String, List<String>> filters) {
        List<AdoptionPet> pets = getPets(page, filters, AdoptionPet.class);
        return pets;
    }

    @Override
    public List<? extends Pet> getAdoptionPetsByUser(int page) {
        List<AdoptionPet> pets = getPetsByUser(page, AdoptionPet.class);
        return pets;
    }

    @Override
    public List<? extends Pet> getMissingPets(int page) {
        List<MissingPet> pets = getPets(page, MissingPet.class);
        return pets;
    }

    @Override
    public List<MissingPet> getMissingPets(int page, Map<String, List<String>> filters) {
        List<MissingPet> pets = getPets(page, filters, MissingPet.class);
        return pets;
    }

    @Override
    public List<? extends Pet> getMissingPetsByUser(int page) {
        List<MissingPet> pets = getPetsByUser(page, MissingPet.class);
        return pets;
    }

    private <T extends ParseObject> List<T> getPetsByUser(int page, Class petClass) {
        User user = UserService.getInstance().getUser();
        List<T> pets = new ArrayList<>();
        ParseQuery<T> query = ParseQuery.getQuery(petClass);
        query.addDescendingOrder("createdAt");
        query.whereEqualTo(AdoptionPet.OWNER, user.getParseUser());
        query.whereNotEqualTo(AdoptionPet.STATE, AdoptionPetState.HIDDEN.toString());
        query.setLimit(LIMIT);
        query.setSkip(page * LIMIT);
        try {
            pets = query.find();
        } catch (ParseException e) {
            return null;
        }

        return pets;
    }

    private <T extends ParseObject> List<T> getPets(int page, Class petClass) {
        List<T> pets = new ArrayList<>();
        User user = UserService.getInstance().getUser();
        ParseQuery<T> query = ParseQuery.getQuery(petClass);

        if (petClass.equals(AdoptionPet.class)) {
            query.whereEqualTo(AdoptionPet.STATE, AdoptionPetState.NO_ADOPTED.toString());
        }

        query.whereNotEqualTo(AdoptionPet.OWNER, user.getParseUser());

        query.addDescendingOrder("createdAt");
        query.setLimit(LIMIT);
        query.setSkip(page * LIMIT);

        try {
            pets = query.find();
        } catch (ParseException e) {
            return null;
        }

        return pets;
    }

    private <T extends ParseObject> List<T> getPets(int page, Map<String, List<String>> filters, Class petClass) {
        User user = UserService.getInstance().getUser();
        List<T> pets = new ArrayList<>();
        List<ParseQuery<T>> querys = new ArrayList<ParseQuery<T>>();
        ParseQuery<T> finalQuery;
        try {

            if (filters.containsKey(Filter.DISTANCIA) && !filters.get(Filter.DISTANCIA).isEmpty()) {
                for (String distancia : filters.get(Filter.DISTANCIA)) {
                    ParseQuery<T> query = createQuery(user, filters, petClass);

                    if (distancia.equals(Filter.MAS_15)) {
                        ParseQuery<T> queryDistance = ParseQuery.getQuery(petClass);
                        queryDistance.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), 15);
                        query.whereDoesNotMatchKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryDistance);
                    } else if (distancia.equals(Filter.MENOS_1)) {
                        ParseQuery<T> queryDistance = ParseQuery.getQuery(petClass);
                        queryDistance.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), 1);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryDistance);
                    } else if (distancia.equals(Filter.D_ENTRE_1_5)) {
                        ParseQuery<T> queryResult = createQueryDistanceBetween(user, 5, 1, petClass);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryResult);
                    } else if (distancia.equals(Filter.D_ENTRE_5_10)) {
                        ParseQuery<T> queryResult = createQueryDistanceBetween(user, 10, 5, petClass);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryResult);
                    } else if (distancia.equals(Filter.D_ENTRE_10_15)) {
                        ParseQuery<T> queryResult = createQueryDistanceBetween(user, 15, 10, petClass);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryResult);
                    }
                    querys.add(query);
                }
                finalQuery = ParseQuery.or(querys);
            } else {
                finalQuery = createQuery(user, filters, petClass);
            }

            finalQuery.addDescendingOrder("createdAt");
            finalQuery.setLimit(LIMIT);
            finalQuery.setSkip(page * LIMIT);
            pets = finalQuery.find();
        } catch (ParseException e) {
            return null;
        }
        return pets;
    }

    private <T extends ParseObject> ParseQuery<T> createQuery(User user, Map<String, List<String>> filters, Class petClass) {
        ParseQuery<T> query = ParseQuery.getQuery(petClass);
        query.whereNotEqualTo(AdoptionPet.OWNER, user.getParseUser());
        //Filtro las no adoptadas
        if (petClass.equals(AdoptionPet.class)) {
            query.whereEqualTo(AdoptionPet.STATE, AdoptionPetState.NO_ADOPTED.toString());
        }

        Set<String> filterKeys = filters.keySet();
        for (String key : filterKeys) {
            if (!key.equals(Filter.DISTANCIA))
                addFilter(query, key, filters.get(key), user);
        }

        return query;
    }

    private <T extends ParseObject> ParseQuery<T> createQueryDistanceBetween(User user, int distanceMax, int distanceMin, Class petClass) {
        ParseQuery<T> queryMax = ParseQuery.getQuery(petClass);
        queryMax.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), distanceMax);

        ParseQuery<T> queryMin = ParseQuery.getQuery(petClass);
        queryMin.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), distanceMin);
        ParseQuery<T> queryResult = queryMax.whereDoesNotMatchKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryMin);
        return queryResult;
    }


    private <T extends ParseObject> void addFilter(ParseQuery<T> query, String keyFilter, List<String> values, User user) {
        if (keyFilter.equals(Filter.ESPECIE)) {
            query.whereContainedIn(AdoptionPet.KIND, values);
        } else if (keyFilter.equals(Filter.SEXO)) {
            query.whereContainedIn(AdoptionPet.GENDER, values);
        } else if (keyFilter.equals(Filter.EDAD)) {
            query.whereContainedIn(AdoptionPet.AGE, values);
        }
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