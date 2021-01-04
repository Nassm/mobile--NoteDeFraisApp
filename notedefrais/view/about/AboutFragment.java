package com.example.notedefrais.view.about;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.view.FragmentBase;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.viewmodel.about.AboutViewModel;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;


public class AboutFragment extends FragmentBase implements AboutViewModel.Action {

    public final static String TAG = "AboutFragment";
    private CompositeDisposable disposable;
    private TextView version;
    private ImageButton call;
    private AboutViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        disposable = new CompositeDisposable();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        if (disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        Objects.requireNonNull(getActivity()).setTitle(App.get().getString(R.string.A_PROPOS));
        version = v.findViewById(R.id.versionApp);
        call = v.findViewById(R.id.callPrisma);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        viewModel = new AboutViewModel(this, disposable);
    }

    @Override
    public void callPrismaSoft(String phoneNumber)
    {
        if(!isCallPermissionGranted())
        {
            Toasty.error(Objects.requireNonNull(getActivity()), getString(R.string.PERMISSION_APPEL), Toast.LENGTH_LONG, true).show();

            requestingCallPermission();
        } else
        {
            launchCall(Objects.requireNonNull(getActivity()), phoneNumber);
        }
    }

    @Override
    public void vmLoadingFinished(boolean isFinish)
    {
        if(isFinish)
        {
            version.setText(viewModel.getVersionApp());
            call.setOnClickListener(v -> viewModel.onClickCall());
        }
    }

    public static void launchCall(Activity activity, String phoneNumber)
    {
        Intent mobileCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        activity.startActivity(mobileCall);
    }

    private boolean isCallPermissionGranted()
    {
        return ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CALL_PHONE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestingCallPermission()
    {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CALL_PHONE}, NdfActivity.CALL_REQUEST_PERMISSION_CODE);
    }
}
