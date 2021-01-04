package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_carburant;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_carburantDao extends GenericDao<PrismaGestionCo_NDF_depense_carburant> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_carburant")
    List<PrismaGestionCo_NDF_depense_carburant> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_carburant depenseCarburant " +
            "WHERE depenseCarburant.id = :id")
    PrismaGestionCo_NDF_depense_carburant getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_carburant depenseCarburant " +
            "WHERE depenseCarburant.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_carburant getByIdDepense(int idDepense);
}
