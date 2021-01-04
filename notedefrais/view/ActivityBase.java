package com.example.notedefrais.view;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notedefrais.model.utils.ICommunication;

import es.dmoral.toasty.Toasty;

public abstract class ActivityBase extends AppCompatActivity implements ICommunication {

    @Override
    public void success(String s) {
        Toasty.success(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String s) {
        Toasty.error(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(String s) {
        Toasty.warning(this, s, Toast.LENGTH_SHORT).show();
    }

}
