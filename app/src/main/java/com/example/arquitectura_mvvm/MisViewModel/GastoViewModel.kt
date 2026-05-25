package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitectura_mvvm.modelo.entity.Gasto
import com.example.arquitectura_mvvm.modelo.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GastoViewModel(private val repositorio: GastoRepository) : ViewModel() {
    val gastos: Flow<List<Gasto>> = repositorio.gastos

    fun agregar(concepto: String, monto: String, pagadoPor: String, grupoId: Int = 0) {
        val montoDouble = monto.toDoubleOrNull() ?: return
        viewModelScope.launch {
            repositorio.insertar(
                Gasto(
                    concepto = concepto,
                    monto = montoDouble,
                    pagadoPor = pagadoPor,
                    grupoId = grupoId
                )
            )
        }
    }

    fun eliminar(gasto: Gasto) {
        viewModelScope.launch { repositorio.eliminar(gasto) }
    }
}