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

import java.util.Date;

import ar.uba.fi.nicodiaz.mascota.R;
import ar.uba.fi.nicodiaz.mascota.utils.CommentDB;

public class NewCommentActivity extends AppCompatActivity {

    private EditText editText;
    private int replyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        replyId = getIntent().getIntExtra("Parent", -1);
        Log.d(String.valueOf(Log.DEBUG), "replyTo: " + String.valueOf(replyId));

        editText = (EditText) findViewById(R.id.comment_editText);
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

            CommentDB comment = new CommentDB();
            comment.author = "foo"; // TODO: poner el usuario actual
            comment.text = text;
            comment.date = new Date(); // Ahora
            comment.id = -1; // TODO: hacer alguna forma de que sea secuencial y unico en toda la base de datos...
            comment.parent = replyId;
            comment.petId = 0; // TODO: poner la mascota actual que se esta haciendo el comentario.

            // TODO: Guardar comentario en la base de datos

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
