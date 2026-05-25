package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arquitectura_mvvm.modelo.repository.GrupoRepository

class GrupoViewModelFactory(private val repositorio: GrupoRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GrupoViewModel(repositorio) as T
    }
}