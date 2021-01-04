package com.example.notedefrais.model.database.tables;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.notedefrais.model.database.pojo.SynchronizationState;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity(tableName = PrismaGestionCo_NDF_synchronisation.TABLE_NAME)
public class PrismaGestionCo_NDF_synchronisation extends GenericEntity implements Serializable {

    //region Constant
    public static final String TABLE_NAME = "PrismaGestionCo_NDF_synchronisation";
    //endregion

    //region Fields
    private SynchronizationState synchronizationsState;
    private Date date_create;
    private Date date_update;

    @Ignore
    private List<PrismaGestionCo_NDF_synchronisation_line> ndf_synchronization_lineDevice;
    //endregion


    //region Getter/Setter
    public SynchronizationState getSynchronizationsState()
    {
        return synchronizationsState;
    }

    public void setSynchronizationsState(SynchronizationState synchronizationsState)
    {
        this.synchronizationsState = synchronizationsState;
    }

    public List<PrismaGestionCo_NDF_synchronisation_line> getNdf_synchronization_lineDevice()
    {
        return ndf_synchronization_lineDevice;
    }

    public void setNdf_synchronization_lineDevice(List<PrismaGestionCo_NDF_synchronisation_line> ndf_synchronization_lineDevice)
    {
        this.ndf_synchronization_lineDevice = ndf_synchronization_lineDevice;
    }

    public Date getDate_create()
    {
        return date_create;
    }

    public void setDate_create(Date date_create)
    {
        this.date_create = date_create;
    }

    public Date getDate_update()
    {
        return date_update;
    }

    public void setDate_update(Date date_update)
    {
        this.date_update = date_update;
    }
    //endregion
}
