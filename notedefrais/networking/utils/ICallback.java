package com.example.notedefrais.networking.utils;

public interface ICallback<T> {

    void returnError(String message);
    void returnProgress(String entityName, int progress);
    void complete();
}
