package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_depense_hotel;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_projet;
import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_depense_hotelDao extends GenericDao<PrismaGestionCo_NDF_depense_hotel> {

    // Query
    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_hotel")
    List<PrismaGestionCo_NDF_depense_hotel> get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_hotel depenseHotel " +
            "WHERE depenseHotel.id = :id")
    PrismaGestionCo_NDF_depense_hotel getById(Integer id);

    @Query("SELECT * FROM PrismaGestionCo_NDF_depense_hotel depenseHotel " +
            "WHERE depenseHotel.idDepense = :idDepense")
    PrismaGestionCo_NDF_depense_hotel getByIdDepense(int idDepense);
}
