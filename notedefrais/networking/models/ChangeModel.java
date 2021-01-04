package com.example.notedefrais.networking.models;

import java.util.Date;

public class ChangeModel {

    private String id;
    private String tableName;
    private Object object;
    private Date dateCreate;

    public ChangeModel(String id, String tableName, Object object, Date dateCreate)
    {
        this.id = id;
        this.tableName = tableName;
        this.object = object;
        this.dateCreate = dateCreate;
    }

    //region getter/setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }
    //endregion

}
