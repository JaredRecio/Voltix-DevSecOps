package com.example.arquitectura_mvvm.modelo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val concepto: String,
    val monto: Double,
    val pagadoPor: String
)