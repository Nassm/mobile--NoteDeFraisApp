package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_synchronisation_line;

import java.util.List;

@Dao
public interface PrismaGestionCo_NDF_synchronisation_lineDao extends GenericDao<PrismaGestionCo_NDF_synchronisation_line>
{
    @Query("SELECT * FROM PrismaGestionCo_NDF_synchronisation_line lines WHERE lines.id_synchronization = :id ORDER BY DATE_CREATE")
    List<PrismaGestionCo_NDF_synchronisation_line> getByIdSynchronization(int id);
}
