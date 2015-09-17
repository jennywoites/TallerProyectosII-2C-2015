package ar.uba.fi.nicodiaz.mascota.model;

import android.content.Context;

/**
 * Created by nicolas on 13/09/15.
 */
public class Mascota {
    public String name;
    public String description;
    public String imageName;

    public int getImageResourceId(Context context) {
        try {
            return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
