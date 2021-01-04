package com.example.notedefrais;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.room.Room;
import com.example.notedefrais.model.database.Database;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;

import java.util.ArrayList;

public class App extends Application {

    private String TAG = App.class.getSimpleName();
    private static String BASE_URL;
    private static String userName;
    private static App INSTANCE;
    private Database database;
    private static int userId;
    public static int ndfId;
    public static final boolean DEBUG = false;
    private static ArrayList<PrismaGestionCo_projet> projs;


    @SuppressLint("CheckResult")
    @Override
    public void onCreate()
    {
        super.onCreate();

        /* instance of app */
        INSTANCE = this;

        /* autoruise les reqêtes sur le thread UI. non conseiller mais utilisé juste pour set l'url */
        database = Room.databaseBuilder(getApplicationContext(), Database.class, BuildConfig.DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    //region Getter/Setter
    public static App get() {
        return INSTANCE;
    }

    public Database getDB()
    {
        return database;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String getUserName()
    {
        return userName;
    }

    public static void setUserName(String userName)
    {
        App.userName = userName;
    }

    public static int getUserId()
    {
        return userId;
    }

    public static void setUserId(int userId)
    {
        App.userId = userId;
    }

    public static int getNdfId() { return ndfId; }

    public static void setNdfId(int ndfId) { App.ndfId = ndfId; }

    public static boolean isDEBUG() { return DEBUG; }

    public static ArrayList<PrismaGestionCo_projet> getProjs() {
        return projs;
    }

    public static void setProjs(ArrayList<PrismaGestionCo_projet> proj) {
        App.projs = proj;
    }
}
