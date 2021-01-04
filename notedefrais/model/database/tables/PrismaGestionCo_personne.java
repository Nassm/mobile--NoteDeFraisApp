package com.example.notedefrais.model.database.tables;

import androidx.room.Entity;

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

@Entity(tableName = PrismaGestionCo_personne.TABLENAME)
public class PrismaGestionCo_personne extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_personne";
    public static String getTABLENAME() { return TABLENAME; }

    private String nom;
    private String prenom;
    private int isActif;
    private int isAdministrateur;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_personne() { }



    /** region getter & setter */
    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }

    public void setPrenom(String prenom) { this.prenom = prenom; }

    public int getIsActif() {
        return isActif;
    }

    public void setIsActif(int isActif) {
        this.isActif = isActif;
    }

    public int getIsAdministrateur() {
        return isAdministrateur;
    }

    public void setIsAdministrateur(int isAdministrateur) {
        this.isAdministrateur = isAdministrateur;
    }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_personne>>() {
        }.getType();

        List<PrismaGestionCo_personne> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_personne_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_personne>() {
        }.getType();

        PrismaGestionCo_personne entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_personne_dao().insert(entity);
    }
}
