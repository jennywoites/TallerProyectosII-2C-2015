package ar.uba.fi.nicodiaz.mascota.mismascotas.encontradas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;

import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.adopcion.MascotaDetalleComentariosFragment;

/**
 * Created by nicolas on 03/10/15.
 */
public class ViewPagerMascotaEncontradaPublicadaDetalleAdapter extends ViewPagerAdapter {

    public ViewPagerMascotaEncontradaPublicadaDetalleAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm, mTitles, mNumOfTabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MascotaEncontradaPublicadaDetalleDescripcionFragment();
            case 1:
                return new MascotaDetalleComentariosFragment();
            case 2:
                return new MascotaEncontradaPublicadaDetalleSolicitudesFragment();
            default:
                return null;
        }
    }
}
