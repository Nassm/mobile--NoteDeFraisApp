package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_intervention;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;

import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_interventionDao extends GenericDao<PrismaGestionCo_NDF_depense_intervention> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_intervention")
    List<PrismaGestionCo_NDF_depense_intervention> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_intervention depenseIntervention " +
            "WHERE depenseIntervention.id = :id")
    PrismaGestionCo_NDF_depense_intervention getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_intervention depenseIntervention " +
            "WHERE depenseIntervention.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_intervention getByIdDepense(int idDepense);
}
