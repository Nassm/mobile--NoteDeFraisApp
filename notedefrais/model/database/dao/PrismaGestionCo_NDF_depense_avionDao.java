package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_avion;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_avionDao extends GenericDao<PrismaGestionCo_NDF_depense_avion> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_avion")
    List<PrismaGestionCo_NDF_depense_avion> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_avion depenseAvion " +
            "WHERE depenseAvion.id = :id")
    PrismaGestionCo_NDF_depense_avion getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_avion depenseAvion " +
            "WHERE depenseAvion.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_avion getByIdDepense(int idDepense);
}
