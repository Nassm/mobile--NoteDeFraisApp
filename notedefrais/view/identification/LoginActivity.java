package com.example.notedefrais.view.identification;

import androidx.appcompat.app.ActionBar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.constante.ConstanteDataBase;
import com.example.notedefrais.networking.job.RemoteDataStore;
import com.example.notedefrais.view.ActivityBase;
import com.example.notedefrais.view.NdfActivity;
import com.example.notedefrais.viewmodel.identification.LoginViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends ActivityBase implements LoginViewModel.IActions {

    private String TAG = LoginActivity.class.getSimpleName();
    private Button valider;
    private boolean exit = false;
    private LoginViewModel viewModel;
    private EditText login, password;
    private CompositeDisposable disposable;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        disposable = new CompositeDisposable();
        disposable.add(App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().getSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(smartphoneParameter -> {
                            if (smartphoneParameter.isPresent())
                            {
                                identification(false);
                            } else {
                                launchFirstConnectionActivity();
                            }
                        }
                        , throwable -> Log.e("",""))); //put logger instead throwable
    }

    /* Init of view after verification of base */
    private void identification(boolean firstConnection)
    {
        if (disposable == null || disposable.isDisposed())
        {
            disposable = new CompositeDisposable();
        }
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.etLogin);
        password = findViewById(R.id.etPassword);
        valider = findViewById(R.id.btnLog);

        viewModel = new LoginViewModel(this, disposable, firstConnection);

        valider.setOnClickListener(v ->
        {
            String log = login.getText().toString();
            String pass = password.getText().toString();
            viewModel.onLoginClick(log, pass);
        });

        displayHomeAsUpEnabled();
    }

    private void displayHomeAsUpEnabled()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FirstConnectionActivity.CODE)
        {
            identification(true);
        }
    }

    @Override
    public void launchFirstConnectionActivity()
    {
        Intent intent = new Intent(LoginActivity.this, FirstConnectionActivity.class);
        LoginActivity.this.startActivityForResult(intent, FirstConnectionActivity.CODE);
    }

    @Override
    public void launchNdfActivity()
    {
        /* Synchronisation avec le serveur à la connexion */
        new RemoteDataStore().sync(null, 0);

        startActivity(NdfActivity.launchDetail(this));

        finish();
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        if (exit) {
            finishAffinity();
        } else {
            Toast.makeText(this, getString(R.string.APPUYER_DE_NOUVEAU_POUR_QUITTER), Toast.LENGTH_LONG).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null)
        {
            if (viewModel.disposable != null && !viewModel.disposable.isDisposed()) {
                viewModel.disposable.dispose();
            }
        }
        if (disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }

    @Override
    public void showProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(this.getString(R.string.CHARGEMENT_DES_DONNEES));
        progressDialog.setMessage(getString(R.string.PATIENTER_COMMUNICATION_SERVEUR));
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setMax(ConstanteDataBase.NUMBER_OF_SYNCHRONIZED_TABLES);
        progressDialog.show();
    }

    @Override
    public void updateMessageProgressDialog(String entityName, int progress)
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.setMessage(getString(R.string.CHARGEMENT_DES) + " : " + entityName);
            progressDialog.setProgress(progress);
        }
    }

    @Override
    public void cancelProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.cancel();
        }
    }
}
