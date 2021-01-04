package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;

import java.util.List;

@Dao
public interface PrismaGestionCo_projetDao extends GenericDao<PrismaGestionCo_projet> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_projet")
    List<PrismaGestionCo_projet> get();

    @Query("SELECT * FROM PrismaGestionCo_projet proj WHERE proj.id = :id")
    List<PrismaGestionCo_projet> getById(Integer id);

    @Query("SELECT proj.nom FROM PrismaGestionCo_projet proj WHERE proj.id = :id")
    List<String> getNameById(int id);

    @Query("SELECT id FROM PrismaGestionCo_projet proj WHERE proj.nom = :projectName")
    int getIdProjectByName(String projectName);

    @Query("SELECT idTiersClient FROM PrismaGestionCo_projet proj WHERE proj.id = :id")
    int getIdTiersClientByIdProject(int id);
}
