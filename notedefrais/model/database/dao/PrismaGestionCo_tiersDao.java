package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.notedefrais.model.database.tables.PrismaGestionCo_personne;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_tiers;

import java.util.List;

@Dao
public interface PrismaGestionCo_tiersDao extends GenericDao<PrismaGestionCo_tiers> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_tiers")
    List<PrismaGestionCo_tiers> get();
}
