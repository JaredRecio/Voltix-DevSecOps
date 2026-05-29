package com.example.arquitectura_mvvm.modelo

import android.content.Context
import androidx.room.*
import com.example.arquitectura_mvvm.modelo.dao.*
import com.example.arquitectura_mvvm.modelo.entity.*

@Database(
    entities = [Producto::class, Gasto::class, Grupo::class, Meta::class, Notificacion::class],
    version = 4
)
abstract class AppBaseDatos : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun gastoDao(): GastoDao
    abstract fun grupoDao(): GrupoDao
    abstract fun metaDao(): MetaDao
    abstract fun notificacionDao(): NotificacionDao

    companion object {
        @Volatile private var INSTANCE: AppBaseDatos? = null
        fun getDatabase(context: Context): AppBaseDatos {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppBaseDatos::class.java,
                    "voltix_bd"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}