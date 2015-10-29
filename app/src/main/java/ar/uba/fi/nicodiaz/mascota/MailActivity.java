package ar.uba.fi.nicodiaz.mascota;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

public class MailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BackgroundMail bm = new BackgroundMail(this);
        bm.setGmailUserName("yourgmail@gmail.com");
        bm.setGmailPassword("yourgmailpassword");
        bm.setMailTo("receiver@gmail.com");
        bm.setFormSubject("Subject");
        bm.setFormBody("Body");
        bm.send();
    }

}
