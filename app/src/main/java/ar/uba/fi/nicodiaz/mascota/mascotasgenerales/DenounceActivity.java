package ar.uba.fi.nicodiaz.mascota.mascotasgenerales;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;

public class DenounceActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_new_denounce);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                editText = (EditText) findViewById(R.id.comment_editText);
            }
        };
        WaitForInternet.setCallback(callback);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.lenguaje_indebido:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
            case R.id.spam:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
            case R.id.conducta_extrania:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
            case R.id.contenido_inadecuado:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
            case R.id.intento_comercio:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
            case R.id.fraude:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
            case R.id.otro:
                if (checked) {
                    // TODO: marcar la denuncia
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nueva_denuncia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_publish:
                new SaveDenounceTask().execute();  // TODO: guardar en base de datos
                volverAtras();
                return true;
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
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }

    private void volverAtras() {
        finish();
        overridePendingTransition(R.anim.slide_in_2, R.anim.slide_out_2);
    }

    private class SaveDenounceTask extends AsyncTask<Void, Integer, Boolean> {
        private String text;

        @Override
        protected void onPreExecute() {
            text = editText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: guardar en base de datos una denuncia.
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(DenounceActivity.this, "No se pudo publicar la denuncia.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DenounceActivity.this, "Denuncia enviada. ¡Gracias!", Toast.LENGTH_SHORT).show();
            }
            volverAtras();
        }
    }
}
