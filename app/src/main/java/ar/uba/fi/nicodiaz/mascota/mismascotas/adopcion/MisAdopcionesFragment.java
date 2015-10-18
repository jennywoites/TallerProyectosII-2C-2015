package ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by nicolas on 09/10/15.
 */
public class MisAdopcionesFragment extends Fragment {
    private static final String TAG = "MisAdopcionesFragment";

    CharSequence Titles[] = {"Publicadas", "Solicitadas"};
    int NumbOfTabs = 2;

    private Context activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        ((AppCompatActivity) activity).getSupportActionBar().setSubtitle("Adopciones");

        // View:
        View view = inflater.inflate(R.layout.fragment_mis_adopciones, container, false);
        view.setTag(TAG);

        ViewPagerMisAdopcionesAdapter adapter = new ViewPagerMisAdopcionesAdapter(((AppCompatActivity) activity).getSupportFragmentManager(), Titles, NumbOfTabs);
        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        SlidingTabLayout tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorPrimary);
            }
        });
        tabs.setViewPager(pager);

        return view;
    }
}
