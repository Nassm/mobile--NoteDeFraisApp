package com.example.notedefrais.viewmodel.ndf;

import android.annotation.SuppressLint;

import androidx.lifecycle.ViewModel;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NdfViewModel extends ViewModel {

    private String TAG = NdfViewModel.class.getSimpleName();
    private ArrayList<PrismaGestionCo_NDF> noteDeFrais;
    private IAction action;
    private CompositeDisposable disposable;
    public static final int VIEW_NDF = 1;
    public static final int ADD_NDF = 2;

    @SuppressLint("CheckResult")
    public NdfViewModel(IAction action, CompositeDisposable disposable, GenericEntity entity)
    {
        this.action = action;
        this.disposable = disposable;

        if(entity != null && entity.getId() != 0)
        {
            disposable.add(Single.fromCallable(() ->
            {
                try
                {
                    return App.get().getDB().prismagestionco_ndf_dao().getByIdProject(entity.getId());
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
                                this.noteDeFrais = ((ArrayList<PrismaGestionCo_NDF>) loadedData);

                                disposable.add(Single.fromCallable(()->
                                {
                                    try
                                    {
                                        for(PrismaGestionCo_NDF ndf : this.noteDeFrais)
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
                                    action.error(App.get().getString(R.string.AUCUNE_NOTE_DE_FRAIS));
                                    action.vmLoadingFinished(false);
                                }
                    }));
        } else
            {
                disposable.add(Single.fromCallable(() ->
                {
                    try
                    {
                        return App.get().getDB().prismagestionco_ndf_dao().get();
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
                                this.noteDeFrais = ( (ArrayList<PrismaGestionCo_NDF>) loadedData);


                                new CompositeDisposable().add(Single.fromCallable(()->
                                {
                                    try
                                    {
                                        for(PrismaGestionCo_NDF ndf : this.noteDeFrais)
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
                                action.error(App.get().getString(R.string.AUCUNE_NOTE_DE_FRAIS));
                                action.vmLoadingFinished(false);
                            }
                        }));
            }
    }

    public ArrayList<PrismaGestionCo_NDF> getNoteDeFrais()
    {
        return noteDeFrais;
    }

    public IAction getAction()
    {
        return action;
    }

    public CompositeDisposable getDisposable()
    {
        return disposable;
    }


    public void onItemNdfClick(PrismaGestionCo_NDF ndf)
    {
        action.startIntent(VIEW_NDF, ndf, null);
    }

    public void onAddExpenseReportClick()
    {
        action.startIntent(ADD_NDF, null, new ArrayList<>());
    }

    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);

        void startIntent(int tag, PrismaGestionCo_NDF ndf, ArrayList<PrismaGestionCo_NDF> ndfs);
    }
}