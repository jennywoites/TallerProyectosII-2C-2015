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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.CommentComplaint;
import ar.uba.fi.nicodiaz.mascota.model.CommentDB;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.model.service.impl.ComplaintService;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;

public class DenounceCommentActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_new_denounce_comment);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                editText = (EditText) findViewById(R.id.comment_editText);
            }
        };
        WaitForInternet.setCallback(callback);
    }

    public void onRadioButtonClicked(View view) {
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
                if (!this.validate()) {
                    return false;
                }
                if (UserService.getInstance().isBanned(ParseUser.getCurrentUser())) {
                    Toast.makeText(DenounceCommentActivity.this, "Ocurrió un problema al denunciar. Intente más tarde.", Toast.LENGTH_SHORT).show();
                    volverAtras();
                    return false;
                }
                new SaveDenounceTask().execute();
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

    private String getRazon() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
        return (((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString());
    }

    private boolean validate() {
        return checkOpciones(R.id.radiogroup, R.id.textview);
    }

    private boolean checkOpciones(int radioGrupoId, int labelId) {
        String errorOpciones = getResources().getString(R.string.MASCOTA_ADOPCION_ERROR_OPCIONES_VACIAS);
        RadioGroup rg = (RadioGroup) findViewById(radioGrupoId);
        TextView textView = (TextView) findViewById(labelId);

        if (rg.getCheckedRadioButtonId() == -1) {
            textView.setError(errorOpciones);
            return false;
        } else {
            textView.setError(null);
            return true;
        }
    }

    private class SaveDenounceTask extends AsyncTask<Void, Integer, Boolean> {
        private String text;

        @Override
        protected void onPreExecute() {
            text = editText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            CommentComplaint complaint = createComplaint();
            ComplaintService.getInstance().saveCommentComplaint(complaint);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                Toast.makeText(DenounceCommentActivity.this, "No se pudo publicar la denuncia.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(DenounceCommentActivity.this, "Denuncia enviada. ¡Gracias!", Toast.LENGTH_SHORT).show();
            }
            volverAtras();
        }
    }

    private CommentComplaint createComplaint() {
        CommentDB commentDB = CommentService.getInstance().getSelectedComment();
        CommentComplaint complaint = new CommentComplaint();
        complaint.setInformed(commentDB.getAuthor());
        complaint.setInformer(UserService.getInstance().getUser());
        complaint.setComment(commentDB);
        complaint.setKind(getRazon());
        String info = editText.getText().toString();
        complaint.setInfo(info);
        return complaint;
    }
}
