package com.example.arquitectura_mvvm.modelo.dao

import androidx.room.*
import com.example.arquitectura_mvvm.modelo.entity.Grupo
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {
    @Insert
    suspend fun insertar(grupo: Grupo)

    @Delete
    suspend fun eliminar(grupo: Grupo)

    @Query("SELECT * FROM grupos")
    fun obtenerTodos(): Flow<List<Grupo>>
}