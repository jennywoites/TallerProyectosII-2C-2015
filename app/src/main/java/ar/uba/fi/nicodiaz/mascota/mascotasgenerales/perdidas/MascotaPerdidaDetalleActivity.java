package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.perdidas;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class MascotaPerdidaDetalleActivity extends AppCompatActivity {
    CharSequence Titles[];
    int NumbOfTabs = 2;
    FloatingActionButton FAB_encontre;
    FloatingActionButton FAB_comment;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si dio adoptar:
        /*if (resultCode == Activity.RESULT_OK) {
           disableEncontreFAB();
        }*/
    }

    private void enableEncontreFAB() {
        FAB_encontre.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ColorPrimary)));
        FAB_encontre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MascotaPerdidaDetalleActivity.this, SolicitarPerdidaActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    private void disableEncontreFAB() {
        FAB_encontre.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.Disabled)));
        FAB_encontre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MascotaPerdidaDetalleActivity.this, "Ya ha enviado una solicitud.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int fragementView = getIntent().getIntExtra("fragementView", 0);
        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_mascota_perdida_detalle);

                Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
                setSupportActionBar(toolbar);
                //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                Pet pet = PetServiceFactory.getInstance().getSelectedPet();

                final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                collapsingToolbar.setTitle(pet.getName());

                FAB_encontre = (FloatingActionButton) findViewById(R.id.FAB_encontre);

               /* if (RequestService.getInstance().hasMissingRequestSent((MissingPet) pet)) {
                    disableEncontreFAB();
                } else {*/
                    enableEncontreFAB();
                //}

                FAB_comment = (FloatingActionButton) findViewById(R.id.FAB_comentar);
                FAB_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MascotaPerdidaDetalleActivity.this, NewCommentActivity.class);
                        startActivity(i);
                    }
                });

                loadHeader(pet);


                int commentsCount = CommentService.getInstance().getCount(pet.getID());
                Titles = new CharSequence[]{"Información", "Comentarios (" + String.valueOf(commentsCount) + ")"};

                ViewPagerAdapter adapter = new ViewPagerMascotaPerdidaDetalleAdapter(getSupportFragmentManager(), Titles, NumbOfTabs);
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
                                FAB_encontre.setVisibility(View.VISIBLE);
                                FAB_comment.setVisibility(View.GONE);
                                break;
                            case 1:
                                FAB_encontre.setVisibility(View.GONE);
                                FAB_comment.setVisibility(View.VISIBLE);
                                break;
                            default:
                                FAB_encontre.setVisibility(View.GONE);
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
        getMenuInflater().inflate(R.menu.menu_mascota_detalle, menu);
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
            case R.id.action_denounce:
                Intent i = new Intent(MascotaPerdidaDetalleActivity.this, DenounceActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
