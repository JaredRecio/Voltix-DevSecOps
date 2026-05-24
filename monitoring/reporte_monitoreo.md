# Reporte de Monitoreo — Voltix
**Materia:** Innovaciones Tecnológicas  
**Alumno:** Jesús Jared Recio Ozúa  
**Fecha:** Mayo 2026  

## Descripción de la app
Voltix es una aplicación móvil Android para gestión de gastos grupales,
desarrollada con Kotlin, Jetpack Compose y arquitectura MVVM + Room.

## Métricas de estabilidad
| Métrica | Valor |
|---|---|
| Crashes reportados | 0 |
| ANRs | 0 |
| Build exitosos | 1 |
| Tasa de éxito CI | 100% |

## Pruebas ejecutadas
| Test | Resultado |
|---|---|
| gasto_montoPositivo_esValido | ✅ PASS |
| gasto_conceptoNoVacio_esValido | ✅ PASS |
| gasto_montoNegativo_esInvalido | ✅ PASS |
| calculo_totalGrupo_correcto | ✅ PASS |

## Pipeline CI/CD
- **Trigger:** Push a rama `master`
- **Build:** `assembleDebug` ✅
- **Tests:** `./gradlew test` ✅
- **Artefacto:** reporte de pruebas generado automáticamente

## Tecnologías utilizadas
- Kotlin + Jetpack Compose
- Room Database (SQLite local)
- Arquitectura MVVM
- GitHub Actions (CI/CD)
- Terraform (IaC)
- JUnit (pruebas unitarias)
