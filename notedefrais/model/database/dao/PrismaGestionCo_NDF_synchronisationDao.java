package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.pojo.SynchronizationState;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_synchronisation;

@Dao
public interface PrismaGestionCo_NDF_synchronisationDao extends GenericDao<PrismaGestionCo_NDF_synchronisation> {

    @Query("SELECT *"
            + " FROM PrismaGestionCo_NDF_synchronisation"
            + " WHERE (synchronizationsState  <> " + SynchronizationState.COMPLETE_CODE
            + " OR synchronizationsState is null)"
            + " ORDER BY DATE_CREATE"
            + " LIMIT 1"
    )
    PrismaGestionCo_NDF_synchronisation getFirstNotSend();

}
