package ar.uba.fi.nicodiaz.mascota;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

public class MailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BackgroundMail bm = new BackgroundMail(this);
        bm.setGmailUserName("yourgmail@gmail.com");
        bm.setGmailPassword("yourgmailpassword");
        bm.setMailTo("receiver@gmail.com");
        bm.setFormSubject("Subject");
        bm.setFormBody("Body");
        bm.send();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
