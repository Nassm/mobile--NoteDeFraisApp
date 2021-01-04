package com.example.notedefrais.view;

import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.notedefrais.model.utils.ICommunication;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public abstract class FragmentBase extends Fragment implements ICommunication {


    @Override
    public void success(String s) {
        Toasty.success(Objects.requireNonNull(getContext()), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String s) {
        Toasty.error(Objects.requireNonNull(getContext()), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(String s) {
        Toasty.warning(Objects.requireNonNull(getContext()), s, Toast.LENGTH_SHORT).show();
    }
}
