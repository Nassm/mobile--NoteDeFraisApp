package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_vehicule;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_vehiculeDao extends GenericDao<PrismaGestionCo_NDF_vehicule> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_vehicule")
    List<PrismaGestionCo_NDF_vehicule> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_vehicule ndfVehicule " +
            "WHERE ndfVehicule.id = :id")
    List<PrismaGestionCo_NDF_vehicule> getById(Integer id);

    @Query("SELECT vehicule.marque FROM PrismaGestionCo_NDF_vehicule vehicule WHERE vehicule.id = :id")
    List<String> getNameById(int id);

    @Query("SELECT id FROM PrismaGestionCo_NDF_vehicule ndfVehicule " +
            "WHERE ndfVehicule.marque = :marque")
    int getIdByName(String marque);
}
