package com.example.notedefrais.model.database.dao;

import androidx.room.Dao;
import androidx.room.Query;
import com.example.notedefrais.model.database.tables.PrismaGestionCo_NDF_smartphone_parameter;

import java.util.Optional;
import io.reactivex.Single;

@Dao
public interface PrismaGestionCo_NDF_smartphone_parameterDao extends GenericDao<PrismaGestionCo_NDF_smartphone_parameter> {

    @Query("SELECT * FROM PrismaGestionCo_NDF_smartphone_parameter")
    PrismaGestionCo_NDF_smartphone_parameter get();

    @Query("SELECT * FROM PrismaGestionCo_NDF_smartphone_parameter WHERE url is not null AND url <> ''")
    Single<Optional<PrismaGestionCo_NDF_smartphone_parameter>> getSingle();

}
