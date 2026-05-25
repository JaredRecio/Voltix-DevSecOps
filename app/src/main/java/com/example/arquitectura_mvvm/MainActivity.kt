package com.example.arquitectura_mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.arquitectura_mvvm.MisViewModel.GastoViewModel
import com.example.arquitectura_mvvm.MisViewModel.GastoViewModelFactory
import com.example.arquitectura_mvvm.MisViewModel.GrupoViewModel
import com.example.arquitectura_mvvm.MisViewModel.GrupoViewModelFactory
import com.example.arquitectura_mvvm.modelo.AppBaseDatos
import com.example.arquitectura_mvvm.modelo.entity.Gasto
import com.example.arquitectura_mvvm.modelo.entity.Grupo
import com.example.arquitectura_mvvm.modelo.repository.GastoRepository
import com.example.arquitectura_mvvm.modelo.repository.GrupoRepository
import com.example.arquitectura_mvvm.ui.theme.Arquitectura_MVVMTheme

private val Fondo        = Color(0xFF1A0933)
private val FondoCampo   = Color(0xFF2D1052)
private val FondoTarjeta = Color(0xFF3B1263)
private val RosaFuerte   = Color(0xFFE040FB)
private val VioletaSuave = Color(0xFFCE93D8)
private val RosaClaro    = Color(0xFFF8BBD0)
private val AzulPastel   = Color(0xFFB3E5FC)
private val BlancoTexto  = Color(0xFFF3E5F5)
private val RojoError    = Color(0xFFFF6B9D)
private val VerdePago    = Color(0xFF80CBC4)
private val GradStart    = Color(0xFF9C27B0)
private val GradEnd      = Color(0xFFE91E8C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Arquitectura_MVVMTheme {
                AppNavegacion()
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val ctx = LocalContext.current
    val db = AppBaseDatos.getDatabase(ctx)
    val gastoVM: GastoViewModel = viewModel(
        factory = GastoViewModelFactory(GastoRepository(db.gastoDao()))
    )
    val grupoVM: GrupoViewModel = viewModel(
        factory = GrupoViewModelFactory(GrupoRepository(db.grupoDao()))
    )
    NavHost(navController = navController, startDestination = "grupos") {
        composable("grupos") {
            PantallaGrupos(navController, grupoVM)
        }
        composable("gastos/{grupoId}/{grupoNombre}") { back ->
            val grupoId = back.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
            val grupoNombre = back.arguments?.getString("grupoNombre") ?: "Grupo"
            PantallaGastos(navController, gastoVM, grupoId, grupoNombre)
        }
        composable("saldos/{grupoId}/{grupoNombre}") { back ->
            val grupoId = back.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
            val grupoNombre = back.arguments?.getString("grupoNombre") ?: "Grupo"
            PantallaSaldos(navController, gastoVM, grupoId, grupoNombre)
        }
    }
}

@Composable
fun CampoVoltix(
    valor: String,
    onValor: (String) -> Unit,
    label: String,
    placeholder: String,
    isError: Boolean,
    msgError: String,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = valor,
        onValueChange = onValor,
        label = { Text(label, color = VioletaSuave) },
        placeholder = { Text(placeholder) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = BlancoTexto,
            unfocusedTextColor = BlancoTexto,
            focusedContainerColor = FondoCampo,
            unfocusedContainerColor = FondoCampo,
            focusedBorderColor = RosaFuerte,
            unfocusedBorderColor = VioletaSuave,
            focusedPlaceholderColor = VioletaSuave,
            unfocusedPlaceholderColor = Color(0xFF9E7BB5),
            errorBorderColor = RojoError,
            errorContainerColor = FondoCampo,
            errorTextColor = BlancoTexto
        ),
        shape = RoundedCornerShape(16.dp),
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado)
    )
    if (isError) Text(
        msgError, color = RojoError, fontSize = 11.sp,
        modifier = Modifier.padding(start = 12.dp, top = 2.dp)
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
fun BotonGradiente(texto: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.horizontalGradient(listOf(GradStart, GradEnd))),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(26.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(texto, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun PantallaGrupos(nav: NavController, vm: GrupoViewModel) {
    val grupos by vm.grupos.collectAsState(initial = emptyList())
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var errNombre by rememberSaveable { mutableStateOf(false) }

    Scaffold(containerColor = Fondo) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Text("⚡ Voltix", color = BlancoTexto, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
            Text("Mis grupos", color = VioletaSuave, fontSize = 13.sp)
            Spacer(Modifier.height(16.dp))
            CampoVoltix(nombre, { nombre = it; errNombre = false },
                "Nombre del grupo", "Ej: Roomies, Viaje...", errNombre, "⚠ Escribe un nombre")
            CampoVoltix(descripcion, { descripcion = it },
                "Descripción (opcional)", "Ej: Gastos del depa", false, "")
            BotonGradiente("+ Crear grupo") {
                errNombre = nombre.isBlank()
                if (!errNombre) {
                    vm.agregar(nombre.trim(), descripcion.trim())
                    nombre = ""; descripcion = ""
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Grupos activos", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            if (grupos.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    Text("Aún no tienes grupos 👥", color = Color(0xFF9E7BB5), fontSize = 14.sp)
                }
            } else {
                LazyColumn {
                    items(grupos) { grupo ->
                        TarjetaGrupo(
                            grupo = grupo,
                            onVerGastos = { nav.navigate("gastos/${grupo.id}/${grupo.nombre}") },
                            onVerSaldos = { nav.navigate("saldos/${grupo.id}/${grupo.nombre}") },
                            onEliminar = { vm.eliminar(grupo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaGrupo(
    grupo: Grupo,
    onVerGastos: () -> Unit,
    onVerSaldos: () -> Unit,
    onEliminar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(FondoTarjeta)
            .padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(grupo.nombre, color = BlancoTexto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    if (grupo.descripcion.isNotBlank())
                        Text(grupo.descripcion, color = AzulPastel, fontSize = 12.sp)
                }
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(34.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF5C1A3A))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = RojoError, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.weight(1f).height(38.dp).clip(RoundedCornerShape(20.dp))
                        .background(Brush.horizontalGradient(listOf(GradStart, GradEnd))).clickable { onVerGastos() },
                    contentAlignment = Alignment.Center
                ) { Text("💳 Gastos", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                Box(
                    modifier = Modifier.weight(1f).height(38.dp).clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4A1080)).clickable { onVerSaldos() },
                    contentAlignment = Alignment.Center
                ) { Text("📊 Saldos", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun PantallaGastos(nav: NavController, vm: GastoViewModel, grupoId: Int, grupoNombre: String) {
    val todosGastos by vm.gastos.collectAsState(initial = emptyList())
    val gastos = todosGastos.filter { it.grupoId == grupoId }
    var concepto by rememberSaveable { mutableStateOf("") }
    var monto by rememberSaveable { mutableStateOf("") }
    var pagadoPor by rememberSaveable { mutableStateOf("") }
    var errConcepto by rememberSaveable { mutableStateOf(false) }
    var errMonto by rememberSaveable { mutableStateOf(false) }
    var errPagador by rememberSaveable { mutableStateOf(false) }
    val totalGrupo = gastos.sumOf { it.monto }

    Scaffold(containerColor = Fondo) { inner ->
        Column(
            modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = VioletaSuave)
                }
                Column {
                    Text("💳 Gastos", color = BlancoTexto, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(grupoNombre, color = VioletaSuave, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                    .padding(horizontal = 22.dp, vertical = 16.dp)
            ) {
                Column {
                    Text("Total del grupo", color = RosaClaro.copy(alpha = 0.85f), fontSize = 12.sp)
                    Text("$${"%.2f".format(totalGrupo)} MXN", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    Text("${gastos.size} gasto(s)", color = RosaClaro.copy(alpha = 0.7f), fontSize = 11.sp)
                }
            }
            Spacer(Modifier.height(14.dp))
            CampoVoltix(concepto, { concepto = it; errConcepto = false }, "Concepto", "Ej: Netflix...", errConcepto, "⚠ Escribe un concepto")
            CampoVoltix(monto, { monto = it; errMonto = false }, "Monto ($)", "0.00", errMonto, "⚠ Monto inválido", KeyboardType.Decimal)
            CampoVoltix(pagadoPor, { pagadoPor = it; errPagador = false }, "Pagado por", "Nombre", errPagador, "⚠ Escribe quién pagó")
            BotonGradiente("+ Agregar gasto") {
                val montoVal = monto.toDoubleOrNull()
                errConcepto = concepto.isBlank()
                errMonto = montoVal == null || montoVal <= 0.0
                errPagador = pagadoPor.isBlank()
                if (!errConcepto && !errMonto && !errPagador) {
                    vm.agregar(concepto.trim(), monto.trim(), pagadoPor.trim(), grupoId)
                    concepto = ""; monto = ""; pagadoPor = ""
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Gastos registrados", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            if (gastos.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    Text("Aún no hay gastos 💸", color = Color(0xFF9E7BB5), fontSize = 14.sp)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(gastos) { gasto -> TarjetaGasto(gasto) { vm.eliminar(gasto) } }
                }
            }
        }
    }
}

@Composable
fun TarjetaGasto(gasto: Gasto, onEliminar: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
            .clip(RoundedCornerShape(16.dp)).background(FondoTarjeta)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp))
                    .background(Brush.verticalGradient(listOf(GradStart, GradEnd))),
                contentAlignment = Alignment.Center
            ) { Text("💳", fontSize = 18.sp) }
            Column(Modifier.weight(1f)) {
                Text(gasto.concepto, color = BlancoTexto, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Pagó: ${gasto.pagadoPor}", color = AzulPastel, fontSize = 12.sp)
            }
            Text("$${"%.2f".format(gasto.monto)}", color = VerdePago, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            IconButton(
                onClick = onEliminar,
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF5C1A3A))
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = RojoError, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun PantallaSaldos(nav: NavController, vm: GastoViewModel, grupoId: Int, grupoNombre: String) {
    val todosGastos by vm.gastos.collectAsState(initial = emptyList())
    val gastos = todosGastos.filter { it.grupoId == grupoId }
    val totalGrupo = gastos.sumOf { it.monto }
    val personas = gastos.map { it.pagadoPor }.distinct()
    val numPersonas = if (personas.isEmpty()) 1 else personas.size
    val partePorPersona = totalGrupo / numPersonas
    val pagadoPorPersona = personas.associateWith { persona ->
        gastos.filter { it.pagadoPor == persona }.sumOf { it.monto }
    }

    Scaffold(containerColor = Fondo) { inner ->
        Column(
            modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = VioletaSuave)
                }
                Column {
                    Text("📊 Saldos", color = BlancoTexto, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(grupoNombre, color = VioletaSuave, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                    .padding(horizontal = 22.dp, vertical = 16.dp)
            ) {
                Column {
                    Text("Resumen del grupo", color = RosaClaro.copy(alpha = 0.85f), fontSize = 12.sp)
                    Text("$${"%.2f".format(totalGrupo)} MXN total", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("$numPersonas persona(s) · $${"%.2f".format(partePorPersona)} c/u", color = RosaClaro.copy(alpha = 0.8f), fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Balance por persona", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            if (personas.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    Text("Sin datos aún 📭", color = Color(0xFF9E7BB5), fontSize = 14.sp)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(personas) { persona ->
                        val pagado = pagadoPorPersona[persona] ?: 0.0
                        val balance = pagado - partePorPersona
                        val esPagador = balance >= 0
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(16.dp)).background(FondoTarjeta)
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (esPagador) Brush.verticalGradient(listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)))
                                            else Brush.verticalGradient(listOf(Color(0xFF7B1A1A), Color(0xFFE57373)))
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(persona.take(1).uppercase(), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(persona, color = BlancoTexto, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("Pagó: $${"%.2f".format(pagado)}", color = AzulPastel, fontSize = 12.sp)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        if (esPagador) "+$${"%.2f".format(balance)}" else "-$${"%.2f".format(-balance)}",
                                        color = if (esPagador) VerdePago else RojoError,
                                        fontWeight = FontWeight.ExtraBold, fontSize = 16.sp
                                    )
                                    Text(
                                        if (esPagador) "le deben" else "debe",
                                        color = if (esPagador) VerdePago.copy(alpha = 0.7f) else RojoError.copy(alpha = 0.7f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}