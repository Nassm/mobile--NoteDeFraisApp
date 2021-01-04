package com.example.notedefrais.networking.utils;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Pair;
import com.example.notedefrais.App;
import com.example.notedefrais.BuildConfig;
import com.example.notedefrais.model.constante.ConstanteDataBase;
import com.example.notedefrais.model.database.Database;
import com.example.notedefrais.model.database.tables.GenericEntity;
import com.example.notedefrais.model.database.pojo.SynchronizationState;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_synchronisation;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_synchronisation_line;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_smartphone_parameter;
import com.example.notedefrais.model.utils.Helper;
import com.example.notedefrais.model.utils.NdfLogger;
import com.example.notedefrais.networking.models.ChangeModel;
import com.example.notedefrais.networking.models.ProgressPullModel;
import com.example.notedefrais.networking.models.PullResponseModel;
import org.json.JSONArray;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SynchronizationTask {
    public static final String TAG = SynchronizationTask.class.getSimpleName();

    public static void pullAll(final ICallback<ProgressPullModel> ICallback)
    {
        try
        {
            NetworkingUtils.getApiInstanceForJob()
                    .pullAll(BuildConfig.KEYCHAIN, App.getUserId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .concatMap((Function<PullResponseModel, Observable<ProgressPullModel>>) pullResponseModel ->
                    {
                        PrismaGestionCo_NDF_smartphone_parameter smartphoneParameter = App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get();
                        if (smartphoneParameter == null)
                        {
                            smartphoneParameter = new PrismaGestionCo_NDF_smartphone_parameter();
                            //smartphoneParameter.setId(Integer.valueOf("1"));
                        }
                        smartphoneParameter.setDateEndLastSynchro(pullResponseModel.getLastUpdatedDate());
                        App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().insert(smartphoneParameter);

                        return insertChangesObservable(pullResponseModel);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ProgressPullModel>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                            NdfLogger.d(TAG, "onSubscribe");
                            App.get().getDB().query("PRAGMA foreign_keys=OFF;",null);
                        }

                        @Override
                        public void onNext(ProgressPullModel progressPullModel)
                        {
                            NdfLogger.d(TAG, "onNext: " + progressPullModel.getEntityName());
                            ICallback.returnProgress(progressPullModel.getEntityName(), progressPullModel.getProgress());
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            NdfLogger.e(TAG, e.getMessage(), e);
                            ICallback.returnError(e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                            App.get().getDB().query("PRAGMA foreign_keys=ON;",null);

                            if (checkForeignKeys())
                            {
                                ICallback.complete();
                            }else{
                                ICallback.returnError(null);
                            }
                        }
                    });
        } catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
            ICallback.returnError(e.getMessage());
        }
    }
    @SuppressLint("CheckResult")
    private static void pull(Date since)
    {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        NetworkingUtils.getApiInstanceForJob()
                .pull(BuildConfig.KEYCHAIN, dt.format(since))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap((Function<PullResponseModel, Single<Date>>) pullResponseModel -> insertChangesSingle(pullResponseModel))
                .subscribeWith(new DisposableSingleObserver<Date>()
                {
                    @Override
                    public void onSuccess(Date dateEndLastSynchro)
                    {
                        /** after pull since, we set into smartphone parametr the dateof t */
                        PrismaGestionCo_NDF_smartphone_parameter smartphoneParameter = App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get();
                        smartphoneParameter.setDateEndLastSynchro(dateEndLastSynchro);
                        App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().insert(smartphoneParameter);
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        NdfLogger.e(TAG, e.getMessage(), e);
                        dispose();
                    }
                });
    }
    private static void push(List<ChangeModel> changeModels, PrismaGestionCo_NDF_synchronisation synchronization) throws IOException, RemoteException
    {
        /* First, push change models */
        Response<ResponseBody> response = NetworkingUtils.getApiInstanceForJob().push(BuildConfig.KEYCHAIN, changeModels).execute();
        if (response == null || !response.isSuccessful() || response.errorBody() != null)
        {
            /* if not success, set into database Synchronization Object state as ERROR  */
            synchronization.setSynchronizationsState(SynchronizationState.ERROR);
            App.get().getDB().prismagestionco_ndf_synchronisation_dao().insert(synchronization);

            if(response != null && response.errorBody() != null)
            {
                NdfLogger.e(TAG, response.errorBody().string());
            }
            else
            {
                NdfLogger.e(TAG, "pas de réponse du push");
            }
            throw new RemoteException(response);
        }
        else
            {
                /* if OK, set into database Synchronization Object as COMPLETE */
                synchronization.setSynchronizationsState(SynchronizationState.COMPLETE);
                App.get().getDB().prismagestionco_ndf_synchronisation_dao().insert(synchronization);

                /* get other synchronization Object */
                Pair<List<ChangeModel>, PrismaGestionCo_NDF_synchronisation> pairResult = getSynchronizationNotSend();
                if (pairResult != null)
                {
                    /* make recursive call until push all Synchronisation object
                     * this is possible because we update the synchronizationState of the first object got as COMPLETE
                     * So, we retrieve another Synchronization object according to DATE created for push */
                    push(pairResult.first, pairResult.second);
                }/**
                else
                    {
                         If no synchronization object, we pull with LastDateSynchro
                        PrismaGestionCo_NDF_smartphone_parameter smartphoneParameter = App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get();
                        pull(smartphoneParameter.getDateEndLastSynchro());
                    }*/
            }
    }
    private static boolean checkForeignKeys()
    {
        Cursor cursor = App.get().getDB().query("PRAGMA foreign_key_check;",null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            String tableName = cursor.getString(cursor.getColumnIndex("table"));
            String rowid = cursor.getString(cursor.getColumnIndex("rowid"));
            String parent = cursor.getString(cursor.getColumnIndex("parent"));

            String errorMessage = "check foreign keys failed, tableName : " + tableName + ", rowid : " + rowid + ", parent : " + parent;
            return false;
        }
        return true;
    }


    /** region private method */
    private static Observable<ProgressPullModel> insertChangesObservable(final PullResponseModel pullResponseModel)
    {
        return Observable
                .create(emitter ->
                {
                    int progress = 1; /** représente l'avancement actuel du chargement */

                    for (Map.Entry<String, List<Object>> entry : pullResponseModel.getChanges().entrySet()) {
                        String entityName = entry.getKey();

                        if (!emitter.isDisposed())
                        {
                            emitter.onNext(new ProgressPullModel(entityName, progress));
                        }

                        insertChange(entry);

                        progress++;
                    }

                    if (!emitter.isDisposed())
                    {
                        emitter.onComplete();
                    }
                });
    }
    private static Single<Date> insertChangesSingle(final PullResponseModel pullResponseModel)
    {
        return Single.create(emitter ->
        {
            try
            {
                /** get entry from pullResponseModel and insert change into database */
                for (Map.Entry<String, List<Object>> entry : pullResponseModel.getChanges().entrySet())
                {
                    insertChange(entry);
                }
                /** return the date of the last synchro */
                emitter.onSuccess(pullResponseModel.getLastUpdatedDate());
            }
            catch (Exception e)
            {
                NdfLogger.e(TAG, e.getMessage());
                emitter.onError(e);
            }
        });
    }
    private static void insertChange(Map.Entry<String, List<Object>> entry) throws Exception
    {
        /** insert entry into each table  | using reflection java*/
        String entityName = entry.getKey();
        List<Object> value = entry.getValue();

        Class<?> cls;

        entityName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1);
        cls = Class.forName(App.get().getPackageName() + "." + ConstanteDataBase.PACKAGE_MODEL + "." + ConstanteDataBase.PACKAGE_DATABASE + "." + ConstanteDataBase.PACKAGE_TABLES + "." + entityName);
        Object obj = cls.newInstance();
        Method method = cls.getDeclaredMethod(ConstanteDataBase.METHODE_DATABASE_CREATE_FROM_JSON, JSONArray.class);
        method.invoke(obj, new JSONArray(value));
    }


    /** region Helper */
    public static Object executerMethode(Object objet, String nomMethode, Object[] parametres) throws Exception
    {
        Object retour;
        Class[] typeParametres = null;
        if (parametres != null)
        {
            typeParametres = new Class[parametres.length];
            for (int i = 0; i < parametres.length; ++i)
            {
                typeParametres[i] = parametres[i].getClass();
            }
        }

        Method m = objet.getClass().getMethod(nomMethode, typeParametres);
        if (Modifier.isStatic(m.getModifiers())) {
            retour = m.invoke(null, parametres);
        } else {
            retour = m.invoke(objet, parametres);
        }
        return retour;
    }
    public static Pair<List<ChangeModel>, PrismaGestionCo_NDF_synchronisation> getSynchronizationNotSend()
    {
        List<ChangeModel> changeModels = new ArrayList<>();
        PrismaGestionCo_NDF_synchronisation synchronizationNotSend = null;
        try
        {
            synchronizationNotSend = App.get().getDB().prismagestionco_ndf_synchronisation_dao().getFirstNotSend();

            if (synchronizationNotSend != null)
            {
                Date dateCreatedOrUpdated;
                if (synchronizationNotSend.getDate_update() != null)
                {
                    dateCreatedOrUpdated = synchronizationNotSend.getDate_update();
                }
                else
                    {
                        dateCreatedOrUpdated = synchronizationNotSend.getDate_create();
                    }

                long diffInMillies = Math.abs(new Date().getTime() - dateCreatedOrUpdated.getTime());
                long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

                /* if not state sending, trying push */
                if (synchronizationNotSend.getSynchronizationsState() != SynchronizationState.SENDING || diffInMinutes > 5)
                {
                    List<PrismaGestionCo_NDF_synchronisation_line> synchronizationLines = App.get().getDB().prismagestionco_ndf_synchronisation_line_dao().getByIdSynchronization(synchronizationNotSend.getId());
                    if (synchronizationLines != null && !synchronizationLines.isEmpty())
                    {
                        for (PrismaGestionCo_NDF_synchronisation_line synchronizationLine :
                                synchronizationLines)
                        {
                            Database myDatabase = App.get().getDB();

                            String tableName = synchronizationLine.getTable();
                            int idLine = synchronizationLine.getId_line();

                            Object objDao = executerMethode(myDatabase, tableName.toLowerCase() + "_dao", null); // correspond aux définitions de classes dao abstraites
                            Object objEntitie = executerMethode(objDao, "getById", new Object[]{idLine});

                            /* We build a all changemodel with thses synchronizationLine properties and add it to change model list */
                            ChangeModel changeModel = new ChangeModel(String.valueOf(synchronizationLine.getId()), synchronizationLine.getTable(), objEntitie, synchronizationLine.getDate_create());
                            changeModels.add(changeModel);
                        }
                    }
                    /* set synchronisation object as sending || Push it || return it */
                    synchronizationNotSend.setSynchronizationsState(SynchronizationState.SENDING);
                    push(changeModels, synchronizationNotSend);
                    return new Pair<>(changeModels, synchronizationNotSend);
                }
            }

            /**
            else
                {
                    // else pull from date endlast synchro
                    PrismaGestionCo_NDF_smartphone_parameter smartphoneParameter = App.get().getDB().prismagestionco_ndf_smartphone_parameter_dao().get();
                    pull(smartphoneParameter.getDateEndLastSynchro());
                } */
        }
        catch (Exception e)
        {
            NdfLogger.e(TAG, e.getMessage());
            if (synchronizationNotSend != null)
            {
                synchronizationNotSend.setSynchronizationsState(SynchronizationState.ERROR);
                synchronizationNotSend.setDate_update(new Date());
            }
        }
        finally
        {
            if (synchronizationNotSend != null)
                App.get().getDB().prismagestionco_ndf_synchronisation_dao().insert(synchronizationNotSend);
        }
        return null;
    }
    public static void makeSynchronization(Map<String, List<GenericEntity>> entities)
    {
        // TODO: 14/01/2020 corriger les setId

        /** First, we create a synchronisation object */
        PrismaGestionCo_NDF_synchronisation synchronization = new PrismaGestionCo_NDF_synchronisation();
        synchronization.setSynchronizationsState(SynchronizationState.NOT_SEND);
        synchronization.setDate_create(new Date());

        /** insert it into database */
        long idSync = App.get().getDB().prismagestionco_ndf_synchronisation_dao().insert(synchronization);

        /** we check inside the dictionary of ENTITIES to make as Sync Object */
        for (Map.Entry<String, List<GenericEntity>> entry : entities.entrySet())
        {
            String entityName = entry.getKey();
            List<GenericEntity> genericEntities = entry.getValue();

            /** we build all Synchronisation_line according entities and set these Sync Id as Id of Synchronisation object created  */
            for (GenericEntity genericEntity : genericEntities)
            {
                PrismaGestionCo_NDF_synchronisation_line synchronizationLine = new PrismaGestionCo_NDF_synchronisation_line();
                synchronizationLine.setId(Helper.generateRandomInteger());
                synchronizationLine.setDate_create(new Date());
                synchronizationLine.setId_line(genericEntity.getId());
                synchronizationLine.setId_synchronization((int)idSync);
                synchronizationLine.setTable(entityName);
                App.get().getDB().prismagestionco_ndf_synchronisation_line_dao().insert(synchronizationLine);
            }
        }
        SynchronizationTask.getSynchronizationNotSend();
    }
}
