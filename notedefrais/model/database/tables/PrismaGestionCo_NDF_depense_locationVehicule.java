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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(tableName = PrismaGestionCo_NDF_depense_locationVehicule.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF_depense.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneDepense"),
        }
        , indices = {@Index("idDepense")}
        )
public class PrismaGestionCo_NDF_depense_locationVehicule extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_locationVehicule";
    public static String getTABLENAME() { return TABLENAME; }

    @NonNull
    private String idSmartphoneDepense;
    private int idDepense;
    private String marqueVehicule;
    private String modeleVehicule;
    private String loueur;
    private Date dateDebut;
    private Date dateFin;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_locationVehicule() { }


    @Ignore
    public PrismaGestionCo_NDF_depense_locationVehicule(int id, int idDepense, String marqueVehicule, String modeleVehicule, String loueur, Date dateDebut, Date dateFin)
    {
        setId(id);
        this.idDepense = idDepense;
        this.marqueVehicule = marqueVehicule;
        this.modeleVehicule = modeleVehicule;
        this.loueur = loueur;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
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

    public String getMarqueVehicule()
    {
        return marqueVehicule;
    }

    public void setMarqueVehicule(String marqueVehicule)
    {
        this.marqueVehicule = marqueVehicule;
    }

    public String getModeleVehicule() {
        return modeleVehicule;
    }

    public void setModeleVehicule(String modeleVehicule)
    {
        this.modeleVehicule = modeleVehicule;
    }

    public String getLoueur()
    {
        return loueur;
    }

    public void setLoueur(String loueur)
    {
        this.loueur = loueur;
    }

    public Date getDateDebut()
    {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut)
    {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin()
    {
        return dateFin;
    }

    public void setDateFin(Date dateFin)
    {
        this.dateFin = dateFin;
    }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_locationVehicule>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_locationVehicule> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_locationVehicule>() {
        }.getType();

        PrismaGestionCo_NDF_depense_locationVehicule entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_locationvehicule_dao().insert(entity);
    }
}
