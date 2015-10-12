package ar.uba.fi.nicodiaz.mascota.MisMascotas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by nicolas on 09/10/15.
 */
public class MisMascotasFragment extends Fragment {
    private static final String TAG = "MisMascotasFragment";

    private Context activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        ((AppCompatActivity) activity).getSupportActionBar().setSubtitle(null);

        // View:
        View view = inflater.inflate(R.layout.fragment_mis_mascotas, container, false);
        view.setTag(TAG);

        ImageButton my_adopt_button = (ImageButton) view.findViewById(R.id.adoptions_button);
        my_adopt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, new MisAdopcionesFragment());
               // ft.addToBackStack(null);
                ft.commit();
            }
        });

        /*ImageButton my_missing_button = (ImageButton) view.findViewById(R.id.missing_button);
        my_adopt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, new MisAdopcionesPerdidasFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/

        return view;
    }
}
