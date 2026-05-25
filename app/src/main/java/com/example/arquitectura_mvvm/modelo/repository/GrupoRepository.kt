package com.example.arquitectura_mvvm.modelo.repository

import com.example.arquitectura_mvvm.modelo.dao.GrupoDao
import com.example.arquitectura_mvvm.modelo.entity.Grupo
import kotlinx.coroutines.flow.Flow

class GrupoRepository(private val grupoDao: GrupoDao) {
    val grupos: Flow<List<Grupo>> = grupoDao.obtenerTodos()
    suspend fun insertar(grupo: Grupo) = grupoDao.insertar(grupo)
    suspend fun eliminar(grupo: Grupo) = grupoDao.eliminar(grupo)
}