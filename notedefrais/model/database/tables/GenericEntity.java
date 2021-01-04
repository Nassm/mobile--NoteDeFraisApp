package com.example.notedefrais.model.database.tables;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.example.notedefrais.model.utils.NdfLogger;

import java.io.Serializable;

public abstract class GenericEntity implements Serializable {

    private static final String TAG = GenericEntity.class.getSimpleName();

    /** region fields */
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String idSmartphone;
    @NonNull
    /** end region */


    /** region Getter/Setter */
    public int getId()
    {
        return id;
    }

    public void setId(@NonNull int id)
    {
        this.id = id;
    }

    public String getIdSmartphone() { return idSmartphone;
    }

    public void setIdSmartphone(String idSmartphone) { this.idSmartphone = idSmartphone;
    }

    public GenericEntity clone()
    {
        GenericEntity o = null;
        try
        {
            o = (GenericEntity) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            NdfLogger.e(TAG, e.getMessage());
        }
        return o;
    }
}
