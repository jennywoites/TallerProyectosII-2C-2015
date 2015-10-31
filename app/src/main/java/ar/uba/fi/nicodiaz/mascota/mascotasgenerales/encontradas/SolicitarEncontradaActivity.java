package ar.uba.fi.nicodiaz.mascota.mascotasgenerales.encontradas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.Address;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionRequest;
import ar.uba.fi.nicodiaz.mascota.model.FoundRequest;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PushService;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.model.RequestState;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.service.FoundPet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;
import ar.uba.fi.nicodiaz.mascota.utils.service.PetServiceFactory;

public class SolicitarEncontradaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_solicitar_encontrada);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                final FoundPet pet = (FoundPet) PetServiceFactory.getInstance().getSelectedPet();
                ((TextView) findViewById(R.id.titulo)).setText("¿Piensas que está es tu Mascota?");
                ((TextView) findViewById(R.id.infSexoPet)).setText(pet.getGender());
                ((TextView) findViewById(R.id.infRazaPet)).setText(pet.getBreed());
                ((TextView) findViewById(R.id.infEspeciePet)).setText(pet.getKind());

                Address lastKnowAddress = pet.getLastKnowAddress();
                if (lastKnowAddress.getSubLocality() != null) {
                    ((TextView) findViewById(R.id.infUbicacion)).setText(lastKnowAddress.getSubLocality());
                } else if (lastKnowAddress.getLocality() != null) {
                    ((TextView) findViewById(R.id.infUbicacion)).setText(lastKnowAddress.getLocality());
                } else {
                    ((TextView) findViewById(R.id.infUbicacion)).setText("");
                }

                final ParseImageView imageView = new ParseImageView(mActivity);
                ParseFile photoFile = pet.getPicture();
                if (photoFile != null) {
                    imageView.setParseFile(photoFile);
                    imageView.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            try {
                                ((ImageView) findViewById(R.id.imageView)).setImageDrawable(imageView.getDrawable());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }

                Button adoptBtn = (Button) findViewById(R.id.mine);
                adoptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Guardar en base de datos request de adopcion para la mascota actual.
                        if (!validate()) {
                            return;
                        }
                        String message = ((EditText) findViewById(R.id.comment_editText)).getText().toString();
                        User user = UserService.getInstance().getUser();
                        FoundRequest foundRequest = new FoundRequest();
                        foundRequest.setMessage(message);
                        foundRequest.setState(RequestState.PENDING);
                        foundRequest.setFoundPet(pet);
                        foundRequest.setRequestingUser(user);
                        foundRequest.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime()));

                        RequestService.getInstance().save(foundRequest);
                        PushService.getInstance().sendRequestFoundPet(pet);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                });

                Button cancelBtn = (Button) findViewById(R.id.cancel);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        volverAtras();
                    }
                });
            }
        };
        WaitForInternet.setCallback(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solicitar_encontrada, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        volverAtras();
    }

    private void volverAtras() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }

    private boolean validate() {
        boolean valid = true;

        EditText commentEditText = (EditText) findViewById(R.id.comment_editText);
        String comment = ((EditText) findViewById(R.id.comment_editText)).getText().toString();
        String errorComment = getResources().getString(R.string.MASCOTA_SOLICITUD_ERROR_EMPTY_COMMENT);

        if (comment.isEmpty()) {
            commentEditText.setError(errorComment);
            valid = false;
        } else {
            commentEditText.setError(null);
        }

        return (valid);
    }
}
