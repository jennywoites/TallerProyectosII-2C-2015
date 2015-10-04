package ar.uba.fi.nicodiaz.mascota.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by nicolas on 03/10/15.
 */
public class MascotaDetalleComentariosFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mascota_detalle_comentarios, container, false);
    }
}
