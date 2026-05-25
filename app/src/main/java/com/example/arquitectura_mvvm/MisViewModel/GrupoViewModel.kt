package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitectura_mvvm.modelo.entity.Grupo
import com.example.arquitectura_mvvm.modelo.repository.GrupoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GrupoViewModel(private val repositorio: GrupoRepository) : ViewModel() {
    val grupos: Flow<List<Grupo>> = repositorio.grupos

    fun agregar(nombre: String, descripcion: String) {
        if (nombre.isBlank()) return
        viewModelScope.launch {
            repositorio.insertar(Grupo(nombre = nombre, descripcion = descripcion))
        }
    }

    fun eliminar(grupo: Grupo) {
        viewModelScope.launch { repositorio.eliminar(grupo) }
    }
}