package com.example.arquitectura_mvvm.modelo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metas")
data class Meta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val grupoId: Int = 0,
    val descripcion: String,
    val montoObjetivo: Double,
    val montoActual: Double = 0.0
)