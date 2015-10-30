package ar.uba.fi.nicodiaz.mascota.mismascotas.perdidas;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.util.List;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.NewCommentActivity;
import ar.uba.fi.nicodiaz.mascota.model.MissingPet;
import ar.uba.fi.nicodiaz.mascota.model.MissingPetState;
import ar.uba.fi.nicodiaz.mascota.model.MissingRequest;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PushService;
import ar.uba.fi.nicodiaz.mascota.model.RequestMissingService;
import ar.uba.fi.nicodiaz.mascota.model.RequestState;
import ar.uba.fi.nicodiaz.mascota.model.service.api.PetService;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;


public class MascotaPerdidaPublicadaDetalleActivity extends AppCompatActivity {

    CharSequence Titles[];
    int NumbOfTabs = 3;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int fragementView = getIntent().getIntExtra("fragementView", 0);
        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_mascota_perdida_publicada_detalle);

                Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
                setSupportActionBar(toolbar);
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                pet = PetServiceFactory.getInstance().getSelectedPet();

                final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                collapsingToolbar.setTitle(pet.getName());

                final FloatingActionButton FAB_edit = (FloatingActionButton) findViewById(R.id.FAB_editar);
                FAB_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: editar mascota... podría llamarse de alguna forma a algo parecido a el de crear mascota, pero en lugar de agregar se updatee en la base de datos.
                        Toast.makeText(MascotaPerdidaPublicadaDetalleActivity.this, "Proximamente podrás editar tu mascota", Toast.LENGTH_SHORT).show();
                    }
                });

                final FloatingActionButton FAB_comment = (FloatingActionButton) findViewById(R.id.FAB_comentar);
                FAB_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MascotaPerdidaPublicadaDetalleActivity.this, NewCommentActivity.class);
                        startActivity(i);
                    }
                });

                loadHeader(pet);

                int commentsCount = CommentService.getInstance().getCount(pet.getID());
                Titles = new CharSequence[]{"Información", "Comentarios (" + String.valueOf(commentsCount) + ")", "Solicitudes"};

                ViewPagerAdapter adapter = new ViewPagerMascotaPerdidaPublicadaDetalleAdapter(getSupportFragmentManager(), Titles, NumbOfTabs);
                ViewPager pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(adapter);
                pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
                        appBarLayout.setExpanded(false, true);

                        switch (position) {
                            case 0:
                                FAB_edit.setVisibility(View.VISIBLE);
                                FAB_comment.setVisibility(View.GONE);
                                break;
                            case 1:
                                FAB_edit.setVisibility(View.GONE);
                                FAB_comment.setVisibility(View.VISIBLE);
                                break;
                            default:
                                FAB_edit.setVisibility(View.GONE);
                                FAB_comment.setVisibility(View.GONE);
                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);
                tabs.setDistributeEvenly(true);
                tabs.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
                tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                    @Override
                    public int getIndicatorColor(int position) {
                        return getResources().getColor(R.color.ColorPrimary);
                    }
                });
                tabs.setViewPager(pager);
                pager.setCurrentItem(fragementView);

            }
        };
        WaitForInternet.setCallback(callback);
    }

    private void loadHeader(Pet pet) {
        final ImageView headerImage = (ImageView) findViewById(R.id.header);

        final ParseImageView imageView = new ParseImageView(this);
        ParseFile photoFile = pet.getPicture();
        if (photoFile != null) {
            imageView.setParseFile(photoFile);
            imageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {

                    try {
                        headerImage.setImageDrawable(imageView.getDrawable());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mascota_perdida_publicada_detalle, menu);
        if (((MissingPet) pet).getState().equals(MissingPetState.PUBLISHED)) {
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_republish).setVisible(false);
        } else if (((MissingPet) pet).getState().equals(MissingPetState.HIDDEN)) {
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_republish).setVisible(true);
        } else { // Caso Reservada o Adoptada, no se puede despublicar o republicar.
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_republish).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_close:
                volverAtras();
                return true;
            case R.id.action_delete:
                despublicar();
                return true;
            case R.id.action_republish:
                republicar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void despublicar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Realmente quiere despublicar su mascota?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MissingPet) pet).setState(MissingPetState.HIDDEN);
                        PetService petService = PetServiceFactory.getInstance();
                        PushService pushService = PushService.getInstance();
                        petService.saveMissingPet((MissingPet) pet);

                        RequestMissingService requestService = RequestMissingService.getInstance();
                        List<MissingRequest> requests = requestService.getAllMissingRequests((MissingPet) pet);
                        for (MissingRequest request : requests) {
                            if (request.getState().equals(RequestState.PENDING)) {
                                request.ignore();
                                requestService.save(request);
                                pushService.sendUnpublishRequestMissingPet(request);
                            }
                        }
                        onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void republicar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Realmente quiere republicar su mascota?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MissingPet) pet).setState(MissingPetState.PUBLISHED);
                        PetService petService = PetServiceFactory.getInstance();
                        PushService pushService = PushService.getInstance();
                        petService.saveMissingPet((MissingPet) pet);
                        RequestMissingService requestService = RequestMissingService.getInstance();
                        List<MissingRequest> requests = requestService.getAllMissingRequests((MissingPet) pet);
                        for (MissingRequest request : requests) {
                            if (request.getState().equals(RequestState.IGNORED)) {
                                request.pend();
                                requestService.save(request);
                                pushService.sendRepublishRequestMissingPet(request);
                            }
                        }
                        onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }


    private void volverAtras() {
        finish();
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }
}
