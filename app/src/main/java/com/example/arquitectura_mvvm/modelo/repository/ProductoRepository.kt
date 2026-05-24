package com.example.arquitectura_mvvm.modelo.repository

import com.example.arquitectura_mvvm.modelo.dao.ProductoDao
import com.example.arquitectura_mvvm.modelo.entity.Producto
import kotlinx.coroutines.flow.Flow

class ProductoRepository(
    private val productoDao: ProductoDao
) {
    val productos: Flow<List<Producto>> = productoDao.obtenerTodos()

    suspend fun insertar(producto: Producto) {
        productoDao.insertar(producto)
    }

    suspend fun actualizar(producto: Producto) {
        productoDao.actualizar(producto)
    }

    suspend fun eliminar(producto: Producto) {
        productoDao.eliminar(producto)
    }
}