package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas;

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

/**
 * Created by nicolas on 03/10/15.
 */
public class MascotaEncontradaDetalleDescripcionFragment extends Fragment {

    View view;
    private Pet encontradaPet;
    private SliderLayout photo_slider;
    private SliderLayout video_slider;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mascota_encontrada_detalle_descripcion, container, false);

        encontradaPet = PetServiceFactory.getInstance().getSelectedPet();

        ArrayList<String> urlPhotos = new ArrayList<>();
        for (ParseFile picture : encontradaPet.getPictures()) {
            urlPhotos.add(picture.getUrl());
        }
        ArrayList<String> urlVideos = encontradaPet.getVideos();
        loadInformacionBasica(encontradaPet);
        loadInformacionSocial(encontradaPet);
        loadInformacionMedica(encontradaPet);
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

        if (videos.isEmpty()) {
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

    private void loadInformacionBasica(Pet pet) {

        TextView textView = (TextView) view.findViewById(R.id.infSexoPet);
        textView.setText(pet.getGender());

        textView = (TextView) view.findViewById(R.id.infRazaPet);
        if (pet.getBreed() == null || pet.getBreed().isEmpty()) {
            textView.setText(R.string.raza_desconocida);
        } else {
            textView.setText(pet.getBreed());
        }

        textView = (TextView) view.findViewById(R.id.infEdadPet);
        textView.setText(pet.getAgeRange());

        textView = (TextView) view.findViewById(R.id.infDescPet);
        textView.setText(pet.getDescription());

    }

    private void loadInformacionSocial(Pet pet) {
        TextView textView = (TextView) view.findViewById(R.id.infRelacionNiños);
        textView.setText(pet.getChildren());

        textView = (TextView) view.findViewById(R.id.infRelacionAnimales);
        textView.setText(pet.getOtherPets());

        String socialNotes = pet.getSocialNotes();
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

    private void loadInformacionMedica(Pet pet) {
        TextView textView = (TextView) view.findViewById(R.id.infTomaMedicina);
        textView.setText(pet.getMedicine());

        String responseNO = getResources().getString(R.string.no);

        if (pet.getMedicine().equals(responseNO)) {
            textView = (TextView) view.findViewById(R.id.infPeriocidadLabel);
            textView.setVisibility(View.GONE);

            textView = (TextView) view.findViewById(R.id.infPeriocidad);
            textView.setVisibility(View.GONE);
        } else {
            textView = (TextView) view.findViewById(R.id.infPeriocidad);
            textView.setText(pet.getMedicineTime());
        }


        String medicineNotes = pet.getMedicineNotes();
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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFraGoogleMapgment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        ar.uba.fi.nicodiaz.mascota.model.Address adoptionPetAddress = encontradaPet.getAddress();

/*        TextView ubicacion = (TextView) view.findViewById(R.id.text_ubicacion);
        ubicacion.setText(adoptionPetAddress.getCalle());*/

        double latitude = adoptionPetAddress.getLocation().getLatitude();
        double longitude = adoptionPetAddress.getLocation().getLongitude();
        LatLng posicion = new LatLng(latitude, longitude);
        String msg = encontradaPet.getName() + " apareció acá";
        mMap.addMarker(new MarkerOptions().position(posicion).title(msg)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(posicion, 16)));
    }
}
