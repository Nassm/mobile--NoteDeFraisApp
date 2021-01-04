package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;

import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_Dao extends GenericDao<PrismaGestionCo_NDF> {

    @Query("SELECT * FROM PrismaGestionCo_NDF " +
            "WHERE etat is not null AND etat <> 'Comptabilisee'")
    List<PrismaGestionCo_NDF> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF ndf " +
            "WHERE ndf.id = :id")
    PrismaGestionCo_NDF getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF ndf " +
            "WHERE ndf.idSmartphone = :idSmartphone")
    PrismaGestionCo_NDF getByIdSmartphone(String idSmartphone);

    @Query("SELECT id FROM PrismaGestionCo_NDF ndf WHERE ndf.nom = :nom")
    int getIdNdfByName(String nom);

    @Query("SELECT DISTINCT " +
            "ndf.id, ndf.idPersonne, ndf.numeroNote, ndf.nom, ndf.complement, ndf.etat" +
            ", ndf.idPersonneApprobation, ndf.dateApprobation, ndf.commentaireRefus, ndf.idEcriture, ndf.idSmartphone " +
            "FROM PrismaGestionCo_NDF ndf " +
            "JOIN PrismaGestionCo_NDF_depense dep " +
            "ON ndf.idSmartphone = dep.idSmartphoneNDF  " +
            "WHERE dep.idProjet = :idProject")
    List<PrismaGestionCo_NDF> getByIdProject(int idProject);

    @Query("SELECT * FROM PrismaGestionCo_NDF " +
            "WHERE etat is not null AND etat = 'Comptabilisee'")
    List<PrismaGestionCo_NDF> getByApproval();

    @Query("UPDATE PrismaGestionCo_NDF SET etat = 'Soumis pour approbation' WHERE idSmartphone = :idSmartphoneNdf ")
    void updateSubmitStateById(String idSmartphoneNdf);
}
