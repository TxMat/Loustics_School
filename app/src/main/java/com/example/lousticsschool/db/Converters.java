package com.example.lousticsschool.db;

import androidx.room.TypeConverter;

public class Converters {

    // converts String[] to String
    @TypeConverter
    public String fromStringArray(String[] stringArray) {
        StringBuilder sb = new StringBuilder();
        for (String s : stringArray) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString();
    }

    // converts String to String[]
    @TypeConverter
    public String[] toStringArray(String string) {
        return string.split(",");
    }
}
