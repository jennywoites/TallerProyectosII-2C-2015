package ar.uba.fi.nicodiaz.mascota;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BannedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banned);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
