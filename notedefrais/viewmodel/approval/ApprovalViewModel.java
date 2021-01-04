package com.example.notedefrais.viewmodel.approval;

import android.annotation.SuppressLint;

import androidx.lifecycle.ViewModel;
import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;

import java.util.ArrayList;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ApprovalViewModel extends ViewModel {

    private String TAG = ApprovalViewModel.class.getSimpleName();
    private ArrayList<PrismaGestionCo_NDF> mApproval;
    private IAction action;
    private CompositeDisposable disposable;
    public static final int VIEW_APPROVAL = 1;


    @SuppressLint("CheckResult")
    public ApprovalViewModel(IAction action, CompositeDisposable disposable)
    {
        this.action = action;
        this.disposable = disposable;

        disposable.add(Single.fromCallable(() ->
        {
            try
            {
                return App.get().getDB().prismagestionco_ndf_dao().getByApproval();
            }
            catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage());
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loadedData) ->
                    {
                        if (loadedData != null && loadedData.size() != 0)
                        {
                            this.mApproval = ( (ArrayList<PrismaGestionCo_NDF>) loadedData);

                            disposable.add(Single.fromCallable(()->
                            {
                                try
                                {
                                    for(PrismaGestionCo_NDF ndf : this.mApproval)
                                    {
                                        Float amount = 0.0f;
                                        ndf.setDepenses(App.get().getDB().prismagestionco_ndf_depense_dao().getByIdSmartphoneNDF(ndf.getIdSmartphone()));
                                        ndf.setDepenseCount(String.valueOf(ndf.getDepenses().size()));
                                        for(PrismaGestionCo_NDF_depense depense : ndf.getDepenses())
                                        {
                                            amount += depense.getMontantTTC();
                                        }
                                        ndf.setAmount(amount);
                                    }
                                    return "";
                                }
                                catch (Exception e)
                                {
                                    NdfLogger.e(TAG, e.getMessage());
                                    return null;
                                }
                            }).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe((error) ->
                                    {
                                        if(error == "")
                                        {
                                            action.vmLoadingFinished(true);
                                        } else
                                        {
                                            action.error(error);
                                        }
                                    }));
                        } else
                        {
                            action.error(App.get().getString(R.string.AUCUNE_APPROVATION));
                            action.vmLoadingFinished(false);
                        }
                    }));
    }

    public ArrayList<PrismaGestionCo_NDF> getApproval()
    {
        return mApproval;
    }

    public IAction getAction()
    {
        return action;
    }

    public CompositeDisposable getDisposable()
    {
        return disposable;
    }

    public void onApprovalItemClick(PrismaGestionCo_NDF ndf)
    {
        action.startIntent(VIEW_APPROVAL, ndf);
    }

    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);

        void startIntent(int tag, PrismaGestionCo_NDF ndf);
    }
}