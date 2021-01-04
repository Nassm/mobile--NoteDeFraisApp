package com.example.notedefrais.model.database.pojo;

public enum SynchronizationState {

    NOT_SEND(0),
    SENDING(1),
    COMPLETE(2),
    ERROR(3);

    private int code;

    public static final int NOT_SEND_CODE = 0;
    public static final int SENDING_CODE = 1;
    public static final int COMPLETE_CODE = 2;
    public static final int ERROR_CODE = 3;

    SynchronizationState(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

}
