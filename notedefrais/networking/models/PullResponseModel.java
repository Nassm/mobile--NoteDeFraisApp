package com.example.notedefrais.networking.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PullResponseModel {

    private Date lastUpdatedDate;
    private Map<String, List<Object>> changes; /* dictionnary with tablename name and entities list*/

    public Date getLastUpdatedDate()
    {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate)
    {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Map<String, List<Object>> getChanges()
    {
        return changes;
    }

    public void setChanges(Map<String, List<Object>> changes)
    {
        this.changes = changes;
    }

}
