package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.arquitectura_mvvm.modelo.repository.NotificacionRepository

class NotificacionViewModelFactory(private val repo: NotificacionRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = NotificacionViewModel(repo) as T
}