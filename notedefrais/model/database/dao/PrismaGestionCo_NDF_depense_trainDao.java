package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_train;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_trainDao extends GenericDao<PrismaGestionCo_NDF_depense_train> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_train")
    List<PrismaGestionCo_NDF_depense_train> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_train depenseTrain " +
            "WHERE depenseTrain.id = :id")
    PrismaGestionCo_NDF_depense_train getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_train depenseTrain " +
            "WHERE depenseTrain.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_train getByIdDepense(int idDepense);
}
