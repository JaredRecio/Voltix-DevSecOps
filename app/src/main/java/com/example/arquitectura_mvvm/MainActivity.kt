package com.example.arquitectura_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arquitectura_mvvm.MisViewModel.GastoViewModel
import com.example.arquitectura_mvvm.MisViewModel.GastoViewModelFactory
import com.example.arquitectura_mvvm.modelo.AppBaseDatos
import com.example.arquitectura_mvvm.modelo.repository.GastoRepository
import com.example.arquitectura_mvvm.ui.theme.Arquitectura_MVVMTheme

// ── Paleta Voltix ──────────────────────────────────────────
private val Fondo         = Color(0xFF1A0933)   // morado muy oscuro
private val FondoCampo    = Color(0xFF2D1052)   // morado campo
private val FondoTarjeta  = Color(0xFF3B1263)   // morado tarjeta
private val RosaFuerte    = Color(0xFFE040FB)   // rosa/violeta
private val VioletaSuave  = Color(0xFFCE93D8)   // lila pastel
private val RosaClaro     = Color(0xFFF8BBD0)   // rosa claro
private val AzulPastel    = Color(0xFFB3E5FC)   // azul pastel
private val BlancoTexto   = Color(0xFFF3E5F5)   // blanco violáceo
private val RojoError     = Color(0xFFFF6B9D)   // rosa error
private val VerdePago     = Color(0xFF80CBC4)   // verde menta suave
private val GradStart     = Color(0xFF9C27B0)   // morado grad inicio
private val GradEnd       = Color(0xFFE91E8C)   // rosa grad fin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Arquitectura_MVVMTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Fondo
                ) { inner ->
                    PantallaGastos(modifier = Modifier.padding(inner))
                }
            }
        }
    }
}

@Composable
fun PantallaGastos(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val db  = AppBaseDatos.getDatabase(ctx)
    val vm: GastoViewModel = viewModel(
        factory = GastoViewModelFactory(GastoRepository(db.gastoDao()))
    )

    val gastos by vm.gastos.collectAsState(initial = emptyList())

    var concepto    by rememberSaveable { mutableStateOf("") }
    var monto       by rememberSaveable { mutableStateOf("") }
    var pagadoPor   by rememberSaveable { mutableStateOf("") }
    var errConcepto by rememberSaveable { mutableStateOf(false) }
    var errMonto    by rememberSaveable { mutableStateOf(false) }
    var errPagador  by rememberSaveable { mutableStateOf(false) }

    val totalGrupo = gastos.sumOf { it.monto }

    val campoColores = OutlinedTextFieldDefaults.colors(
        focusedTextColor          = BlancoTexto,
        unfocusedTextColor        = BlancoTexto,
        focusedContainerColor     = FondoCampo,
        unfocusedContainerColor   = FondoCampo,
        focusedBorderColor        = RosaFuerte,
        unfocusedBorderColor      = VioletaSuave,
        focusedPlaceholderColor   = VioletaSuave,
        unfocusedPlaceholderColor = Color(0xFF9E7BB5),
        errorBorderColor          = RojoError,
        errorContainerColor       = FondoCampo,
        errorTextColor            = BlancoTexto
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {

        // ── Header ─────────────────────────────────────────
        Spacer(Modifier.height(8.dp))
        Text(
            "⚡ Voltix",
            color = BlancoTexto,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            "Gestor de gastos grupales",
            color = VioletaSuave,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(14.dp))

        // ── Tarjeta total ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            Column {
                Text(
                    "Total del grupo",
                    color = RosaClaro.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "$${"%.2f".format(totalGrupo)} MXN",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "${gastos.size} gasto(s) registrado(s)",
                    color = RosaClaro.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // ── Formulario ──────────────────────────────────────
        OutlinedTextField(
            modifier  = Modifier.fillMaxWidth(),
            value     = concepto,
            onValueChange = { concepto = it; errConcepto = false },
            label     = { Text("Concepto", color = VioletaSuave) },
            placeholder = { Text("Ej: Netflix, Gym, Renta...") },
            colors    = campoColores,
            shape     = RoundedCornerShape(16.dp),
            isError   = errConcepto,
            singleLine = true
        )
        if (errConcepto) Text(
            "⚠ Escribe un concepto",
            color = RojoError, fontSize = 11.sp,
            modifier = Modifier.padding(start = 12.dp, top = 2.dp)
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            modifier  = Modifier.fillMaxWidth(),
            value     = monto,
            onValueChange = { monto = it; errMonto = false },
            label     = { Text("Monto ($)", color = VioletaSuave) },
            placeholder = { Text("0.00") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors    = campoColores,
            shape     = RoundedCornerShape(16.dp),
            isError   = errMonto,
            singleLine = true
        )
        if (errMonto) Text(
            "⚠ Ingresa un monto válido",
            color = RojoError, fontSize = 11.sp,
            modifier = Modifier.padding(start = 12.dp, top = 2.dp)
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            modifier  = Modifier.fillMaxWidth(),
            value     = pagadoPor,
            onValueChange = { pagadoPor = it; errPagador = false },
            label     = { Text("Pagado por", color = VioletaSuave) },
            placeholder = { Text("Nombre de quien pagó") },
            colors    = campoColores,
            shape     = RoundedCornerShape(16.dp),
            isError   = errPagador,
            singleLine = true
        )
        if (errPagador) Text(
            "⚠ Escribe quién pagó",
            color = RojoError, fontSize = 11.sp,
            modifier = Modifier.padding(start = 12.dp, top = 2.dp)
        )

        Spacer(Modifier.height(16.dp))

        // ── Botón agregar ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Brush.horizontalGradient(listOf(GradStart, GradEnd))),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val montoVal = monto.toDoubleOrNull()
                    errConcepto = concepto.isBlank()
                    errMonto    = montoVal == null || montoVal <= 0.0
                    errPagador  = pagadoPor.isBlank()
                    if (!errConcepto && !errMonto && !errPagador) {
                        vm.agregar(concepto.trim(), monto.trim(), pagadoPor.trim())
                        concepto = ""; monto = ""; pagadoPor = ""
                    }
                },
                modifier = Modifier.fillMaxSize(),
                colors   = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape    = RoundedCornerShape(26.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    "+ Agregar gasto",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Lista ───────────────────────────────────────────
        Text(
            "Gastos registrados",
            color = VioletaSuave,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))

        if (gastos.isEmpty()) {
            Box(
                Modifier.fillMaxWidth().padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Aún no hay gastos 💸",
                    color = Color(0xFF9E7BB5),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(gastos) { gasto ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(FondoTarjeta)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Icono decorativo
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.verticalGradient(listOf(GradStart, GradEnd))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💳", fontSize = 18.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    gasto.concepto,
                                    color = BlancoTexto,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    "Pagó: ${gasto.pagadoPor}",
                                    color = AzulPastel,
                                    fontSize = 12.sp
                                )
                            }

                            Text(
                                "$${"%.2f".format(gasto.monto)}",
                                color = VerdePago,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )

                            IconButton(
                                onClick = { vm.eliminar(gasto) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF5C1A3A))
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = RojoError,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}