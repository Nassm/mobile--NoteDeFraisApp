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


@Entity(tableName = PrismaGestionCo_NDF_depense_categorie.TABLENAME)
public class PrismaGestionCo_NDF_depense_categorie extends GenericEntity {

    public static final String RESTAURATION = "Restauration";
    public static final String INTERVENTION = "Intervention";
    public static final String CARBURANT = "Carburant";
    public static final String KILOMETRAGE = "Kilometrage";
    public static final String AVION = "Avion";
    public static final String HOTEL = "Hotel";
    public static final String TRAIN = "Train";
    public static final String MULTITAUX = "Multitaux";
    public static final String LOCATIONVEHICULE = "LocationVehicule";

    /** ------ new depense categorie ------- */
    public static final String AVANCE = "Avance";
    public static final String DIVERS = "Divers";
    public static final String INTERNET = "Internet";
    public static final String PARKING = "Parking";
    public static final String PEAGE = "Peage";
    public static final String TAXI = "Taxi";
    public static final String TELEPHONE = "Telephone";
    public static final String TRANSPORTENCOMMUN = "TransportEnCommun";



    public static final String TABLENAME = "PrismaGestionCo_NDF_depense_categorie";
    public static String getTABLENAME() { return TABLENAME; }


    /** region fields */
    private String nom;
    private int activee;
    private int tauxIntermediaire;
    private int tauxStandard;
    private int tauxReduit;
    private int tauxSuperReduit;
    private String TVARecuperable;
    private int completeImputationComptable;
    private String colorPrimarySmartphone;
    private String colorSecondarySmartphone;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_depense_categorie()
    {
    }



    /** region getter & setter */

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getActivee() {
        return activee;
    }

    public void setActivee(int activee) {
        this.activee = activee;
    }

    public int getTauxIntermediaire() {
        return tauxIntermediaire;
    }

    public void setTauxIntermediaire(int tauxIntermediaire) { this.tauxIntermediaire = tauxIntermediaire; }

    public int getTauxStandard() {
        return tauxStandard;
    }

    public void setTauxStandard(int tauxStandard) {
        this.tauxStandard = tauxStandard;
    }

    public int getTauxReduit() {
        return tauxReduit;
    }

    public void setTauxReduit(int tauxReduit) {
        this.tauxReduit = tauxReduit;
    }

    public int getTauxSuperReduit() {
        return tauxSuperReduit;
    }

    public void setTauxSuperReduit(int tauxSuperReduit) {
        this.tauxSuperReduit = tauxSuperReduit;
    }

    public String getTVARecuperable() {
        return TVARecuperable;
    }

    public void setTVARecuperable(String TVARecuperable) {
        this.TVARecuperable = TVARecuperable;
    }

    public int getCompleteImputationComptable() {
        return completeImputationComptable;
    }

    public void setCompleteImputationComptable(int completeImputationComptable) { this.completeImputationComptable = completeImputationComptable; }

    public String getColorPrimarySmartphone() { return colorPrimarySmartphone; }

    public void setColorPrimarySmartphone(String colorPrimarySmartphone) { this.colorPrimarySmartphone = colorPrimarySmartphone; }

    public String getColorSecondarySmartphone() { return colorSecondarySmartphone; }

    public void setColorSecondarySmartphone(String colorSecondarySmartphone) { this.colorSecondarySmartphone = colorSecondarySmartphone; }



    /** region JSON */

    public void createFromJson(JSONArray json) throws SQLException {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense_categorie>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense_categorie> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_categorie_dao().insertAll(list);
    }

    public void createFromJson(JsonObject json) throws SQLException {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense_categorie>() {
        }.getType();

        PrismaGestionCo_NDF_depense_categorie entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_categorie_dao().insert(entity);
    }
}
