package ar.uba.fi.nicodiaz.mascota.MascotasGenerales;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.model.CommentDB;
import ar.uba.fi.nicodiaz.mascota.model.CommentService;
import ar.uba.fi.nicodiaz.mascota.model.Pet;
import ar.uba.fi.nicodiaz.mascota.model.PetService;
import ar.uba.fi.nicodiaz.mascota.model.User;
import ar.uba.fi.nicodiaz.mascota.model.UserService;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternet;
import ar.uba.fi.nicodiaz.mascota.utils.WaitForInternetCallback;

public class NewCommentActivity extends AppCompatActivity {

    private EditText editText;
    private String  parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WaitForInternetCallback callback = new WaitForInternetCallback(this) {
            public void onConnectionSuccess() {
                setContentView(R.layout.activity_new_comment);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                parentId = getIntent().getStringExtra("Parent");
                if (parentId == null || parentId.isEmpty()) {
                    parentId = "-1";
                }
                Log.d(String.valueOf(Log.DEBUG), "replyTo: " + String.valueOf(parentId));

                editText = (EditText) findViewById(R.id.comment_editText);
            }
        };
        WaitForInternet.setCallback(callback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_comentario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_publish:
                new SaveCommentTask().execute();  // TODO: guardar en base de datos
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

    private class SaveCommentTask extends AsyncTask<Void, Integer, Boolean> {
        private String text;

        @Override
        protected void onPreExecute() {
            text = editText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (text.equals("")) {
                return false;
            }

            Pet pet = PetService.getInstance().getSelectedPet();
            User user = UserService.getInstance().getUser();

            CommentDB comment = new CommentDB();
            comment.setAuthor(user);
            comment.setParentID(parentId);
            comment.setText(text);
            comment.setPetId(pet.getID());

            CommentService.getInstance().save(comment);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(NewCommentActivity.this, "No se pudo publicar el comentario.", Toast.LENGTH_SHORT).show();
            volverAtras();
        }
    }
}
