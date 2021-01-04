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

@Entity(tableName = PrismaGestionCo_reglement_mode.TABLENAME)
public class PrismaGestionCo_reglement_mode extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_reglement_mode";

    public static String getTABLENAME() { return TABLENAME; }

    private String libelle;
    private int nonExceptionnel;
    private String journal;
    private int creationReglement;
    private String compte;
    private Float montantFraisImpaye;
    private int isAutoriseTraite;
    private String typeModeReglement;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_reglement_mode() { }


    @Ignore
    public PrismaGestionCo_reglement_mode(int id, String libelle, int nonExceptionnel, String journal, int creationReglement, String compte, Float montantFraisImpaye, int isAutoriseTraite, String typeModeReglement) {
        setId(id);
        this.libelle = libelle;
        this.nonExceptionnel = nonExceptionnel;
        this.journal = journal;
        this.creationReglement = creationReglement;
        this.compte = compte;
        this.montantFraisImpaye = montantFraisImpaye;
        this.isAutoriseTraite = isAutoriseTraite;
        this.typeModeReglement = typeModeReglement;
    }


    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getNonExceptionnel() {
        return nonExceptionnel;
    }

    public void setNonExceptionnel(int nonExceptionnel) {
        this.nonExceptionnel = nonExceptionnel;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public int getCreationReglement() {
        return creationReglement;
    }

    public void setCreationReglement(int creationReglement) {
        this.creationReglement = creationReglement;
    }

    public int getIsAutoriseTraite() {
        return isAutoriseTraite;
    }

    public void setIsAutoriseTraite(int isAutoriseTraite) {
        this.isAutoriseTraite = isAutoriseTraite;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public Float getMontantFraisImpaye() {
        return montantFraisImpaye;
    }

    public void setMontantFraisImpaye(Float montantFraisImpaye) {
        this.montantFraisImpaye = montantFraisImpaye;
    }

    public String getTypeModeReglement() {
        return typeModeReglement;
    }

    public void setTypeModeReglement(String typeModeReglement) {
        this.typeModeReglement = typeModeReglement;
    }


    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_reglement_mode>>() {
        }.getType();

        List<PrismaGestionCo_reglement_mode> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismaGestionCo_reglement_mode_dao().insertAll(list);
    }

    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_reglement_mode>() {
        }.getType();

        PrismaGestionCo_reglement_mode entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismaGestionCo_reglement_mode_dao().insert(entity);
    }
}
