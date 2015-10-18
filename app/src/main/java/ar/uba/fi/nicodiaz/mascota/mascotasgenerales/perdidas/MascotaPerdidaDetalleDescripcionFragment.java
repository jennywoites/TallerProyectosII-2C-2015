package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.perdidas;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.HashMap;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class MascotaPerdidaDetalleDescripcionFragment extends Fragment {
    View view;
    private Pet missingPet;
    private SliderLayout photo_slider;
    private SliderLayout video_slider;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mascota_perdida_detalle_descripcion, container, false);
        missingPet = PetServiceFactory.getInstance().getSelectedPet();
        ArrayList<String> urlPhotos = new ArrayList<>();

        for (ParseFile picture : missingPet.getPictures()) {
            urlPhotos.add(picture.getUrl());
        }

        ArrayList<String> urlVideos = missingPet.getVideos();
        loadInformacionBasica(missingPet);
        // Info Social y Médica no son necesarias en Mascotas Perdidas.
        // loadInformacionSocial(missingPet);
        // loadInformacionMedica(missingPet);
        loadPhotos(urlPhotos);
        loadVideos(urlVideos);
        setUpMapIfNeeded();

        return view;
    }

    @Override
    public void onStop() {
        photo_slider.stopAutoCycle();
        video_slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {
        photo_slider.startAutoCycle();
        video_slider.stopAutoCycle();
        setUpMapIfNeeded();
        super.onResume();
    }

    private void loadVideos(ArrayList<String> urlVideos) {
        video_slider = (SliderLayout) view.findViewById(R.id.video_slider);
        final HashMap<String, String> videos = new HashMap<>();
        int aux = 0;

        for (String urlVideo : urlVideos) {
            videos.put("Video " + aux, urlVideo);
            aux++;
        }

        if (videos.isEmpty()) { // TODO: o otro metodo, la query de base de datos no devolvio datos
            CardView cardview_video = (CardView) view.findViewById(R.id.cardview_video);
            cardview_video.setVisibility(View.GONE);
        } else {
            for (final String name : videos.keySet()) {
                TextSliderView slide = new TextSliderView(getActivity());
                slide.image("http://img.youtube.com/vi/" + videos.get(name) + "/mqdefault.jpg");
                slide.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                slide.description("Reproducir");
                slide.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView baseSliderView) {
                        Toast.makeText(baseSliderView.getContext(), "Click", Toast.LENGTH_SHORT).show();

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videos.get(name)));
                            intent.putExtra("force_fullscreen", true); // TODO: en versiones viejas de youtube puede no funcionar.
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videos.get(name)));
                            startActivity(intent);
                        }
                    }
                });

                video_slider.addSlider(slide);
            }

            video_slider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
            video_slider.setCustomAnimation(new DescriptionAnimation());
            video_slider.setCustomIndicator((PagerIndicator) view.findViewById(R.id.custom_indicator_video));
        }
    }

    private void loadPhotos(ArrayList<String> urlPhotos) {
        photo_slider = (SliderLayout) view.findViewById(R.id.photo_slider);
        final HashMap<String, String> photos = new HashMap<>();
        int aux = 0;

        for (String url : urlPhotos) {
            photos.put("Photo " + aux, url);
            aux++;
        }

        for (String name : photos.keySet()) {
            DefaultSliderView slide = new DefaultSliderView(getActivity());
            slide.image(photos.get(name));
            slide.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            photo_slider.addSlider(slide);
        }

        photo_slider.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        photo_slider.setCustomIndicator((PagerIndicator) view.findViewById(R.id.custom_indicator));
    }

    private void loadInformacionBasica(Pet adoptionPet) {

        TextView textView = (TextView) view.findViewById(R.id.infSexoPet);
        textView.setText(adoptionPet.getGender());
        textView = (TextView) view.findViewById(R.id.infRazaPet);

        if (adoptionPet.getBreed() == null || adoptionPet.getBreed().isEmpty()) {
            textView.setText(R.string.raza_desconocida);
        } else {
            textView.setText(adoptionPet.getBreed());
        }

        textView = (TextView) view.findViewById(R.id.infEdadPet);
        textView.setText(adoptionPet.getAgeRange());

        textView = (TextView) view.findViewById(R.id.infDescPet);
        textView.setText(adoptionPet.getDescription());

    }

    private void loadInformacionSocial(Pet adoptionPet) {
        TextView textView = (TextView) view.findViewById(R.id.infRelacionNiños);
        textView.setText(adoptionPet.getChildren());
        textView = (TextView) view.findViewById(R.id.infRelacionAnimales);
        textView.setText(adoptionPet.getOtherPets());
        String socialNotes = adoptionPet.getSocialNotes();

        if (!socialNotes.isEmpty()) {
            textView = (TextView) view.findViewById(R.id.infComentariosSocial);
            textView.setText(socialNotes);
        } else {
            textView = (TextView) view.findViewById(R.id.comentariosSocialLabel);
            textView.setVisibility(View.GONE);

            textView = (TextView) view.findViewById(R.id.infComentariosSocial);
            textView.setVisibility(View.GONE);
        }
    }

    private void loadInformacionMedica(Pet adoptionPet) {
        TextView textView = (TextView) view.findViewById(R.id.infTomaMedicina);
        textView.setText(adoptionPet.getMedicine());
        String responseNO = getResources().getString(R.string.no);

        if (adoptionPet.getMedicine().equals(responseNO)) {
            textView = (TextView) view.findViewById(R.id.infPeriocidadLabel);
            textView.setVisibility(View.GONE);

            textView = (TextView) view.findViewById(R.id.infPeriocidad);
            textView.setVisibility(View.GONE);
        } else {
            textView = (TextView) view.findViewById(R.id.infPeriocidad);
            textView.setText(adoptionPet.getMedicineTime());
        }

        String medicineNotes = adoptionPet.getMedicineNotes();

        if (!medicineNotes.isEmpty()) {
            textView = (TextView) view.findViewById(R.id.infComentariosMedicina);
            textView.setText(medicineNotes);
        } else {
            textView = (TextView) view.findViewById(R.id.comentariosMedicinaLabel);
            textView.setVisibility(View.GONE);

            textView = (TextView) view.findViewById(R.id.infComentariosMedicina);
            textView.setVisibility(View.GONE);
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        ar.uba.fi.nicodiaz.mascota.model.Address adoptionPetAddress = missingPet.getAddress();

        /*
        TextView ubicacion = (TextView) view.findViewById(R.id.text_ubicacion);
        ubicacion.setText(adoptionPetAddress.getCalle());
        */

        if (adoptionPetAddress != null) {
            double latitude = adoptionPetAddress.getLocation().getLatitude();
            double longitude = adoptionPetAddress.getLocation().getLongitude();
            LatLng posicion = new LatLng(latitude, longitude);
            String msg = missingPet.getName() + " se perdió en esta zona.";
            mMap.addMarker(new MarkerOptions().position(posicion).title(msg)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(posicion, 16)));
        }
    }
}
