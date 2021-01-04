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

@Entity(tableName = PrismaGestionCo_NDF_depense_restauration.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        }
        , indices = {@Index("idDepense")}
        )
public class PrismaGestionCo_NDF_depense_restauration extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_restauration";

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private String invites;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_restauration() { }


    @Ignore
    public PrismaGestionCo_NDF_depense_restauration(int id, int idDepense, String invites)
    {
        setId(id);
        this.idDepense = idDepense;
        this.invites = invites;
    }

    @NonNull
    public String getIdSmartphoneDepense() { return idSmartphoneDepense; }

    public void setIdSmartphoneDepense(@NonNull String idSmartphoneDepense) { this.idSmartphoneDepense = idSmartphoneDepense; }

    public static String getTABLENAME() {
        return TABLENAME;
    }

    public int getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(int idDepense) {
        this.idDepense = idDepense;
    }

    public String getInvites() {
        return invites;
    }

    public void setInvites(String invites) {
        this.invites = invites;
    }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_restauration>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_restauration> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_restauration_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_restauration>() {
        }.getType();

        PrismaGestionCo_NDF_depense_restauration entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_restauration_dao().insert(entity);
    }
}
