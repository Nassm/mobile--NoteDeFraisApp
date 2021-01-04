package com.example.notedefrais.model.utils;

import java.util.ArrayList;

public interface GenericAdapter<T>
{
    void setList(ArrayList<T> entities);

    void addToList(T entity);

    void removeFromList(T entity);
}
