package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense;

import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_Dao extends GenericDao<PrismaGestionCo_NDF_depense> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense")
    List<PrismaGestionCo_NDF_depense> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense depense " +
            "WHERE depense.id = :id")
    PrismaGestionCo_NDF_depense getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense depense WHERE depense.idSmartphoneNDF = :idSmartphoneNDF")
    List<PrismaGestionCo_NDF_depense> getByIdSmartphoneNDF(String idSmartphoneNDF);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense depense WHERE depense.idProjet = :idProject")
    List<PrismaGestionCo_NDF_depense> getByProject(int idProject);
}
