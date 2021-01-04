package com.example.notedefrais.model.database.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import io.reactivex.Completable;


public interface GenericDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T entity);

    @Update()
    void update(T entity);

    @Delete
    void delete(T entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<T> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertCompletable(T entitie);



}
