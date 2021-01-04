package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_kilometrage;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_kilometrageDao extends GenericDao<PrismaGestionCo_NDF_depense_kilometrage> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_kilometrage")
    List<PrismaGestionCo_NDF_depense_kilometrage> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_kilometrage depenseKilometrage " +
            "WHERE depenseKilometrage.id = :id")
    PrismaGestionCo_NDF_depense_kilometrage getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_kilometrage depenseKilometrage " +
            "WHERE depenseKilometrage.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_kilometrage getByIdDepense(int idDepense);

}

