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
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.arquitectura_mvvm.MisViewModel.*
import com.example.arquitectura_mvvm.modelo.AppBaseDatos
import com.example.arquitectura_mvvm.modelo.entity.*
import com.example.arquitectura_mvvm.modelo.repository.*
import com.example.arquitectura_mvvm.ui.theme.Arquitectura_MVVMTheme

// ── Paleta ──────────────────────────────────────────────────
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
private val AmarilloMeta = Color(0xFFFFD54F)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Arquitectura_MVVMTheme { AppNavegacion() }
        }
    }
}

@Composable
fun AppNavegacion() {
    val nav = rememberNavController()
    val ctx = LocalContext.current
    val db  = AppBaseDatos.getDatabase(ctx)

    val gastoVM: GastoViewModel = viewModel(factory = GastoViewModelFactory(GastoRepository(db.gastoDao())))
    val grupoVM: GrupoViewModel = viewModel(factory = GrupoViewModelFactory(GrupoRepository(db.grupoDao())))
    val metaVM:  MetaViewModel  = viewModel(factory = MetaViewModelFactory(MetaRepository(db.metaDao())))
    val notiVM:  NotificacionViewModel = viewModel(factory = NotificacionViewModelFactory(NotificacionRepository(db.notificacionDao())))

    NavHost(nav, startDestination = "bienvenida") {
        composable("bienvenida")        { PantallaBienvenida(nav) }
        composable("grupos/{usuario}")  { back ->
            val usuario = back.arguments?.getString("usuario") ?: "Usuario"
            PantallaGrupos(nav, grupoVM, gastoVM, notiVM, usuario)
        }
        composable("gastos/{grupoId}/{grupoNombre}") { back ->
            val gId   = back.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
            val gNom  = back.arguments?.getString("grupoNombre") ?: ""
            PantallaGastos(nav, gastoVM, notiVM, gId, gNom)
        }
        composable("saldos/{grupoId}/{grupoNombre}") { back ->
            val gId  = back.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
            val gNom = back.arguments?.getString("grupoNombre") ?: ""
            PantallaSaldos(nav, gastoVM, notiVM, gId, gNom)
        }
        composable("metas/{grupoId}/{grupoNombre}") { back ->
            val gId  = back.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
            val gNom = back.arguments?.getString("grupoNombre") ?: ""
            PantallaMetas(nav, metaVM, gId, gNom)
        }
        composable("notificaciones/{grupoId}/{grupoNombre}") { back ->
            val gId  = back.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
            val gNom = back.arguments?.getString("grupoNombre") ?: ""
            PantallaNotificaciones(nav, notiVM, gId, gNom)
        }
    }
}

// ── Componentes reutilizables ────────────────────────────────
@Composable
fun CampoVoltix(
    valor: String, onValor: (String) -> Unit,
    label: String, placeholder: String,
    isError: Boolean, msgError: String,
    teclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = valor, onValueChange = onValor,
        label = { Text(label, color = VioletaSuave) },
        placeholder = { Text(placeholder) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = BlancoTexto, unfocusedTextColor = BlancoTexto,
            focusedContainerColor = FondoCampo, unfocusedContainerColor = FondoCampo,
            focusedBorderColor = RosaFuerte, unfocusedBorderColor = VioletaSuave,
            focusedPlaceholderColor = VioletaSuave, unfocusedPlaceholderColor = Color(0xFF9E7BB5),
            errorBorderColor = RojoError, errorContainerColor = FondoCampo, errorTextColor = BlancoTexto
        ),
        shape = RoundedCornerShape(16.dp), isError = isError, singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado)
    )
    if (isError) Text(msgError, color = RojoError, fontSize = 11.sp,
        modifier = Modifier.padding(start = 12.dp, top = 2.dp))
    Spacer(Modifier.height(10.dp))
}

