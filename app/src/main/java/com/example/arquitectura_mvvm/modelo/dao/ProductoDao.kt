package com.example.arquitectura_mvvm.modelo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.arquitectura_mvvm.modelo.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert
    suspend fun insertar(producto: Producto)

    @Update
    suspend fun actualizar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Query("select * from productos")
    fun obtenerTodos(): Flow<List<Producto>>
}