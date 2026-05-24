package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arquitectura_mvvm.modelo.repository.GastoRepository

class GastoViewModelFactory(private val repositorio: GastoRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GastoViewModel(repositorio) as T
    }
}