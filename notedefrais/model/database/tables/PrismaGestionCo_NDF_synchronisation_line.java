package com.example.notedefrais.model.database.tables;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.notedefrais.App;
import com.example.notedefrais.model.constante.ConstanteDataBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(tableName = PrismaGestionCo_NDF_synchronisation_line.TABLE_NAME)
public class PrismaGestionCo_NDF_synchronisation_line extends GenericEntity implements Serializable {

    //region Constant
    public static final String TABLE_NAME = "PrismaGestionCo_NDF_synchronisation_line";

    //region Fields
    private int id_synchronization;

    private int id_line;

    private String table;

    private Date date_create;

    private Date date_update;

    @Ignore
    private PrismaGestionCo_NDF_synchronisation prismaGestionCo_NDF_Synchronisation;


    /** region constructor |require empty constructor */
    public PrismaGestionCo_NDF_synchronisation_line() {
    }



    /** region getter and setter */
    public int getId_synchronization()
    {
        return id_synchronization;
    }

    public void setId_synchronization(int id_synchronization)
    {
        this.id_synchronization = id_synchronization;
    }

    public int getId_line()
    {
        return id_line;
    }

    public void setId_line(int id_line)
    {
        this.id_line = id_line;
    }

    public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
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

    public PrismaGestionCo_NDF_synchronisation getPrismaGestionCo_NDF_Synchronisation()
    {
        return prismaGestionCo_NDF_Synchronisation;
    }
    public void setPrismaGestionCo_NDF_Synchronisation(PrismaGestionCo_NDF_synchronisation prismaGestionCo_NDF_Synchronisation)
    {
        this.prismaGestionCo_NDF_Synchronisation = prismaGestionCo_NDF_Synchronisation;
    }


    public void createFromJson(JSONArray json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<ArrayList<PrismaGestionCo_NDF_synchronisation_line>>() {
        }.getType();

        List<PrismaGestionCo_NDF_synchronisation_line> list = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_synchronisation_line_dao().insertAll(list);
    }

    public void createFromJson(JsonObject json) throws SQLException
    {
        Gson gson = new GsonBuilder().setDateFormat(ConstanteDataBase.DATE_FORMAT).create();
        Type listType = new TypeToken<PrismaGestionCo_NDF_synchronisation_line>() {
        }.getType();

        PrismaGestionCo_NDF_synchronisation_line entity = gson.fromJson(String.valueOf(json), listType);

        App.get().getDB().prismagestionco_ndf_synchronisation_line_dao().insert(entity);
    }
    public static PrismaGestionCo_NDF_synchronisation_line addSynchro(String action_crud, String table_name, UUID id_table)
    {
        PrismaGestionCo_NDF_synchronisation_line synchro = new PrismaGestionCo_NDF_synchronisation_line();

        // TODO: 14/01/2020 corriger les uid en ID
        synchro.setId(0);
        synchro.setTable(table_name);
        synchro.setDate_create(new Date());

        return synchro;
    }

}
