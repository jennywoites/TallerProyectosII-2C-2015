package ar.uba.fi.nicodiaz.mascota.utils.service;

import ar.uba.fi.nicodiaz.mascota.model.service.impl.PetServiceMock;
import ar.uba.fi.nicodiaz.mascota.model.service.impl.PetServiceParse;
import ar.uba.fi.nicodiaz.mascota.model.service.api.PetService;

/**
 * Created by Juan Manuel Romera on 17/10/2015.
 */
public class PetServiceFactory {

    public static Boolean developmentMode = Boolean.FALSE;

    public static PetService getInstance() {
        if (developmentMode) {
            return PetServiceMock.getInstance();
        } else {
            return PetServiceParse.getInstance();
        }
    }

}
