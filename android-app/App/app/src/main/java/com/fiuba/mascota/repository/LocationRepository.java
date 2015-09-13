package com.fiuba.mascota.repository;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Juan Manuel Romera on 13/9/2015.
 */
public class LocationRepository {
    private static LocationRepository ourInstance = new LocationRepository();

    public static LocationRepository getInstance() {
        return ourInstance;
    }

    private LocationRepository() {
    }

    public List<String> getStatesByCountry(String country) {
        List<String> states = new ArrayList<>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("State");
            query.whereEqualTo("country", country);
            List<ParseObject> result = query.find();

            if (!result.isEmpty()) {
                for (ParseObject object : result) {
                    String stateName = (String) object.get("name");
                    states.add(stateName);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collections.sort(states);
        return states;
    }

    public List<String> getCityByState(String state) {
        List<String> cities = new ArrayList<>();
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
            query.whereEqualTo("state", state);
            List<ParseObject> result = query.find();

            if (!result.isEmpty()) {
                for (ParseObject object : result) {
                    String stateName = (String) object.get("name");
                    cities.add(stateName);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collections.sort(cities);
        return cities;
    }
}
