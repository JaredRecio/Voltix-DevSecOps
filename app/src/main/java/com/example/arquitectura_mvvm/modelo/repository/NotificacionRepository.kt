package com.example.arquitectura_mvvm.modelo.repository

import com.example.arquitectura_mvvm.modelo.dao.NotificacionDao
import com.example.arquitectura_mvvm.modelo.entity.Notificacion
import kotlinx.coroutines.flow.Flow

class NotificacionRepository(private val dao: NotificacionDao) {
    fun obtenerPorGrupo(grupoId: Int): Flow<List<Notificacion>> = dao.obtenerPorGrupo(grupoId)
    fun contarNoLeidas(): Flow<Int> = dao.contarNoLeidas()
    suspend fun insertar(n: Notificacion) = dao.insertar(n)
    suspend fun marcarLeida(n: Notificacion) = dao.actualizar(n.copy(leida = true))
}