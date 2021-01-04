package com.example.notedefrais.model.database.tables;

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

@Entity(tableName = PrismaGestionCo_NDF_vehicule.TABLENAME) //
public class PrismaGestionCo_NDF_vehicule extends GenericEntity {

    /** fk removed !!
     *foreignKeys = {@ForeignKey(entity = PrismaGestionCo_personne.class, parentColumns = "id", childColumns = "idPersonne"),}, indices = {@Index("idPersonne")}
     */
    public static final String TABLENAME = "PrismaGestionCo_NDF_vehicule";

    public static String getTABLENAME() { return TABLENAME; }

    private int idPersonne;
    private String marque;
    private String modele;
    private String categorie;
    private String carburant;
    private String immatriculation;
    private String puissanceChevaux;
    private int classification;
    private Float prixAuKm;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_vehicule() { }

    @Ignore
    public PrismaGestionCo_NDF_vehicule(int id, int idPersonne, String marque, String modele, String categorie, String carburant, String immatriculation, String puissanceChevaux, int classification, Float prixAuKm)
    {
        setId(id);
        this.idPersonne = idPersonne;
        this.marque = marque;
        this.modele = modele;
        this.categorie = categorie;
        this.carburant = carburant;
        this.immatriculation = immatriculation;
        this.puissanceChevaux = puissanceChevaux;
        this.classification = classification;
        this.prixAuKm = prixAuKm;
    }

    public int getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne = idPersonne;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCarburant() {
        return carburant;
    }

    public void setCarburant(String carburant) {
        this.carburant = carburant;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getPuissanceChevaux() {
        return puissanceChevaux;
    }

    public void setPuissanceChevaux(String puissanceChevaux) {
        this.puissanceChevaux = puissanceChevaux;
    }

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public Float getPrixAuKm() {
        return prixAuKm;
    }

    public void setPrixAuKm(Float prixAuKm) {
        this.prixAuKm = prixAuKm;
    }


    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_vehicule>>() {
        }.getType();

        List<PrismaGestionCo_NDF_vehicule> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_vehicule_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_vehicule>() {
        }.getType();

        PrismaGestionCo_NDF_vehicule entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_vehicule_dao().insert(entity);
    }
}
