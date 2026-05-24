package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitectura_mvvm.modelo.entity.Producto
import com.example.arquitectura_mvvm.modelo.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repositorio: ProductoRepository
) : ViewModel() {

    val productos: Flow<List<Producto>> = repositorio.productos

    fun agregarProducto(nombre: String, precio: String) {
        val precioDouble = precio.toDoubleOrNull() ?: 1.0
        val producto = Producto(nombre = nombre, precio = precioDouble)
        viewModelScope.launch {
            repositorio.insertar(producto)
        }
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.actualizar(producto)
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repositorio.eliminar(producto)
        }
    }
}