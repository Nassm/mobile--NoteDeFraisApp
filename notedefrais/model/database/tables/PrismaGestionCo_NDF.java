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

@Entity(tableName = PrismaGestionCo_NDF.TABLENAME, foreignKeys = {
        @ForeignKey(entity = PrismaGestionCo_personne.class, parentColumns = "id", childColumns = "idPersonne")}
        , indices = {@Index("idPersonne")}
        )
public class PrismaGestionCo_NDF extends GenericEntity {

    public static final String TABLENAME = "PrismaGestionCo_NDF";
    public static String getTABLENAME() { return TABLENAME; }


    /** region fields */
    @NonNull
    private int idPersonne;
    @NonNull
    private int numeroNote;
    @NonNull
    private String nom;
    @NonNull
    private String etat;
    private int idPersonneApprobation;
    private int idEcriture;
    private String complement;
    private String commentaireRefus;
    private Date dateApprobation;

    @Ignore
    private String depenseCount;
    @Ignore
    private List<PrismaGestionCo_NDF_depense> depenses;
    @Ignore
    private Float amount;


    /** region constructor */
    public PrismaGestionCo_NDF()
    {
    }

    @Ignore
    public PrismaGestionCo_NDF(int id, int idPersonne, int numeroNote, String nom, String complement, String etat)
    {
        super.setId(id);
        this.idPersonne = idPersonne;
        this.numeroNote = numeroNote;
        this.nom = nom;
        this.complement = complement;
        this.etat = etat;
    }

    /** region getter & setter */
    public int getIdPersonne()
    {
        return idPersonne;
    }

    public void setIdPersonne(int idPersonne)
    {
        this.idPersonne = idPersonne;
    }

    public int getNumeroNote() {
        return numeroNote;
    }

    public void setNumeroNote(int numeroNote) {
        this.numeroNote = numeroNote;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getIdPersonneApprobation() {
        return idPersonneApprobation;
    }

    public void setIdPersonneApprobation(int idPersonneApprobation)
    {
        this.idPersonneApprobation = idPersonneApprobation;
    }

    public Date getDateApprobation() {
        return dateApprobation;
    }

    public void setDateApprobation(Date dateApprobation) {
        this.dateApprobation = dateApprobation;
    }

    public String getCommentaireRefus() {
        return commentaireRefus;
    }

    public void setCommentaireRefus(String commentaireRefus)
    {
        this.commentaireRefus = commentaireRefus;
    }
    public int getIdEcriture() {
        return idEcriture;
    }

    public void setIdEcriture(int idEcriture) {
        this.idEcriture = idEcriture;
    }

    @Ignore
    public String getDepenseCount() { return depenseCount; }
    @Ignore
    public void setDepenseCount(String depenseCount) { this.depenseCount = depenseCount; }
    @Ignore
    public List<PrismaGestionCo_NDF_depense> getDepenses()
    {
        return depenses;
    }
    @Ignore
    public void setDepenses(List<PrismaGestionCo_NDF_depense> depenses) { this.depenses = depenses; }
    @Ignore
    public Float getAmount() { return amount; }
    @Ignore
    public void setAmount(Float amount) { this.amount = amount; }

    /** Region Json */
    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF>>() {
        }.getType();

        List<PrismaGestionCo_NDF> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_dao().insertAll(list);
    }

    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF>() {
        }.getType();

        PrismaGestionCo_NDF entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_dao().insert(entity);
    }
}
