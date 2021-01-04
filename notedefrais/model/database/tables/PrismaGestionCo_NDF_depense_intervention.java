package com.example.notedefrais.model.database.tables;

import androidx.annotation.NonNull;
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
import java.util.UUID;

@Entity(tableName = PrismaGestionCo_NDF_depense_intervention.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        }
        , indices = {@Index("idDepense")}
        )
public class PrismaGestionCo_NDF_depense_intervention extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_intervention";
    public static String getTABLENAME() { return TABLENAME; }

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private int dureeIntervention;
    private Float prixHoraire;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_intervention() {
    }


    @Ignore
    public PrismaGestionCo_NDF_depense_intervention(int id, int idDepense, int dureeIntervention, Float prixHoraire)
    {
        setId(id);
        this.idDepense = idDepense;
        this.dureeIntervention = dureeIntervention;
        this.prixHoraire = prixHoraire;
    }

    @NonNull
    public String getIdSmartphoneDepense() { return idSmartphoneDepense; }

    public void setIdSmartphoneDepense(@NonNull String idSmartphoneDepense) { this.idSmartphoneDepense = idSmartphoneDepense; }

    public int getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(int idDepense) {
        this.idDepense = idDepense;
    }

    public int getDureeIntervention() {
        return dureeIntervention;
    }

    public void setDureeIntervention(int dureeIntervention) {
        this.dureeIntervention = dureeIntervention;
    }

    public Float getPrixHoraire() {
        return prixHoraire;
    }

    public void setPrixHoraire(Float prixHoraire) {
        this.prixHoraire = prixHoraire;
    }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_intervention>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_intervention> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_intervention_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_intervention>() {
        }.getType();

        PrismaGestionCo_NDF_depense_intervention entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_intervention_dao().insert(entity);
    }
}
