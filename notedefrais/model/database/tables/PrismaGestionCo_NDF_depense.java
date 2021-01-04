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

@Entity(tableName = PrismaGestionCo_NDF_depense.TABLENAME, foreignKeys = {
        //@ForeignKey(entity = PrismaGestionCo_NDF.class, parentColumns = "idSmartphone", childColumns = "idSmartphoneNDF"),
        @ForeignKey(entity = PrismaGestionCo_NDF_depense_categorie.class, parentColumns = "id", childColumns = "idCategorie"),
        @ForeignKey(entity = PrismaGestionCo_tiers.class, parentColumns = "id", childColumns = "idTiersClient"),
        @ForeignKey(entity = PrismaGestionCo_projet.class, parentColumns = "id", childColumns = "idProjet"),
        @ForeignKey(entity = PrismaGestionCo_reglement_mode.class, parentColumns = "id", childColumns = "idReglementMode")}
        , indices = {@Index("idCategorie"), @Index("idTiersClient"), @Index("idProjet"),  @Index("idReglementMode")}
        )
public class PrismaGestionCo_NDF_depense extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF_depense";
    public static String getTABLENAME() { return TABLENAME; }

    /** region fields */
    @NonNull
    private String idSmartphoneNDF;
    private int idCategorie;
    private int idTiersClient;
    private int idProjet;
    private int idReglementMode;

    private String type;
    private Date date;
    private String description;
    private Float montantTTC;
    private Float montantHT;
    private Float montantTVA;
    private Float montantTaxe;

    private String image1;
    private String image2;
    private String image3;
    private int isMultiTaux;


    /** region constructor */
    public PrismaGestionCo_NDF_depense()
    {
    }

    @Ignore
    public PrismaGestionCo_NDF_depense(int id, String type, Date date, String description, Float montantTTC, Float montantHT, Float montantTVA, Float montantTaxe)
    {
        super.setId(id);
        this.type = type;
        this.date = date;
        this.description = description;
        this.montantTTC = montantTTC;
        this.montantHT = montantHT;
        this.montantTVA = montantTVA;
        this.montantTaxe = montantTaxe;
    }

    /** region getter & setter */

    @NonNull
    public String getIdSmartphoneNDF() { return idSmartphoneNDF; }

    public void setIdSmartphoneNDF(@NonNull String idSmartphoneNDF) { this.idSmartphoneNDF = idSmartphoneNDF; }

    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int idCategorie) {
        this.idCategorie = idCategorie;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getMontantTTC() {
        return montantTTC;
    }

    public void setMontantTTC(Float montantTTC) {
        this.montantTTC = montantTTC;
    }

    public Float getMontantHT() {
        return montantHT;
    }

    public void setMontantHT(Float montantHT) {
        this.montantHT = montantHT;
    }

    public Float getMontantTVA() {
        return montantTVA;
    }

    public void setMontantTVA(Float montantTVA) {
        this.montantTVA = montantTVA;
    }

    public Float getMontantTaxe() {
        return montantTaxe;
    }

    public void setMontantTaxe(Float montantTaxe) {
        this.montantTaxe = montantTaxe;
    }

    public int getIdReglementMode() {
        return idReglementMode;
    }

    public void setIdReglementMode(int idReglementMode) {
        this.idReglementMode = idReglementMode;
    }

    public int getIdTiersClient() {
        return idTiersClient;
    }

    public void setIdTiersClient(int idTiersClient) {
        this.idTiersClient = idTiersClient;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public int getIsMultiTaux() {
        return isMultiTaux;
    }

    public void setIsMultiTaux(int isMultiTaux) {
        this.isMultiTaux = isMultiTaux;
    }

    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_depense>>() {
        }.getType();

        List<PrismaGestionCo_NDF_depense> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_dao().insertAll(list);
    }
    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_depense>() {
        }.getType();

        PrismaGestionCo_NDF_depense entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_depense_dao().insert(entity);
    }
}
