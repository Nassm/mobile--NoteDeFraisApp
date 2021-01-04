package com.example.notedefrais.model.database.tables;

import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = PrismaGestionCo_NDF_smartphone_parameter.TABLE_NAME)
public class PrismaGestionCo_NDF_smartphone_parameter extends GenericEntity {

    //region Constant
    public static final String TABLE_NAME = "PrismaGestionCo_NDF_smartphone_parameter";

    //region Fields
    private String url;
    private Date dateEndLastSynchro;
    private String PrismaPhoneNumer;
    //endregion


    //region Getter/Setter
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateEndLastSynchro() {
        return dateEndLastSynchro;
    }

    public void setDateEndLastSynchro(Date dateEndLastSynchro) {
        this.dateEndLastSynchro = dateEndLastSynchro; }

    public String getPrismaPhoneNumer() { return PrismaPhoneNumer; }

    public void setPrismaPhoneNumer(String prismaPhoneNumer) { PrismaPhoneNumer = prismaPhoneNumer; }

    //endregion



}
