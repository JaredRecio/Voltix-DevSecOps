package com.example.arquitectura_mvvm.modelo.dao

import androidx.room.*
import com.example.arquitectura_mvvm.modelo.entity.Notificacion
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificacionDao {
    @Insert
    suspend fun insertar(notificacion: Notificacion)

    @Update
    suspend fun actualizar(notificacion: Notificacion)

    @Query("SELECT * FROM notificaciones WHERE grupoId = :grupoId ORDER BY id DESC")
    fun obtenerPorGrupo(grupoId: Int): Flow<List<Notificacion>>

    @Query("SELECT COUNT(*) FROM notificaciones WHERE leida = 0")
    fun contarNoLeidas(): Flow<Int>
}