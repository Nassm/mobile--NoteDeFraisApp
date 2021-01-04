package com.example.notedefrais.viewmodel.about;

import android.content.pm.PackageInfo;
import androidx.lifecycle.ViewModel;
import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_smartphone_parameter;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AboutViewModel extends ViewModel {

    private PrismaGestionCo_NDF_smartphone_parameter smartphone_parameter;
    private Action action;
    private String versionApp;
    private String TAG = AboutViewModel.class.getSimpleName();


    public AboutViewModel(Action action, CompositeDisposable disposable)
    {
        this.action = action;

        disposable.add(Single.fromCallable(() ->
        {
            try
            {
                smartphone_parameter = App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get();

                return "";
            } catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage(), e);
                return e.getMessage();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((error) -> {
                    if (!error.equals(""))
                    {
                        action.error(error);
                        action.vmLoadingFinished(false);
                    }
                    else
                        {
                            PackageInfo pInfo = App.get().getPackageManager().getPackageInfo(App.get().getPackageName(), 0);
                            String version = pInfo.versionName;
                            versionApp = "PrismaSoft - Note de frais V. " + version;
                            action.vmLoadingFinished(true);
                        }
                }));
    }

    public void onClickCall()
    {
        if (smartphone_parameter != null && smartphone_parameter.getPrismaPhoneNumer() != null)
        {
            action.callPrismaSoft(smartphone_parameter.getPrismaPhoneNumer());
        } else
            {
                action.message(App.get().getString(R.string.AUCUN_NUMERO));
            }
    }


    public String getVersionApp()
    {
        return versionApp;
    }


    public interface Action extends ICommunication
    {
        void callPrismaSoft(String phoneNumber);

        void vmLoadingFinished(boolean isFinish);
    }


}
