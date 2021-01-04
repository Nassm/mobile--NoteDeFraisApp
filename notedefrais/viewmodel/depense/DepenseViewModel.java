package com.example.notedefrais.viewmodel.depense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.lifecycle.ViewModel;
import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.view.depense.DepenseDetailsActivity;
import java.util.ArrayList;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DepenseViewModel extends ViewModel implements GenericDepenseViewModel {

    private IAction action;
    private CompositeDisposable disposable;
    private ArrayList<PrismaGestionCo_NDF_depense> depenses;
    private String TAG = DepenseViewModel.class.getSimpleName();


    @SuppressLint("CheckResult")
    public DepenseViewModel(IAction action, CompositeDisposable disposable, GenericEntity entity)
    {
        this.action = action;
        this.disposable = disposable;

        this.disposable.add(Single.fromCallable(() ->
        {
            try
            {
                return App.get().getDB().prismagestionco_ndf_depense_dao().getByProject(entity.getId());
            }
            catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage());
                this.action.error(e.getMessage());
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((loadedData) ->
                {
                    if (loadedData != null && loadedData.size() != 0)
                    {
                        this.depenses = ( (ArrayList<PrismaGestionCo_NDF_depense>) loadedData);
                        this.action.vmLoadingFinished(true);
                    }
                    else
                    {
                        this.action.error(App.get().getString(R.string.AUCUNE_DEPENSE));
                        this.action.vmLoadingFinished(false);
                    }
                }));
    }


    @Override
    public void onRowClick(Context c, PrismaGestionCo_NDF_depense depense)
    {
        Intent intent = DepenseDetailsActivity.launchDetail(c, depense);
        c.startActivity(intent);
    }


    public ArrayList<PrismaGestionCo_NDF_depense> getDepenses()
    {
        return depenses;
    }


    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);
    }
}
