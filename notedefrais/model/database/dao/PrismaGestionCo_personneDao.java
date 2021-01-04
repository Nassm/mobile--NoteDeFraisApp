package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_personne;
import java.util.List;

@Dao
public interface PrismaGestionCo_personneDao extends GenericDao<PrismaGestionCo_personne> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_personne")
    List<PrismaGestionCo_personne> get();
}
