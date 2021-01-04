package com.example.notedefrais.viewmodel.depense;

import android.annotation.SuppressLint;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_vehicule;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_reglement_mode;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;
import java.util.ArrayList;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DepenseDetailsViewModel extends ViewModel {

    private Uri uri;
    private IAction action;
    private CompositeDisposable disposable;
    private PrismaGestionCo_NDF_depense depense;
    private ArrayList<PrismaGestionCo_projet> proj;
    private ArrayList<String> mProjetStringList, mReglementModeStringList, mVehiculeStringList ;
    private ArrayList<PrismaGestionCo_reglement_mode> mReglementModes;
    private ArrayList<PrismaGestionCo_NDF_vehicule> mVehiculesList;
    private String TAG = DepenseDetailsViewModel.class.getSimpleName();


    @SuppressLint("CheckResult")
    public DepenseDetailsViewModel(IAction action, CompositeDisposable disposable, PrismaGestionCo_NDF_depense entity, Uri uri)
    {
        this.uri = uri;
        this.action = action;
        this.disposable = disposable;
        this.depense = entity;
        this.proj = App.getProjs();
        if(proj != null)
        {
            this.mProjetStringList = new ArrayList<>(proj.size());
        } else {
            this.proj = (ArrayList<PrismaGestionCo_projet>)App.get().getDB().prismagestionco_projet_dao().get();
            this.mProjetStringList = new ArrayList<>(proj.size());
        }

        disposable.add(Single.fromCallable(() ->
        {
            try
            {
                return App.get().getDB().prismaGestionCo_reglement_mode_dao().get();
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
                        this.mReglementModes = ( (ArrayList<PrismaGestionCo_reglement_mode>) loadedData);

                        mReglementModeStringList = new ArrayList<>(this.mReglementModes.size());

                        for(PrismaGestionCo_reglement_mode reg : this.mReglementModes)
                        {
                            mReglementModeStringList.add(reg.getLibelle());
                        }
                        for(PrismaGestionCo_projet proj : this.proj)
                        {
                            mProjetStringList.add(proj.getNom());
                        }

                        disposable.add(Single.fromCallable(() ->
                        {
                            try
                            {
                                return App.get().getDB().prismagestionco_ndf_vehicule_dao().get();
                            }
                            catch (Exception e)
                            {
                                NdfLogger.e(TAG, e.getMessage());
                                return null;
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe((loaded) ->
                                {
                                    if (loaded != null && loaded.size() != 0)
                                    {
                                        this.mVehiculesList = ((ArrayList<PrismaGestionCo_NDF_vehicule>) loaded);
                                        mVehiculeStringList = new ArrayList<>(this.mVehiculesList.size());

                                        for(PrismaGestionCo_NDF_vehicule vehicle : this.mVehiculesList)
                                        {
                                            mVehiculeStringList.add(vehicle.getMarque());
                                        }
                                        action.vmLoadingFinished(true);
                                    }
                                    else
                                    {
                                        action.error(App.get().getString(R.string.AUCUNE_DONNEE));
                                    }
                                }));
                    }
                    else
                        {
                            action.error(App.get().getString(R.string.AUCUNE_DONNEE));
                            action.vmLoadingFinished(false);
                        }
                }));
    }

    public Uri getUri()
    {
        return uri;
    }

    public IAction getAction() {
        return action;
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public ArrayList<PrismaGestionCo_projet> getProj() {
        return proj;
    }

    public PrismaGestionCo_NDF_depense getDepense() {
        return depense;
    }

    public ArrayList<PrismaGestionCo_reglement_mode> getmReglementModes()
    {
        return mReglementModes;
    }

    public ArrayList<String> getmProjetStringList()
    {
        return mProjetStringList;
    }

    public ArrayList<String> getmReglementModeStringList()
    {
        return mReglementModeStringList;
    }

    public ArrayList<String> getmVehiculeStringList() { return mVehiculeStringList; }

    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);

        void buildDepenseObject();

        void validateDepenseType(String depenseType);
    }
}
