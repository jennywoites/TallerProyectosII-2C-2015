package ar.uba.fi.nicodiaz.mascota.mismascotas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.adopcion.AdopcionPublicarActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas.EncontradasPublicarActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.perdidas.PerdidasPublicarActivity;
import ar.uba.fi.nicodiaz.mascota.mismascotas.adopcion.MisAdopcionesFragment;
import ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas.MisPerdidasFragment;

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
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        ImageButton my_missing_button = (ImageButton) view.findViewById(R.id.missing_button);
        my_missing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame, new MisPerdidasFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        ImageButton add_adopt_button = (ImageButton) view.findViewById(R.id.add_adoption_button);
        add_adopt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, AdopcionPublicarActivity.class);
                startActivity(i);
            }
        });

        ImageButton add_missing_button = (ImageButton) view.findViewById(R.id.add_missing_button);
        add_missing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, PerdidasPublicarActivity.class);
                startActivity(i);
            }
        });

        ImageButton add_found_button = (ImageButton) view.findViewById(R.id.add_found_button);
        add_found_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, EncontradasPublicarActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}
