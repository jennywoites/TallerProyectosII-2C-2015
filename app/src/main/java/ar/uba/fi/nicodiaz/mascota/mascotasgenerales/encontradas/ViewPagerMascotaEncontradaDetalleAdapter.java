package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;

/**
 * Created by nicolas on 03/10/15.
 */
public class ViewPagerMascotaEncontradaDetalleAdapter extends ViewPagerAdapter {

    public ViewPagerMascotaEncontradaDetalleAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm,mTitles,mNumOfTabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MascotaEncontradaDetalleDescripcionFragment();
            case 1:
                return new MascotaEncontradaDetalleComentariosFragment();
            default:
                return null;
        }
    }
}
