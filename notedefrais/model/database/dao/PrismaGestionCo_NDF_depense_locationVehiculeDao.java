package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_locationVehicule;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;

import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_locationVehiculeDao extends GenericDao<PrismaGestionCo_NDF_depense_locationVehicule> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_locationVehicule")
    List<PrismaGestionCo_NDF_depense_locationVehicule> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_locationVehicule depenseLocationVehicule " +
            "WHERE depenseLocationVehicule.id = :id")
    PrismaGestionCo_NDF_depense_locationVehicule getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_locationVehicule depenseLocationVehicule " +
            "WHERE depenseLocationVehicule.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_locationVehicule getByIdDepense(int idDepense);
}
