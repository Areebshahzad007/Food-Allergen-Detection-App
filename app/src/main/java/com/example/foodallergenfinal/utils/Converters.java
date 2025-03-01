package com.example.foodallergenfinal.utils;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

@ProvidedTypeConverter
public class Converters {
    @TypeConverter
    public static String fromList(List<String> list) {
        return list != null ? String.join(",", list) : null;
    }

    @TypeConverter
    public static List<String> toList(String data) {
        return data != null ? Arrays.asList(data.split(",")) : null;
    }
}
