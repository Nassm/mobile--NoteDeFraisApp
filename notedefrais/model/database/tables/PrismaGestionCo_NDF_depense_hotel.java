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

@Entity(tableName = PrismaGestionCo_NDF_depense_hotel.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        }
        , indices = {@Index("idSmartphoneDepense")}
)
public class PrismaGestionCo_NDF_depense_hotel extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_hotel";
    public static String getTABLENAME() { return TABLENAME; }

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private int nbNuits;



    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_hotel() {
    }

    @Ignore
    public PrismaGestionCo_NDF_depense_hotel(int id, int idDepense, int nbNuits)
    {
        setId(id);
        this.idDepense = idDepense;
        this.nbNuits = nbNuits;
    }

    @NonNull
    public String getIdSmartphoneDepense() { return idSmartphoneDepense; }

    public void setIdSmartphoneDepense(@NonNull String idSmartphoneDepense) { this.idSmartphoneDepense = idSmartphoneDepense; }

    public int getIdDepense()
    {
        return idDepense;
    }

    public void setIdDepense(int idDepense)
    {
        this.idDepense = idDepense;
    }

    public int getNbNuits()
    {
        return nbNuits;
    }

    public void setNbNuits(int nbNuits)
    {
        this.nbNuits = nbNuits;
    }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_hotel>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_hotel> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_hotel_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_hotel>() {
        }.getType();

        PrismaGestionCo_NDF_depense_hotel entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_hotel_dao().insert(entity);
    }
}
