package com.example.notedefrais.view.identification;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notedefrais.App;
import com.example.notedefrais.BuildConfig;
import com.example.notedefrais.R;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.networking.adapter.RetrofitAdapter;

import es.dmoral.toasty.Toasty;

public class FirstConnectionActivity extends AppCompatActivity {

    private String TAG = FirstConnectionActivity.class.getSimpleName();
    public static final int CODE = 1;
    private Boolean exit = false;
    private EditText et_url_svc;
    private Button btn_valider;
    private String url_svc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_connection);

        btn_valider = findViewById(R.id.btn_valider);
        et_url_svc = findViewById(R.id.et_url);
        et_url_svc.setText(BuildConfig.URL_SVC);

        btn_valider.setOnClickListener(view ->
        {
            try
            {
                url_svc = et_url_svc.getText().toString();
                if (!url_svc.equals(""))
                {
                    App.setBaseUrl(url_svc);
                    RetrofitAdapter.createNewInstance(); /** help to check if url is valid */

                    setResult(CODE);
                    finish();
                }
                else
                    {
                        Toasty.warning(FirstConnectionActivity.this, getString(R.string.VEUILLEZ_ENTRER_URL), Toast.LENGTH_LONG).show();
                        btn_valider.setEnabled(true);
                    }
            }
            catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage());
                Toasty.warning(this, getString(R.string.ERROR_GLOBAL), Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed()
    {
        if (exit)
        {
            finishAffinity();
        }
        else
        {
            Toasty.info(this, getString(R.string.APPUYER_DE_NOUVEAU_POUR_QUITTER), Toast.LENGTH_LONG).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 3000);
        }
    }

}
