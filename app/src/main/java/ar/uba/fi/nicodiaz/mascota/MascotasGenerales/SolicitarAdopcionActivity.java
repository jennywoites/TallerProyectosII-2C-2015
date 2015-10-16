package ar.uba.fi.nicodiaz.mascota.MascotasGenerales;

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
import ar.uba.fi.nicodiaz.mascota.model.AdoptionPet;
import ar.uba.fi.nicodiaz.mascota.model.AdoptionRequest;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.RequestService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;

public class SolicitarAdopcionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_adopcion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // TODO: setear todos los campos para la mascota actual
        final Pet pet = PetService.getInstance().getSelectedPet();
        ((TextView) findViewById(R.id.titulo)).setText("¿Quiéres adoptar a " + pet.getName() + "?");
        ((TextView) findViewById(R.id.infSexoPet)).setText(pet.getGender());
        ((TextView) findViewById(R.id.infRazaPet)).setText(pet.getBreed());
        ((TextView) findViewById(R.id.infEdadPet)).setText(pet.getAgeRange());
        ((TextView) findViewById(R.id.infUbicacion)).setText(pet.getAddress().getSubLocality());
        final ParseImageView imageView = new ParseImageView(this);
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

        Button adoptBtn = (Button) findViewById(R.id.adopt);
        adoptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar en base de datos request de adopcion para la mascota actual.
                if (!validate()) {
                    return;
                }
                String message = ((EditText) findViewById(R.id.comment_editText)).getText().toString();
                User user = UserService.getInstance().getUser();
                AdoptionRequest adoptionRequest = new AdoptionRequest();
                adoptionRequest.setMessage(message);
                adoptionRequest.setState("ENVIADA");
                adoptionRequest.setAdoptionPet((AdoptionPet) pet);
                adoptionRequest.setRequestingUser(user);
                adoptionRequest.setDate(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime()));

                RequestService.getInstance().save(adoptionRequest);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solicitar_adopcion, menu);
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
