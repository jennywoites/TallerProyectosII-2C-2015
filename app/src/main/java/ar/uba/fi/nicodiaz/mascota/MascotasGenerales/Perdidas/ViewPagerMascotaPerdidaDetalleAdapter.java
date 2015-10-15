package ar.uba.fi.nicodiaz.mascota.MascotasGenerales.Perdidas;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;

public class ViewPagerMascotaPerdidaDetalleAdapter extends ViewPagerAdapter {

    public ViewPagerMascotaPerdidaDetalleAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm,mTitles,mNumOfTabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MascotaPerdidaDetalleDescripcionFragment();
            case 1:
                return new MascotaPerdidaDetalleComentariosFragment();
            default:
                return null;
        }
    }
}
