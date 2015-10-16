package ar.uba.fi.nicodiaz.mascota.utils;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;

import ar.uba.fi.nicodiaz.mascota.R;

/**
 * Created by nicolas on 16/10/15.
 */
public abstract class WaitForInternetCallback {
    protected Activity mActivity;

    public WaitForInternetCallback(Activity activity) {
        mActivity = activity;
    }

    public abstract void onConnectionSuccess();

    public void onConnectionFailure() {
        mActivity.setContentView(R.layout.disconnected);
        ImageButton retry = (ImageButton) mActivity.findViewById(R.id.retry_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.recreate();
            }
        });
    }
}
