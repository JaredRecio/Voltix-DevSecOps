package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitectura_mvvm.modelo.entity.Meta
import com.example.arquitectura_mvvm.modelo.repository.MetaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MetaViewModel(private val repo: MetaRepository) : ViewModel() {
    fun obtenerPorGrupo(grupoId: Int): Flow<List<Meta>> = repo.obtenerPorGrupo(grupoId)

    fun agregar(grupoId: Int, descripcion: String, montoObjetivo: String) {
        val monto = montoObjetivo.toDoubleOrNull() ?: return
        viewModelScope.launch {
            repo.insertar(Meta(grupoId = grupoId, descripcion = descripcion, montoObjetivo = monto))
        }
    }

    fun abonar(meta: Meta, abono: String) {
        val monto = abono.toDoubleOrNull() ?: return
        viewModelScope.launch {
            repo.actualizar(meta.copy(montoActual = (meta.montoActual + monto).coerceAtMost(meta.montoObjetivo)))
        }
    }

    fun eliminar(meta: Meta) = viewModelScope.launch { repo.eliminar(meta) }
}