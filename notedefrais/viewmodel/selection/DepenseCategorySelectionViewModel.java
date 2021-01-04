package com.example.notedefrais.viewmodel.selection;

import android.annotation.SuppressLint;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_categorie;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DepenseCategorySelectionViewModel {

    private String TAG = DepenseCategorySelectionViewModel.class.getSimpleName();
    private ArrayList<PrismaGestionCo_NDF_depense_categorie> mDepenseCategories;
    private IAction action;
    private CompositeDisposable disposable;
    public static final int SELECT_CATEGORIE = 1;


    @SuppressLint("CheckResult")
    public DepenseCategorySelectionViewModel(IAction action, CompositeDisposable disposable)
    {
        this.action = action;
        this.disposable = disposable;

        this.disposable.add(Single.fromCallable(() ->
        {
            try
            {
                return App.get().getDB().prismagestionco_ndf_depense_categorie_dao().getByActivee();
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
                        this.mDepenseCategories = ( (ArrayList<PrismaGestionCo_NDF_depense_categorie>) loadedData);
                        action.vmLoadingFinished(true);
                    }
                    else
                        {
                            action.error(App.get().getString(R.string.AUCUNE_DONNEE));
                            action.vmLoadingFinished(false);
                        }
                }));
    }

    public ArrayList<PrismaGestionCo_NDF_depense_categorie> getmDepenseCategories()
    {
        return mDepenseCategories;
    }

    public IAction getAction()
    {
        return action;
    }

    public CompositeDisposable getDisposable()
    {
        return disposable;
    }

    public void onItemDepenseCategoryClick(PrismaGestionCo_NDF_depense_categorie categorie)
    {
        action.startIntent(SELECT_CATEGORIE, categorie);
    }


    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);

        void startIntent(int tag, PrismaGestionCo_NDF_depense_categorie dep);
    }
}
