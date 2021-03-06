package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.samples.apps.iosched.ui.widget.SlidingTabLayout;
import com.google.samples.apps.iosched.ui.widget.ViewPagerAdapter;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.DenounceActivity;
import ar.uba.fi.nicodiaz.mascota.mascotasgenerales.NewCommentActivity;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.model.FoundPet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;


public class MascotaEncontradaDetalleActivity extends AppCompatActivity {

    CharSequence Titles[];
    int NumbOfTabs = 2;
    FloatingActionButton FAB_esMia;
    FloatingActionButton FAB_comment;
    private Pet pet;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si dio adoptar:
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            disableEsMiaFAB();
        }
    }

    private void enableEsMiaFAB() {
        FAB_esMia.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ColorPrimary)));
        FAB_esMia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MascotaEncontradaDetalleActivity.this, SolicitarEncontradaActivity.class);
                startActivityForResult(i, 123);
            }
        });
    }

    private void disableEsMiaFAB() {
        FAB_esMia.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Disabled)));
        FAB_esMia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MascotaEncontradaDetalleActivity.this, "Ya ha enviado una solicitud.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int fragementView = getIntent().getIntExtra("fragementView", 0);
        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_mascota_encontrada_detalle);

                Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
                setSupportActionBar(toolbar);
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                pet = PetServiceFactory.getInstance().getSelectedPet();

                final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                //collapsingToolbar.setTitle(pet.getName());

                FAB_esMia = (FloatingActionButton) findViewById(R.id.FAB_esMia);

                if (RequestService.getInstance().hasFoundRequestSent((FoundPet) pet)) {
                    disableEsMiaFAB();
                } else {
                    enableEsMiaFAB();
                }

                FAB_comment = (FloatingActionButton) findViewById(R.id.FAB_comentar);
                FAB_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MascotaEncontradaDetalleActivity.this, NewCommentActivity.class);
                        startActivity(i);
                    }
                });

                loadHeader(pet);


                int commentsCount = CommentService.getInstance().getCount(pet.getID());
                Titles = new CharSequence[]{"Información", "Comentarios (" + String.valueOf(commentsCount) + ")"};

                ViewPagerAdapter adapter = new ViewPagerMascotaEncontradaDetalleAdapter(getSupportFragmentManager(), Titles, NumbOfTabs);
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
                                FAB_esMia.setVisibility(View.VISIBLE);
                                FAB_comment.setVisibility(View.GONE);
                                break;
                            case 1:
                                FAB_esMia.setVisibility(View.GONE);
                                FAB_comment.setVisibility(View.VISIBLE);
                                break;
                            default:
                                FAB_esMia.setVisibility(View.GONE);
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
        getMenuInflater().inflate(R.menu.menu_mascota_encontrada_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_share:
                compartir();
                return true;
            case R.id.action_close:
                volverAtras();
                return true;
            case R.id.action_denounce:
                Intent i = new Intent(MascotaEncontradaDetalleActivity.this, DenounceActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void compartir() {
        ShareDialog shareDialog = new ShareDialog(this);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("¡Se encontró una Mascota!")
                    .setContentDescription(pet.getDescription() + " - 'Mascota', la app para publicar tus mascotas. ¡Disponible en Google Play ahora!")
                    .setImageUrl(Uri.parse(pet.getPicture().getUrl()))
                    .setContentUrl(Uri.parse("https://play.google.com"))
                    .build();
            shareDialog.show(linkContent);
        }
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
