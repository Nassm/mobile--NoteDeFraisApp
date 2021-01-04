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

@Entity(tableName = PrismaGestionCo_NDF_depense_multitaux.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        }
        , indices = {@Index("idDepense")}
        )
public class PrismaGestionCo_NDF_depense_multitaux extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_multitaux";
    public static String getTABLENAME() { return TABLENAME; }

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private Float montantTTC;
    private Float montantHT;
    private Float montantTVA;
    private Float montantTaxe;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_multitaux() { }


    @Ignore
    public PrismaGestionCo_NDF_depense_multitaux(int id, int idDepense, Float montantTTC, Float montantHT, Float montantTVA, Float montantTaxe)
    {
        setId(id);
        this.idDepense = idDepense;
        this.montantTTC = montantTTC;
        this.montantHT = montantHT;
        this.montantTVA = montantTVA;
        this.montantTaxe = montantTaxe;
    }

    @NonNull
    public String getIdSmartphoneDepense() { return idSmartphoneDepense; }

    public void setIdSmartphoneDepense(@NonNull String idSmartphoneDepense) { this.idSmartphoneDepense = idSmartphoneDepense; }

    public int getIdDepense() { return idDepense; }

    public void setIdDepense(int idDepense) { this.idDepense = idDepense; }

    public Float getMontantTTC() { return montantTTC; }

    public void setMontantTTC(Float montantTTC) { this.montantTTC = montantTTC; }

    public Float getMontantHT() { return montantHT; }

    public void setMontantHT(Float montantHT) { this.montantHT = montantHT; }

    public Float getMontantTVA() { return montantTVA; }

    public void setMontantTVA(Float montantTVA) { this.montantTVA = montantTVA; }

    public Float getMontantTaxe() { return montantTaxe; }

    public void setMontantTaxe(Float montantTaxe) { this.montantTaxe = montantTaxe; }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_multitaux>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_multitaux> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_multitaux_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_multitaux>() {
        }.getType();

        PrismaGestionCo_NDF_depense_multitaux entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_multitaux_dao().insert(entity);
    }
}
