package com.tiff.tiffinbox.authentication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.tiff.tiffinbox.Validate;

public class AuthenticationPresenterLayer extends AppCompatActivity implements AuthenticationIntractor {

    Context mcontext;

    AuthenticationPresenterLayer(Context context) {
        this.mcontext = context;
    }

    @Override
    public void progressbarShow(ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void progressbarHide(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToAhead(Class<?> cls) {
        mcontext.startActivity(new Intent(mcontext, cls));
    }

}