package com.example.notedefrais.model.database.tables;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.example.notedefrais.App;
import com.example.notedefrais.model.constante.ConstanteDataBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = PrismaGestionCo_projet.TABLENAME, foreignKeys = {
        @ForeignKey(entity = PrismaGestionCo_tiers.class, parentColumns = "id", childColumns = "idTiersClient")}
        , indices = {@Index("idTiersClient")}
        )
public class PrismaGestionCo_projet extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_projet";
    public static String getTABLENAME() { return TABLENAME; }

    /** region fields */
    private int idTiersClient;
    private String nom;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_projet()
    {
    }

    @Ignore
    public PrismaGestionCo_projet(int id, int idTiers, String nom)
    {
        super.setId(id);
        this.idTiersClient = idTiers;
        this.nom = nom;
    }

    public int getIdTiersClient() {
        return idTiersClient;
    }

    public void setIdTiersClient(int idTiersClient) {
        this.idTiersClient = idTiersClient;
    }

    /** region getter & setter */


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void createFromJson(JSONArray json) throws SQLException {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_projet>>() {
        }.getType();

        List<PrismaGestionCo_projet> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_projet_dao().insertAll(list);
    }

    public void createFromJson(JsonObject json) throws SQLException {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_projet>() {
        }.getType();

        PrismaGestionCo_projet entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_projet_dao().insert(entity);
    }
}
