package com.example.notedefrais.networking.models;

public class ResultLogin {

    private int userID;

    private String userName;

    public ResultLogin(int idPersonneConnected, String name)
    {
        this.userID = idPersonneConnected;
        this.userName = name;
    }

    public int getUserId()
    {
        return userID;
    }

    public void setUserId(int idPersonneConnected)
    {
        this.userID = idPersonneConnected;
    }

    public String getUsername()
    {
        return userName;
    }

    public void setUsername(String name)
    {
        this.userName = name;
    }
}
