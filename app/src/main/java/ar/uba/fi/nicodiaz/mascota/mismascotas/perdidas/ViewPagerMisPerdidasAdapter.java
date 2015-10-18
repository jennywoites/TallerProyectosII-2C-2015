package ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;

import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MisAdopcionesPublicadasFragment;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MisAdopcionesSolicitadasFragment;

/**
 * Created by nicolas on 03/10/15.
 */
public class ViewPagerMisPerdidasAdapter extends ViewPagerAdapter {

    public ViewPagerMisPerdidasAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm,mTitles,mNumOfTabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MisPerdidasPublicadasFragment();
            default:
                return null;
        }
    }
}
