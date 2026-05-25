package com.example.arquitectura_mvvm

import com.example.arquitectura_mvvm.modelo.entity.Gasto
import org.junit.Assert.*
import org.junit.Test

class GastoTest {

    @Test
    fun gasto_montoPositivo_esValido() {
        val gasto = Gasto(concepto = "Netflix", monto = 200.0, pagadoPor = "Rosa")
        assertTrue(gasto.monto > 0)
    }

    @Test
    fun gasto_conceptoNoVacio_esValido() {
        val gasto = Gasto(concepto = "Gym", monto = 500.0, pagadoPor = "Carmen")
        assertTrue(gasto.concepto.isNotBlank())
    }

    @Test
    fun gasto_montoNegativo_esInvalido() {
        val monto = -100.0
        assertFalse(monto > 0)
    }

    @Test
    fun calculo_totalGrupo_correcto() {
        val gastos = listOf(
            Gasto(concepto = "Luz", monto = 300.0, pagadoPor = "Ana"),
            Gasto(concepto = "Internet", monto = 450.0, pagadoPor = "Jael")
        )
        val total = gastos.sumOf { it.monto }
        assertEquals(750.0, total, 0.01)
    }
}