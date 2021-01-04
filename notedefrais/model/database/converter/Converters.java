package com.example.notedefrais.model.database.converter;

import androidx.room.TypeConverter;

import com.example.notedefrais.model.database.pojo.SynchronizationState;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static SynchronizationState toSynchronizationState(int code) {
        return SynchronizationState.values()[code];
    }

    @TypeConverter
    public static Integer toInteger(SynchronizationState synchronizationState) {
        return synchronizationState.getCode();
    }

    @TypeConverter
    public static String uuidToString(int id) {
        if (id == 0)
            return null;

        String string = String.valueOf(id);
        if (string.equals("00000000-0000-0000-0000-000000000000"))
            return null;

        return string;
    }

    @TypeConverter
    public static int stringToInt(String string) {
        if (string == null)
            return 0;

        return Integer.valueOf(string);
    }


}
