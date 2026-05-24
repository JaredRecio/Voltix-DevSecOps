package com.example.arquitectura_mvvm.modelo

import android.content.Context
import androidx.room.*
import com.example.arquitectura_mvvm.modelo.dao.GastoDao
import com.example.arquitectura_mvvm.modelo.dao.ProductoDao
import com.example.arquitectura_mvvm.modelo.entity.Gasto
import com.example.arquitectura_mvvm.modelo.entity.Producto

@Database(entities = [Producto::class, Gasto::class], version = 2)
abstract class AppBaseDatos : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile private var INSTANCE: AppBaseDatos? = null

        fun getDatabase(context: Context): AppBaseDatos {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppBaseDatos::class.java,
                    "voltix_bd"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}