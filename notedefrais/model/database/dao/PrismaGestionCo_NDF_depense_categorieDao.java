package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_categorie;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_categorieDao extends GenericDao<PrismaGestionCo_NDF_depense_categorie>
{
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_categorie")
    List<PrismaGestionCo_NDF_depense_categorie> get();


    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_categorie categorie " +
            "WHERE categorie.activee = 1")
    List<PrismaGestionCo_NDF_depense_categorie> getByActivee();
}