@Composable
fun BotonGradiente(texto: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Brush.horizontalGradient(listOf(GradStart, GradEnd))),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick, modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(26.dp), elevation = ButtonDefaults.buttonElevation(0.dp)
        ) { Text(texto, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
    }
}

@Composable
fun HeaderVoltix(titulo: String, subtitulo: String, nav: NavController? = null, badge: Int = 0) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (nav != null) {
            IconButton(onClick = { nav.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = VioletaSuave)
            }
        }
        Column(Modifier.weight(1f)) {
            Text(titulo, color = BlancoTexto, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text(subtitulo, color = VioletaSuave, fontSize = 12.sp)
        }
        if (badge > 0) {
            Box {
                Icon(Icons.Default.Notifications, contentDescription = null,
                    tint = RosaFuerte, modifier = Modifier.size(28.dp))
                Box(
                    modifier = Modifier.size(16.dp).clip(RoundedCornerShape(8.dp))
                        .background(RojoError).align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) { Text("$badge", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PANTALLA 0 — BIENVENIDA
// ══════════════════════════════════════════════════════════════
@Composable
fun PantallaBienvenida(nav: NavController) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var error  by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0D001A), Fondo, Color(0xFF2D1052)))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("⚡", fontSize = 64.sp)
            Spacer(Modifier.height(8.dp))
            Text("Voltix", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                "Gestor de gastos grupales\nsin dramas ni vergüenzas",
                color = VioletaSuave, fontSize = 14.sp, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = nombre, onValueChange = { nombre = it; error = false },
                label = { Text("¿Cómo te llamas?", color = VioletaSuave) },
                placeholder = { Text("Ej: Sofía, Jared, Carmen...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = BlancoTexto, unfocusedTextColor = BlancoTexto,
                    focusedContainerColor = FondoCampo, unfocusedContainerColor = FondoCampo,
                    focusedBorderColor = RosaFuerte, unfocusedBorderColor = VioletaSuave,
                    focusedPlaceholderColor = VioletaSuave, unfocusedPlaceholderColor = Color(0xFF9E7BB5),
                    errorBorderColor = RojoError, errorContainerColor = FondoCampo, errorTextColor = BlancoTexto
                ),
                shape = RoundedCornerShape(16.dp), isError = error, singleLine = true
            )
            if (error) Text("⚠ Escribe tu nombre para continuar",
                color = RojoError, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))

            Spacer(Modifier.height(16.dp))
            BotonGradiente("Entrar a Voltix ⚡") {
                error = nombre.isBlank()
                if (!error) nav.navigate("grupos/${nombre.trim()}")
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "💡 Voltix automatiza los cobros del grupo\npara que tú sigas siendo el amigo, no el cobrador.",
                color = Color(0xFF9E7BB5), fontSize = 12.sp, textAlign = TextAlign.Center
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PANTALLA 1 — GRUPOS
// ══════════════════════════════════════════════════════════════
@Composable
fun PantallaGrupos(
    nav: NavController, vm: GrupoViewModel,
    gastoVM: GastoViewModel, notiVM: NotificacionViewModel, usuario: String
) {
    val grupos   by vm.grupos.collectAsState(initial = emptyList())
    val todosGastos by gastoVM.gastos.collectAsState(initial = emptyList())
    val noLeidas by notiVM.noLeidas.collectAsState(initial = 0)
    var nombre      by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var errNombre   by rememberSaveable { mutableStateOf(false) }

    Scaffold(containerColor = Fondo) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("⚡ Voltix", color = BlancoTexto, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Hola, $usuario 👋", color = VioletaSuave, fontSize = 13.sp)
                }
                if (noLeidas > 0) {
                    Box {
                        Icon(Icons.Default.Notifications, contentDescription = null,
                            tint = RosaFuerte, modifier = Modifier.size(30.dp))
                        Box(
                            modifier = Modifier.size(16.dp).clip(RoundedCornerShape(8.dp))
                                .background(RojoError).align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) { Text("$noLeidas", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold) }
                    }
                }
            }
            Spacer(Modifier.height(14.dp))

            // Resumen global
            val totalGlobal = todosGastos.sumOf { it.monto }
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Total registrado", color = RosaClaro.copy(0.85f), fontSize = 12.sp)
                        Text("$${"%.2f".format(totalGlobal)} MXN",
                            color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("${grupos.size} grupo(s)", color = RosaClaro.copy(0.8f), fontSize = 12.sp)
                        Text("${todosGastos.size} gasto(s)", color = RosaClaro.copy(0.7f), fontSize = 11.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            CampoVoltix(nombre, { nombre = it; errNombre = false },
                "Nombre del grupo", "Ej: Roomies, Viaje Mazatlán...", errNombre, "⚠ Escribe un nombre")
            CampoVoltix(descripcion, { descripcion = it },
                "Descripción (opcional)", "Ej: Gastos del depa", false, "")
            BotonGradiente("+ Crear grupo") {
                errNombre = nombre.isBlank()
                if (!errNombre) { vm.agregar(nombre.trim(), descripcion.trim()); nombre = ""; descripcion = "" }
            }
            Spacer(Modifier.height(16.dp))
            Text("Mis grupos", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            if (grupos.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    Text("Aún no tienes grupos 👥\nCrea uno para empezar",
                        color = Color(0xFF9E7BB5), fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn {
                    items(grupos) { grupo ->
                        val gastosGrupo = todosGastos.filter { it.grupoId == grupo.id }
                        TarjetaGrupo(
                            grupo = grupo,
                            totalGrupo = gastosGrupo.sumOf { it.monto },
                            numGastos = gastosGrupo.size,
                            onVerGastos = { nav.navigate("gastos/${grupo.id}/${grupo.nombre}") },
                            onVerSaldos = { nav.navigate("saldos/${grupo.id}/${grupo.nombre}") },
                            onVerMetas  = { nav.navigate("metas/${grupo.id}/${grupo.nombre}") },
                            onEliminar  = { vm.eliminar(grupo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaGrupo(
    grupo: Grupo, totalGrupo: Double, numGastos: Int,
    onVerGastos: () -> Unit, onVerSaldos: () -> Unit,
    onVerMetas: () -> Unit, onEliminar: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
        .clip(RoundedCornerShape(16.dp)).background(FondoTarjeta).padding(16.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                        .background(Brush.verticalGradient(listOf(GradStart, GradEnd))),
                    contentAlignment = Alignment.Center
                ) { Text(grupo.nombre.take(1).uppercase(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(grupo.nombre, color = BlancoTexto, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    if (grupo.descripcion.isNotBlank()) Text(grupo.descripcion, color = AzulPastel, fontSize = 12.sp)
                    Text("$numGastos gasto(s) · $${"%.2f".format(totalGrupo)} MXN",
                        color = VioletaSuave, fontSize = 11.sp)
                }
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(34.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF5C1A3A))
                ) { Icon(Icons.Default.Delete, null, tint = RojoError, modifier = Modifier.size(18.dp)) }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    Triple("💳", "Gastos", onVerGastos),
                    Triple("📊", "Saldos", onVerSaldos),
                    Triple("🎯", "Metas",  onVerMetas)
                ).forEach { (icon, label, action) ->
                    Box(
                        modifier = Modifier.weight(1f).height(36.dp).clip(RoundedCornerShape(18.dp))
                            .background(
                                if (label == "Gastos") Brush.horizontalGradient(listOf(GradStart, GradEnd))
                                else Brush.horizontalGradient(listOf(Color(0xFF4A1080), Color(0xFF6A1099)))
                            ).clickable { action() },
                        contentAlignment = Alignment.Center
                    ) { Text("$icon $label", color = if (label == "Gastos") Color.White else VioletaSuave, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PANTALLA 2 — GASTOS
// ══════════════════════════════════════════════════════════════
@Composable
fun PantallaGastos(nav: NavController, vm: GastoViewModel, notiVM: NotificacionViewModel, grupoId: Int, grupoNombre: String) {
    val todosGastos by vm.gastos.collectAsState(initial = emptyList())
    val gastos = todosGastos.filter { it.grupoId == grupoId }
    var concepto  by rememberSaveable { mutableStateOf("") }
    var monto     by rememberSaveable { mutableStateOf("") }
    var pagadoPor by rememberSaveable { mutableStateOf("") }
    var errC by rememberSaveable { mutableStateOf(false) }
    var errM by rememberSaveable { mutableStateOf(false) }
    var errP by rememberSaveable { mutableStateOf(false) }

    Scaffold(containerColor = Fondo) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
            HeaderVoltix("💳 Gastos", grupoNombre, nav)
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                    .padding(horizontal = 22.dp, vertical = 16.dp)
            ) {
                Column {
                    Text("Total del grupo", color = RosaClaro.copy(0.85f), fontSize = 12.sp)
                    Text("$${"%.2f".format(gastos.sumOf { it.monto })} MXN",
                        color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                    Text("${gastos.size} gasto(s) registrado(s)", color = RosaClaro.copy(0.7f), fontSize = 11.sp)
                }
            }
            Spacer(Modifier.height(14.dp))
            CampoVoltix(concepto, { concepto = it; errC = false }, "Concepto", "Ej: Netflix, Gym...", errC, "⚠ Escribe un concepto")
            CampoVoltix(monto,   { monto = it;   errM = false }, "Monto ($)", "0.00", errM, "⚠ Monto inválido", KeyboardType.Decimal)
            CampoVoltix(pagadoPor, { pagadoPor = it; errP = false }, "Pagado por", "Nombre", errP, "⚠ Escribe quién pagó")
            BotonGradiente("+ Agregar gasto") {
                val mv = monto.toDoubleOrNull()
                errC = concepto.isBlank(); errM = mv == null || mv <= 0.0; errP = pagadoPor.isBlank()
                if (!errC && !errM && !errP) {
                    vm.agregar(concepto.trim(), monto.trim(), pagadoPor.trim(), grupoId)
                    concepto = ""; monto = ""; pagadoPor = ""
                }
            }
            Spacer(Modifier.height(12.dp))

            // Botón notificaciones
            Box(
                modifier = Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(21.dp))
                    .background(Color(0xFF4A1080)).clickable {
                        nav.navigate("notificaciones/$grupoId/$grupoNombre")
                    },
                contentAlignment = Alignment.Center
            ) { Text("🔔 Ver notificaciones del grupo", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.Bold) }

            Spacer(Modifier.height(12.dp))
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
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).clip(RoundedCornerShape(16.dp))
        .background(FondoTarjeta).padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.size(42.dp).clip(RoundedCornerShape(12.dp))
                .background(Brush.verticalGradient(listOf(GradStart, GradEnd))),
                contentAlignment = Alignment.Center
            ) { Text("💳", fontSize = 18.sp) }
            Column(Modifier.weight(1f)) {
                Text(gasto.concepto, color = BlancoTexto, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Pagó: ${gasto.pagadoPor}", color = AzulPastel, fontSize = 12.sp)
            }
            Text("$${"%.2f".format(gasto.monto)}", color = VerdePago, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            IconButton(onClick = onEliminar,
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF5C1A3A))
            ) { Icon(Icons.Default.Delete, null, tint = RojoError, modifier = Modifier.size(18.dp)) }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PANTALLA 3 — SALDOS
// ══════════════════════════════════════════════════════════════
@Composable
fun PantallaSaldos(nav: NavController, vm: GastoViewModel, notiVM: NotificacionViewModel, grupoId: Int, grupoNombre: String) {
    val todosGastos by vm.gastos.collectAsState(initial = emptyList())
    val gastos = todosGastos.filter { it.grupoId == grupoId }
    val total = gastos.sumOf { it.monto }
    val personas = gastos.map { it.pagadoPor }.distinct()
    val n = if (personas.isEmpty()) 1 else personas.size
    val parte = total / n
    val pagadoPorPersona = personas.associateWith { p -> gastos.filter { it.pagadoPor == p }.sumOf { it.monto } }

    Scaffold(containerColor = Fondo) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
            HeaderVoltix("📊 Saldos", grupoNombre, nav)
            Spacer(Modifier.height(12.dp))
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                .padding(horizontal = 22.dp, vertical = 16.dp)
            ) {
                Column {
                    Text("Resumen del grupo", color = RosaClaro.copy(0.85f), fontSize = 12.sp)
                    Text("$${"%.2f".format(total)} MXN total", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text("$n persona(s) · $${"%.2f".format(parte)} c/u", color = RosaClaro.copy(0.8f), fontSize = 12.sp)
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
                        val pagado  = pagadoPorPersona[persona] ?: 0.0
                        val balance = pagado - parte
                        val esPagador = balance >= 0
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(16.dp)).background(FondoTarjeta)
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp))
                                            .background(if (esPagador) Brush.verticalGradient(listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)))
                                            else Brush.verticalGradient(listOf(Color(0xFF7B1A1A), Color(0xFFE57373)))),
                                        contentAlignment = Alignment.Center
                                    ) { Text(persona.take(1).uppercase(), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold) }
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
                                        Text(if (esPagador) "le deben" else "debe",
                                            color = if (esPagador) VerdePago.copy(0.7f) else RojoError.copy(0.7f), fontSize = 11.sp)
                                    }
                                }
                                if (!esPagador) {
                                    Spacer(Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(36.dp)
                                            .clip(RoundedCornerShape(18.dp))
                                            .background(Brush.horizontalGradient(listOf(GradStart, GradEnd)))
                                            .clickable { notiVM.enviarRecordatorio(grupoId, persona, -balance) },
                                        contentAlignment = Alignment.Center
                                    ) { Text("⚡ Enviar recordatorio gracioso", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PANTALLA 4 — METAS
// ══════════════════════════════════════════════════════════════
@Composable
fun PantallaMetas(nav: NavController, vm: MetaViewModel, grupoId: Int, grupoNombre: String) {
    val metas by vm.obtenerPorGrupo(grupoId).collectAsState(initial = emptyList())
    var descripcion by rememberSaveable { mutableStateOf("") }
    var objetivo    by rememberSaveable { mutableStateOf("") }
    var errDesc by rememberSaveable { mutableStateOf(false) }
    var errObj  by rememberSaveable { mutableStateOf(false) }
    var metaAbonando by remember { mutableStateOf<Meta?>(null) }
    var abono        by rememberSaveable { mutableStateOf("") }

    Scaffold(containerColor = Fondo) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
            HeaderVoltix("🎯 Metas", grupoNombre, nav)
            Spacer(Modifier.height(12.dp))
            CampoVoltix(descripcion, { descripcion = it; errDesc = false }, "Meta del grupo", "Ej: Viaje a Mazatlán, PS5...", errDesc, "⚠ Escribe una meta")
            CampoVoltix(objetivo, { objetivo = it; errObj = false }, "Monto objetivo ($)", "0.00", errObj, "⚠ Monto inválido", KeyboardType.Decimal)
            BotonGradiente("+ Agregar meta") {
                errDesc = descripcion.isBlank(); errObj = objetivo.toDoubleOrNull() == null
                if (!errDesc && !errObj) { vm.agregar(grupoId, descripcion.trim(), objetivo.trim()); descripcion = ""; objetivo = "" }
            }
            Spacer(Modifier.height(16.dp))
            Text("Metas activas", color = VioletaSuave, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            if (metas.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                    Text("Sin metas aún 🎯\nAgrega una para motivar al grupo",
                        color = Color(0xFF9E7BB5), fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(metas) { meta ->
                        val progreso = if (meta.montoObjetivo > 0) (meta.montoActual / meta.montoObjetivo).toFloat().coerceIn(0f, 1f) else 0f
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(16.dp)).background(FondoTarjeta).padding(16.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎯", fontSize = 24.sp)
                                    Spacer(Modifier.width(10.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(meta.descripcion, color = BlancoTexto, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("$${"%.2f".format(meta.montoActual)} / $${"%.2f".format(meta.montoObjetivo)} MXN",
                                            color = AmarilloMeta, fontSize = 12.sp)
                                    }
                                    Text("${(progreso * 100).toInt()}%", color = AmarilloMeta, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                                    Spacer(Modifier.width(8.dp))
                                    IconButton(onClick = { vm.eliminar(meta) },
                                        modifier = Modifier.size(30.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF5C1A3A))
                                    ) { Icon(Icons.Default.Delete, null, tint = RojoError, modifier = Modifier.size(16.dp)) }
                                }
                                Spacer(Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { progreso },
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                    color = AmarilloMeta, trackColor = Color(0xFF4A1080)
                                )
                                if (progreso >= 1f) {
                                    Spacer(Modifier.height(6.dp))
                                    Text("🎉 ¡Meta alcanzada! El grupo lo logró.", color = VerdePago, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                } else {
                                    Spacer(Modifier.height(8.dp))
                                    if (metaAbonando?.id == meta.id) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            OutlinedTextField(
                                                modifier = Modifier.weight(1f),
                                                value = abono, onValueChange = { abono = it },
                                                label = { Text("Abono ($)", color = VioletaSuave) },
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = BlancoTexto, unfocusedTextColor = BlancoTexto,
                                                    focusedContainerColor = FondoCampo, unfocusedContainerColor = FondoCampo,
                                                    focusedBorderColor = RosaFuerte, unfocusedBorderColor = VioletaSuave
                                                ),
                                                shape = RoundedCornerShape(12.dp), singleLine = true,
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                            )
                                            Button(
                                                onClick = { vm.abonar(meta, abono); abono = ""; metaAbonando = null },
                                                colors = ButtonDefaults.buttonColors(containerColor = GradStart),
                                                shape = RoundedCornerShape(12.dp)
                                            ) { Text("✓", color = Color.White) }
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().height(34.dp)
                                                .clip(RoundedCornerShape(17.dp))
                                                .background(Color(0xFF4A1080))
                                                .clickable { metaAbonando = meta },
                                            contentAlignment = Alignment.Center
                                        ) { Text("+ Abonar a esta meta", color = VioletaSuave, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════
// PANTALLA 5 — NOTIFICACIONES
// ══════════════════════════════════════════════════════════════
@Composable
fun PantallaNotificaciones(nav: NavController, vm: NotificacionViewModel, grupoId: Int, grupoNombre: String) {
    val notis by vm.obtenerPorGrupo(grupoId).collectAsState(initial = emptyList())

    Scaffold(containerColor = Fondo) { inner ->
        Column(modifier = Modifier.padding(inner).fillMaxSize().padding(horizontal = 16.dp, vertical = 10.dp)) {
            HeaderVoltix("🔔 Notificaciones", grupoNombre, nav)
            Spacer(Modifier.height(12.dp))
            if (notis.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉", fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("¡Todo en orden!\nNo hay recordatorios pendientes.",
                            color = Color(0xFF9E7BB5), fontSize = 14.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(notis) { noti ->
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (noti.leida) FondoTarjeta else Color(0xFF4A1263))
                                .clickable { vm.marcarLeida(noti) }
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(if (noti.tipo == "deuda") "⚡" else "ℹ️", fontSize = 20.sp)
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(noti.mensaje, color = BlancoTexto, fontSize = 13.sp)
                                    if (!noti.leida) Text("Toca para marcar como leída",
                                        color = VioletaSuave.copy(0.6f), fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}