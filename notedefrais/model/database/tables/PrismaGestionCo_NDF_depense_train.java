package com.example.notedefrais.model.database.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.example.notedefrais.App;
import com.example.notedefrais.model.constante.ConstanteDataBase;
import com.example.notedefrais.model.utils.ImageSourceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(tableName = PrismaGestionCo_NDF_depense_train.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        }
        , indices = {@Index("idDepense")}
        )
public class PrismaGestionCo_NDF_depense_train extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_train";

    public static String getTABLENAME() { return TABLENAME; }

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private String numTrain;
    private Date dateDepart;
    private Date dateArrivee;
    private String compagnie;
    private String trajet;

    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_train() { }


    @NonNull
    public String getIdSmartphoneDepense() { return idSmartphoneDepense; }

    public void setIdSmartphoneDepense(@NonNull String idSmartphoneDepense) { this.idSmartphoneDepense = idSmartphoneDepense; }

    public int getIdDepense() { return idDepense; }

    public void setIdDepense(int idDepense) { this.idDepense = idDepense; }

    public String getNumTrain() { return numTrain; }

    public void setNumTrain(String numTrain) { this.numTrain = numTrain; }

    public Date getDateDepart() { return dateDepart; }

    public void setDateDepart(Date dateDepart) { this.dateDepart = dateDepart; }

    public Date getDateArrivee() { return dateArrivee; }

    public void setDateArrivee(Date dateArrivee) { this.dateArrivee = dateArrivee; }

    public String getCompagnie() { return compagnie; }

    public void setCompagnie(String compagnie) { this.compagnie = compagnie; }

    public String getTrajet() { return trajet; }

    public void setTrajet(String trajet) { this.trajet = trajet; }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_train>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_train> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_train_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_train>() {
        }.getType();

        PrismaGestionCo_NDF_depense_train entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_train_dao().insert(entity);
    }
}
