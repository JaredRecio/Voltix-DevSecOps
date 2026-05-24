package com.example.arquitectura_mvvm.modelo.dao

import androidx.room.*
import com.example.arquitectura_mvvm.modelo.entity.Gasto
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Insert
    suspend fun insertar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)

    @Query("SELECT * FROM gastos")
    fun obtenerTodos(): Flow<List<Gasto>>
}