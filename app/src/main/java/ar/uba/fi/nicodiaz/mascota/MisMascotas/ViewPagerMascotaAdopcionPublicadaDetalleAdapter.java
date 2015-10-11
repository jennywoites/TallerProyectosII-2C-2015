package ar.uba.fi.nicodiaz.mascota.MisMascotas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;

import ar.uba.fi.nicodiaz.mascota.MascotasGenerales.MascotaDetalleComentariosFragment;

/**
 * Created by nicolas on 03/10/15.
 */
public class ViewPagerMascotaAdopcionPublicadaDetalleAdapter extends ViewPagerAdapter {

    public ViewPagerMascotaAdopcionPublicadaDetalleAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm,mTitles,mNumOfTabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MascotaAdopcionPublicadaDetalleDescripcionFragment();
            case 1:
                return new MascotaDetalleComentariosFragment();
            case 2:
                return new MascotaAdopcionPublicadaDetalleSolicitudesFragment();
            default:
                return null;
        }
    }
}
