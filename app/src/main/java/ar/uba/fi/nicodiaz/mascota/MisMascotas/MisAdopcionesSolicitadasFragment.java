package ar.uba.fi.nicodiaz.mascota.MisMascotas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by nicolas on 14/09/15.
 */
public class MisAdopcionesSolicitadasFragment extends Fragment {
    private static final String TAG = "MisAdopcionesSolicitadasFragment";

    private Context activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();

        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View view = inflater.inflate(R.layout.fragment_vacio, container, false);
        view.setTag(TAG);

        return view;
    }
}
