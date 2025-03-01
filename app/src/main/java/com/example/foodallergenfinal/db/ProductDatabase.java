package com.example.foodallergenfinal.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.foodallergenfinal.utils.Converters;

@Database(entities = {Product.class}, version = 2)
@TypeConverters(Converters.class)
public abstract class ProductDatabase extends RoomDatabase {
    private static volatile ProductDatabase instance;
    public abstract ProductDao productDao();

    public static synchronized ProductDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ProductDatabase.class, "product_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
