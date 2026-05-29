package com.example.arquitectura_mvvm.MisViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arquitectura_mvvm.modelo.entity.Notificacion
import com.example.arquitectura_mvvm.modelo.repository.NotificacionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NotificacionViewModel(private val repo: NotificacionRepository) : ViewModel() {
    fun obtenerPorGrupo(grupoId: Int): Flow<List<Notificacion>> = repo.obtenerPorGrupo(grupoId)
    val noLeidas: Flow<Int> = repo.contarNoLeidas()

    fun enviarRecordatorio(grupoId: Int, persona: String, monto: Double) {
        val mensajes = listOf(
            "👀 $persona... Voltix sabe que tienes $${"%.2f".format(monto)} pendientes. Tu grupo también lo sabe pinche perre. 😏",
            "🌮 $persona, por ese dinero que debes (${"%.2f".format(monto)} MXN) ya habrían comprado tacos para todos.",
            "💸 Hola $persona! Tu deuda de $${"%.2f".format(monto)} MXN te extraña, págala ya, paro.",
            "🐢 $persona moviéndose a pagar sus $${"%.2f".format(monto)} MXN...",
            "⚡ Oye $persona, ahí te encargo los $${"%.2f".format(monto)} MXN ponte pilas en fa."
        )
        viewModelScope.launch {
            repo.insertar(
                Notificacion(
                    grupoId = grupoId,
                    mensaje = mensajes.random(),
                    tipo = "deuda"
                )
            )
        }
    }

    fun marcarLeida(n: Notificacion) = viewModelScope.launch { repo.marcarLeida(n) }
}