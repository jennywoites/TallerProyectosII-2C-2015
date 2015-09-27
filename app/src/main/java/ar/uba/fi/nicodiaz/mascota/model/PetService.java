package ar.uba.fi.nicodiaz.mascota.model;

import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.uba.fi.nicodiaz.mascota.MyApplication;
import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.utils.Filter;

/**
 * Created by Juan Manuel Romera on 23/9/2015.
 */
public class PetService {

    private static PetService ourInstance = new PetService();
    private static Integer LIMIT = 4;
    private static String CAT = "Gato";
    private static String DOG = "Perro";


    public static PetService getInstance() {
        return ourInstance;
    }

    private PetService() {
    }

    private static List<AdoptionPet> adoptionPets;

    public void saveAdoptionPet(AdoptionPet adoptionPet) {
        adoptionPet.saveInBackground();
    }

    public List<AdoptionPet> getAdoptionPets(int page) {
        User user = UserService.getInstance().getUser();
        List<AdoptionPet> adoptionPets = new ArrayList<>();
        ParseQuery<AdoptionPet> query = ParseQuery.getQuery(AdoptionPet.class);
        query.addDescendingOrder("createdAt");
        query.whereNotEqualTo(AdoptionPet.OWNER, user.getParseUser());
        query.setLimit(LIMIT);
        query.setSkip(page * LIMIT);

        try {
            adoptionPets = query.find();
        } catch (ParseException e) {
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return adoptionPets;
    }

    public List<AdoptionPet> getAdoptionPets(int page, Map<String, List<String>> filters) {
        Boolean hasDistanceFilter = Boolean.FALSE;
        User user = UserService.getInstance().getUser();
        List<AdoptionPet> adoptionPets = new ArrayList<>();
        List<ParseQuery<AdoptionPet>> querys = new ArrayList<ParseQuery<AdoptionPet>>();
        ParseQuery<AdoptionPet> finalQuery;
        try {

            if (filters.containsKey(Filter.DISTANCIA) && !filters.get(Filter.DISTANCIA).isEmpty()) {
                for (String distancia : filters.get(Filter.DISTANCIA)) {
                    ParseQuery<AdoptionPet> query = createQuery(user, filters);

                    if (distancia.equals(Filter.MAS_15)) {
                        ParseQuery<AdoptionPet> queryDistance = ParseQuery.getQuery(AdoptionPet.class);
                        queryDistance.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), 15);
                        query.whereDoesNotMatchKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryDistance);
                    } else if (distancia.equals(Filter.MENOS_1)) {
                        ParseQuery<AdoptionPet> queryDistance = ParseQuery.getQuery(AdoptionPet.class);
                        queryDistance.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), 1);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryDistance);
                    } else if (distancia.equals(Filter.D_ENTRE_1_5)) {
                        ParseQuery<AdoptionPet> queryResult = createQueryDistanceBetween(user, 5, 1);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryResult);
                    } else if (distancia.equals(Filter.D_ENTRE_5_10)) {
                        ParseQuery<AdoptionPet> queryResult = createQueryDistanceBetween(user, 10, 5);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryResult);
                    } else if (distancia.equals(Filter.D_ENTRE_10_15)) {
                        ParseQuery<AdoptionPet> queryResult = createQueryDistanceBetween(user, 15, 10);
                        query.whereMatchesKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryResult);
                    }


                    querys.add(query);
                }
                finalQuery = ParseQuery.or(querys);
            } else {
                finalQuery = createQuery(user, filters);
            }

            finalQuery.addDescendingOrder("createdAt");
            finalQuery.setLimit(LIMIT);
            finalQuery.setSkip(page * LIMIT);
            adoptionPets = finalQuery.find();
        } catch (ParseException e) {
            Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return adoptionPets;
    }

    private ParseQuery<AdoptionPet> createQuery(User user, Map<String, List<String>> filters) {
        ParseQuery<AdoptionPet> query = ParseQuery.getQuery(AdoptionPet.class);
        query.whereNotEqualTo(AdoptionPet.OWNER, user.getParseUser());
        Set<String> filterKeys = filters.keySet();
        for (String key : filterKeys) {
            if (!key.equals(Filter.DISTANCIA))
                addFilter(query, key, filters.get(key), user);
        }

        return query;
    }

    private ParseQuery<AdoptionPet> createQueryDistanceBetween(User user, int distanceMax, int distanceMin) {
        ParseQuery<AdoptionPet> queryMax = ParseQuery.getQuery(AdoptionPet.class);
        queryMax.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), distanceMax);

        ParseQuery<AdoptionPet> queryMin = ParseQuery.getQuery(AdoptionPet.class);
        queryMin.whereWithinKilometers(AdoptionPet.LOCATION, user.getAddress().getLocation(), distanceMin);
        ParseQuery<AdoptionPet> queryResult = queryMax.whereDoesNotMatchKeyInQuery(AdoptionPet.OBJECT_ID, AdoptionPet.OBJECT_ID, queryMin);
        return queryResult;
    }


    private void addFilter(ParseQuery<AdoptionPet> query, String keyFilter, List<String> values, User user) {
        if (keyFilter.equals(Filter.ESPECIE)) {
            query.whereContainedIn(AdoptionPet.KIND, values);
        } else if (keyFilter.equals(Filter.SEXO)) {
            query.whereContainedIn(AdoptionPet.GENDER, values);
        } else if (keyFilter.equals(Filter.EDAD)) {
            query.whereContainedIn(AdoptionPet.AGE, values);
        }
    }


/*    // Do not want locations further than maxDistance
    ParseQuery query = ParseQuery.getQuery("MyData");
    query.whereWithinKilometers("location",userGeoPoint,maxDistance);

    // Do not want locations closer than minDistance
    ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("MyData");
    innerQuery.whereWithinKilometers("location",userGeoPoint,minDistance);
    query.whereDoesNotMatchKeyInQuery("objectId","objectId",innerQuery);*/


    public int getIconPet(String petKind) {
        if (petKind.equals(DOG)) {
            return R.drawable.ic_dog;
        } else if (petKind.equals(CAT)) {
            return R.drawable.ic_cat;
        }
        return -1;
    }
}
