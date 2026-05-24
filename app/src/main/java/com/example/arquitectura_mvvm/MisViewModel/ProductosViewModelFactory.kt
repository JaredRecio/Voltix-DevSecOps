package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arquitectura_mvvm.modelo.repository.ProductoRepository

class ProductosViewModelFactory(
    private val repositorio: ProductoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(requestedViewModel: Class<T>): T {
        return ProductosViewModel(repositorio) as T
    }
}