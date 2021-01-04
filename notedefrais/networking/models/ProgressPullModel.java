package com.example.notedefrais.networking.models;

public class ProgressPullModel {

    String entityName;
    int progress;

    public ProgressPullModel(String entityName, int progress)
    {
        this.entityName = entityName;
        this.progress = progress;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

}
