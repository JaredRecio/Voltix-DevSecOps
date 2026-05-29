package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arquitectura_mvvm.modelo.repository.MetaRepository

class MetaViewModelFactory(private val repo: MetaRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MetaViewModel(repo) as T
}