package com.example.arquitectura_mvvm.modelo.repository

import com.example.arquitectura_mvvm.modelo.dao.GastoDao
import com.example.arquitectura_mvvm.modelo.entity.Gasto
import kotlinx.coroutines.flow.Flow

class GastoRepository(private val gastoDao: GastoDao) {
    val gastos: Flow<List<Gasto>> = gastoDao.obtenerTodos()

    suspend fun insertar(gasto: Gasto) = gastoDao.insertar(gasto)
    suspend fun eliminar(gasto: Gasto) = gastoDao.eliminar(gasto)
}