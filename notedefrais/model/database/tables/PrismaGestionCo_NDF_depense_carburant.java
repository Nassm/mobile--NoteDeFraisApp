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

@Entity(tableName = PrismaGestionCo_NDF_depense_carburant.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        @ForeignKey(entity = PrismaGestionCo_NDF_vehicule.class, parentColumns = "id", childColumns = "idNDFVehicule")}
        , indices = {@Index("idNDFVehicule"), @Index("idSmartphoneDepense")}
        )
public class PrismaGestionCo_NDF_depense_carburant extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_carburant";

    public static String getTABLENAME() { return TABLENAME; }

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private int idNDFVehicule;
    private Float litrage;
    private int compteur;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_carburant() {
    }

    @Ignore
    public PrismaGestionCo_NDF_depense_carburant(int id, int idDepense, int idNDFVehicule, Float litrage, int compteur)
    {
        setId(id);
        this.idDepense = idDepense;
        this.idNDFVehicule = idNDFVehicule;
        this.litrage = litrage;
        this.compteur = compteur;
    }

    @NonNull
    public String getIdSmartphoneDepense() { return idSmartphoneDepense; }

    public void setIdSmartphoneDepense(@NonNull String idSmartphoneDepense) { this.idSmartphoneDepense = idSmartphoneDepense; }

    public int getIdDepense() { return idDepense; }

    public void setIdDepense(int idDepense) { this.idDepense = idDepense; }

    public int getIdNDFVehicule() { return idNDFVehicule; }

    public void setIdNDFVehicule(int idNDFVehicule) { this.idNDFVehicule = idNDFVehicule; }

    public Float getLitrage() { return litrage; }

    public void setLitrage(Float litrage) { this.litrage = litrage; }

    public int getCompteur() { return compteur; }

    public void setCompteur(int compteur) { this.compteur = compteur; }


    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_carburant>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_carburant> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_carburant_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_carburant>() {
        }.getType();

        PrismaGestionCo_NDF_depense_carburant entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_carburant_dao().insert(entity);
    }
}
