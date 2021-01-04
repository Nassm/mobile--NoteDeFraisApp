package com.example.notedefrais.viewmodel.ndf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;
import com.example.notedefrais.App;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.viewmodel.depense.GenericDepenseViewModel;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.view.depense.DepenseDetailsActivity;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NdfDetailsDepenseViewModel extends ViewModel implements GenericDepenseViewModel {
    private IAction action;
    private PrismaGestionCo_NDF ndf;
    private CompositeDisposable disposable;
    private ArrayList<PrismaGestionCo_NDF_depense> ndfDepenses;
    private String TAG = NdfDetailsDepenseViewModel.class.getSimpleName();


    @SuppressLint("CheckResult")
    public NdfDetailsDepenseViewModel(IAction action, CompositeDisposable disposable, GenericEntity entity)
    {
        this.action = action;
        this.disposable = disposable;
        this.ndf = (PrismaGestionCo_NDF)entity;

        disposable.add(Single.fromCallable(() ->
        {
            try
            {
                return App.get().getDB().prismagestionco_ndf_depense_dao().getByIdSmartphoneNDF(entity.getIdSmartphone());
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
                        this.ndfDepenses = ((ArrayList<PrismaGestionCo_NDF_depense>) loadedData);
                        action.vmLoadingFinished(true);
                    } else
                        {
                            action.vmLoadingFinished(true);
                        }
                }));
    }

    @Override
    public void onRowClick(Context c, PrismaGestionCo_NDF_depense depense)
    {
        Intent intent = DepenseDetailsActivity.launchDetail(c, depense);
        c.startActivity(intent);
    }

    public IAction getAction()
    {
        return action;
    }

    public PrismaGestionCo_NDF getNdf()
    {
        return ndf;
    }

    public CompositeDisposable getDisposable()
    {
        return disposable;
    }

    public ArrayList<PrismaGestionCo_NDF_depense> getNdfDepenses()
    {
        return ndfDepenses;
    }

    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);

        void onNdfSubmit();

        void onNdfUpdate();

        void onNdfDelete();

        void onDepenseAdd();
    }
}




