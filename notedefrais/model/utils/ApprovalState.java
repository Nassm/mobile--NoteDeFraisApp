package com.example.notedefrais.model.utils;

public enum ApprovalState {

    NOT_SEND ("Non transmis"),

    SUBMITTED ("Soumis pour approbation"),

    IN_PROGRESS ("En cours"),

    ACCEPTED ("Comptabilisee"),

    DENY ("Refusee");


    private final String state;


    ApprovalState(String state)
    {
        this.state = state;
    }

    public String toString()
    {
        return this.state;
    }
}
