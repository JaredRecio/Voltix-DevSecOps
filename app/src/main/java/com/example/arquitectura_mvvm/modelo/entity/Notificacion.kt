package com.example.arquitectura_mvvm.modelo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notificaciones")
data class Notificacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val grupoId: Int = 0,
    val mensaje: String,
    val tipo: String = "info", // "info", "deuda", "meta"
    val leida: Boolean = false
)