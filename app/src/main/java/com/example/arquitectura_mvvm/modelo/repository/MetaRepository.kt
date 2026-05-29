package com.example.arquitectura_mvvm.modelo.repository

import com.example.arquitectura_mvvm.modelo.dao.MetaDao
import com.example.arquitectura_mvvm.modelo.entity.Meta
import kotlinx.coroutines.flow.Flow

class MetaRepository(private val metaDao: MetaDao) {
    fun obtenerPorGrupo(grupoId: Int): Flow<List<Meta>> = metaDao.obtenerPorGrupo(grupoId)
    suspend fun insertar(meta: Meta) = metaDao.insertar(meta)
    suspend fun actualizar(meta: Meta) = metaDao.actualizar(meta)
    suspend fun eliminar(meta: Meta) = metaDao.eliminar(meta)
}