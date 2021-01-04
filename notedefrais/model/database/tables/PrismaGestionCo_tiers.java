package com.example.notedefrais.model.database.tables;

import androidx.room.Entity;
import androidx.room.Ignore;

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

@Entity(tableName = PrismaGestionCo_tiers.TABLENAME)
public class PrismaGestionCo_tiers extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_tiers";
    public static String getTABLENAME() { return TABLENAME; }

    private String code;
    private String nom;
    private String raisonSociale;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_tiers()
    { }

    @Ignore
    public PrismaGestionCo_tiers(int id, String code, String nom, String raisonSociale)
    {
        setId(id);
        this.code = code;
        this.nom = nom;
        this.raisonSociale = raisonSociale;
    }


    /** region getter & setter */
    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getRaisonSociale() { return raisonSociale; }

    public void setRaisonSociale(String raisonSociale) { this.raisonSociale = raisonSociale; }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_tiers>>() {
        }.getType();

        List<PrismaGestionCo_tiers> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_tiers_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_tiers>() {
        }.getType();

        PrismaGestionCo_tiers entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_tiers_dao().insert(entity);
    }
}


