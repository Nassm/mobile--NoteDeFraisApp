package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_restauration;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;

import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_restaurationDao extends GenericDao<PrismaGestionCo_NDF_depense_restauration> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_restauration")
    List<PrismaGestionCo_NDF_depense_restauration> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_restauration depenseRestauration " +
            "WHERE depenseRestauration.id = :id")
    PrismaGestionCo_NDF_depense_restauration getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_restauration depenseRestauration " +
            "WHERE depenseRestauration.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_restauration getByIdDepense(int idDepense);
}
