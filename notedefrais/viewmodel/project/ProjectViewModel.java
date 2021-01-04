package com.example.notedefrais.viewmodel.project;

import com.example.notedefrais.App;
import com.example.notedefrais.R;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import com.example.notedefrais.model.utils.ICommunication;
import com.example.notedefrais.model.utils.NdfLogger;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectViewModel {

    private String TAG = ProjectViewModel.class.getSimpleName();
    private ArrayList<PrismaGestionCo_projet> projects;
    private IAction action;
    private CompositeDisposable disposable;

    public static final int VIEW_NDF_TAG = 1;
    public static final int VIEW_DEPENSE_TAG = 2;

    public ProjectViewModel(IAction action, CompositeDisposable disposable)
    {
        this.action = action;
        this.disposable = disposable;

        disposable.add(Single.fromCallable(() ->
        {
            try
            {
                return App.get().getDB().prismagestionco_projet_dao().get();
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
                        this.projects = ( (ArrayList<PrismaGestionCo_projet>) loadedData);

                        action.vmLoadingFinished(true);

                        if(App.getProjs() == null || App.getProjs().size() < this.projects.size())
                        {
                            App.setProjs(this.projects);
                        }
                    } else
                        {
                            action.error(App.get().getString(R.string.AUCUN_PROJET));
                            action.vmLoadingFinished(false);
                        }
                }));
    }
    public void onNdfClick(PrismaGestionCo_projet project)
    {
        action.startIntent(VIEW_NDF_TAG, project);
    }
    public void onDepenseClick(PrismaGestionCo_projet project)
    {
        action.startIntent(VIEW_DEPENSE_TAG, project);
    }

    public ArrayList<PrismaGestionCo_projet> getProjects()
    {
        return projects;
    }

    public IAction getAction()
    {
        return action;
    }


    public interface IAction extends ICommunication
    {
        void vmLoadingFinished(boolean isFinish);

        void startIntent(int tag, PrismaGestionCo_projet company);
    }
}
