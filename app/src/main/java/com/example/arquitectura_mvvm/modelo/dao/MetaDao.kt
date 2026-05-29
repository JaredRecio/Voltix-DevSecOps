package com.example.arquitectura_mvvm.modelo.dao

import androidx.room.*
import com.example.arquitectura_mvvm.modelo.entity.Meta
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaDao {
    @Insert
    suspend fun insertar(meta: Meta)

    @Update
    suspend fun actualizar(meta: Meta)

    @Delete
    suspend fun eliminar(meta: Meta)

    @Query("SELECT * FROM metas WHERE grupoId = :grupoId")
    fun obtenerPorGrupo(grupoId: Int): Flow<List<Meta>>
}