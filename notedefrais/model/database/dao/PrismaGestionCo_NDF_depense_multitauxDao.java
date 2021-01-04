package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_multitaux;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_multitauxDao extends GenericDao<PrismaGestionCo_NDF_depense_multitaux> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_multitaux")
    List<PrismaGestionCo_NDF_depense_multitaux> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_multitaux depenseMultitaux " +
            "WHERE depenseMultitaux.id = :id")
    PrismaGestionCo_NDF_depense_multitaux getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_multitaux depenseMultitaux " +
            "WHERE depenseMultitaux.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_multitaux getByIdDepense(int idDepense);
}
