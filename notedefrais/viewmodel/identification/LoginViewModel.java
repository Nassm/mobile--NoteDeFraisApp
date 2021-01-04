package com.example.notedefrais.viewmodel.identification;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.notedefrais.App;
import com.example.notedefrais.BuildConfig;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_smartphone_parameter;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.networking.models.ResultLogin;
import com.example.notedefrais.networking.models.ProgressPullModel;
import com.example.notedefrais.networking.utils.ICallback;
import com.example.notedefrais.networking.utils.NetworkingUtils;
import com.example.notedefrais.networking.utils.SynchronizationTask;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel {

    private IActions actions;
    private boolean firstConnection;
    public CompositeDisposable disposable;
    private String TAG = LoginViewModel.class.getSimpleName();

    public LoginViewModel(IActions actions, CompositeDisposable disposable, boolean firstConnection)
    {
        this.actions = actions;
        this.disposable = disposable;
        this.firstConnection = firstConnection;
    }

    public void onLoginClick(String login, String password)
    {
        if (login != null && !login.equals("") && password != null && !password.equals(""))
        {
            Call<ResultLogin> call = NetworkingUtils.getApiInstance().login(BuildConfig.KEYCHAIN, login, password);
            call.enqueue(new Callback<ResultLogin>()
            {
                @Override
                public void onResponse(Call<ResultLogin> call, Response<ResultLogin> response)
                {
                    if (response.isSuccessful())
                    {
                        ResultLogin resultLogin = response.body();
                        if (resultLogin != null)
                        {
                            loginSucceeded(resultLogin);

                        } else
                            {
                                actions.message(App.get().getString(R.string.IDENTIFIANTS_INCORRECT));
                                NdfLogger.e(TAG, App.get().getString(R.string.IDENTIFIANTS_INCORRECT));
                            }
                    } else
                        {
                            actions.message(App.get().getString(R.string.ERROR_COMMUNICATION_SERVER));
                            NdfLogger.e(TAG, App.get().getString(R.string.ERROR_COMMUNICATION_SERVER));


                            if (firstConnection)
                            {
                                actions.launchFirstConnectionActivity();
                            }
                        }
                }

                @Override
                public void onFailure(Call<ResultLogin> call, Throwable t)
                {
                    actions.message(App.get().getString(R.string.ERROR_COMMUNICATION_SERVER));
                    NdfLogger.e(TAG, App.get().getString(R.string.ERROR_COMMUNICATION_SERVER));

                    if (firstConnection)
                        actions.launchFirstConnectionActivity();
                }
            });
        }
        else
            {
                actions.message(App.get().getString(R.string.IDENTIFIANTS_INCORRECT));
            }
    }


    @SuppressLint("NewApi")
    private void loginSucceeded(ResultLogin resultLogin)
    {
        App.setUserId(resultLogin.getUserId());
        App.setUserName(resultLogin.getUsername());

        disposable.add(App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().getSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(smartphoneParameter ->
                        {
                            if (smartphoneParameter.isPresent())
                            {
                                actions.launchNdfActivity();
                            } else
                                {
                                    fillDatabase();
                                }
                        }
                        , throwable -> Log.e("",""))); //put logger instead throwable
    }


    /** Filling database according || on complete start ndf activity*/
    private void fillDatabase()
    {
        actions.showProgressDialog();

        SynchronizationTask.pullAll(new ICallback<ProgressPullModel>()
        {
            @Override
            public void returnError(String message)
            {
                actions.cancelProgressDialog();
                actions.error(App.get().getString(R.string.ERROR_RECUPERATION_DONNEES));
            }

            @Override
            public void returnProgress(String entityName, int progress)
            {
                actions.updateMessageProgressDialog(entityName, progress);
            }

            @Override
            public void complete()
            {
                actions.cancelProgressDialog();

                /** get back object created and set url || help to ensure app won't check data to DB at the next launch */
                PrismaGestionCo_NDF_smartphone_parameter geodp_smartphone_parameter = App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get();
                geodp_smartphone_parameter.setUrl(App.getBaseUrl());

                new Thread
                        (
                                () -> App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().insert(geodp_smartphone_parameter)
                        ).start();

                actions.launchNdfActivity();
            }
        });
    }


    /** Interface for the reference activity */
    public interface IActions extends ICommunication
    {
        void launchFirstConnectionActivity();

        void launchNdfActivity();

        void showProgressDialog();

        void updateMessageProgressDialog(String entityName, int progress);

        void cancelProgressDialog();
    }
}
