package ar.uba.fi.nicodiaz.mascota.model;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.model.exception.ApplicationConnectionException;

/**
 * Created by Juan Manuel Romera on 18/9/2015.
 */
public class AddressService {

    private static final String CLASS_NAME = "Direccion";
    private static final String USER_ID_KEY = "userID";
    private static final String CALLE_KEY = "calle";
    private static final String PISO_KEY = "piso";
    private static final String DEPARTAMENTO_KEY = "depto";
    private static final String LOCATION_KEY = "location";

    private static AddressService ourInstance = new AddressService();

    public static AddressService getInstance() {
        return ourInstance;
    }

    private AddressService() {
    }

    public Boolean hasSavedInformation(String userID) throws ApplicationConnectionException {
        return getAddress(userID) != null;
    }

    public void saveAddress(String userID, Address address) {
        ParseObject parseObject = new ParseObject(CLASS_NAME);
        parseObject.put(USER_ID_KEY, userID);
        parseObject.put(CALLE_KEY, address.getCalle());
        parseObject.put(PISO_KEY, address.getPiso());
        parseObject.put(DEPARTAMENTO_KEY, address.getDepartamento());
        parseObject.put(LOCATION_KEY, new ParseGeoPoint(address.getLatitud(), address.getLongitud()));
        parseObject.saveInBackground();
    }


    public Address getAddress(String userId) throws ApplicationConnectionException {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME);
        query.whereEqualTo(USER_ID_KEY, userId);

        List<ParseObject> result = null;
        try {
            result = query.find();
        } catch (ParseException e) {
            throw new ApplicationConnectionException();
        }

        if (!result.isEmpty()) {
            ParseObject parseObject = result.get(0);
            String calle = (String) parseObject.get(CALLE_KEY);
            String piso = (String) parseObject.get(PISO_KEY);
            String departamento = (String) parseObject.get(DEPARTAMENTO_KEY);
            ParseGeoPoint location = (ParseGeoPoint) parseObject.get(LOCATION_KEY);
            Address address = new Address(calle, piso, departamento, location.getLongitude(), location.getLongitude());
            return address;
        }

        return null;
    }
}
