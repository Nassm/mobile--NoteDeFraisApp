package com.example.notedefrais.model.database.dao;
import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_reglement_mode;
import java.util.List;

@Dao
public interface PrismaGestionCo_reglement_modeDao extends GenericDao<PrismaGestionCo_reglement_mode> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_reglement_mode")
    List<PrismaGestionCo_reglement_mode> get();

    @Query("SELECT * FROM PrismaGestionCo_reglement_mode mode WHERE mode.id = :id ")
    List<PrismaGestionCo_reglement_mode> getById(Integer id);

    @Query("SELECT mode.libelle FROM PrismaGestionCo_reglement_mode mode WHERE mode.id = :id")
    List<String> getLibelleById(int id);


    @Query("SELECT id FROM PrismaGestionCo_reglement_mode reg WHERE reg.libelle = :libelle")
    int getIdReglementByName(String libelle);
}
