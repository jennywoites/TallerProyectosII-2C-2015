package ar.uba.fi.nicodiaz.mascota.model.service.impl;

import com.parse.ParseObject;

import ar.uba.fi.nicodiaz.mascota.model.AdoptionComplaint;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.Complaint;
import ar.uba.fi.nicodiaz.mascota.model.FoundComplaint;
import ar.uba.fi.nicodiaz.mascota.model.FoundPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingComplaint;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.Pet;

/**
 * Created by Juan Manuel Romera on 11/11/2015.
 */
public class ComplaintService {

    private static ComplaintService ourInstance = new ComplaintService();

    public static ComplaintService getInstance() {
        return ourInstance;
    }

    private ComplaintService() {
    }

    public void saveComplaint(Complaint complaint) {
        Pet publication = complaint.getPublication();
        if (publication instanceof AdoptionPet) {
            ((AdoptionComplaint) complaint).saveInBackground();
        } else if (publication instanceof MissingPet) {
            ((MissingComplaint) complaint).saveInBackground();
        } else if (publication instanceof FoundPet) {
            ((FoundComplaint) complaint).saveInBackground();
        }

    }

}
