/**
 * Ejercicio 2: Find, Any y All
 *
 * Implementa los métodos de esta clase para que pasen todos los tests
 * del archivo Ejercicio2FindAnyAllTest.kt
 *
 * IMPORTANTE: No modifiques la firma de los métodos, solo implementa su lógica.
 */

data class Tarea(
    val id: Int,
    val titulo: String,
    val prioridad: Int, // 1 = baja, 2 = media, 3 = alta
    val completada: Boolean,
    val etiquetas: List<String>,
    val tiempoEstimadoHoras: Int,
)

data class EstadoProyecto(
    val hayTareasCriticasPendientes: Boolean,
    val totalHorasPendientes: Int,
    val todosLosBugsResueltos: Boolean
)

class GestorTareas {
    // Parte A: Operaciones con Find

    fun encontrarPrimeraTareaUrgente(tareas: List<Tarea>): Tarea? {
        // Encuentra la primera tarea con prioridad igual a 3
        return tareas.find { it.prioridad == 3 }
    }

    fun buscarPorId(
        tareas: List<Tarea>,
        id: Int,
    ): Tarea? {
        // Encuentra la tarea cuyo ID coincida con el especificado
        return tareas.find { it.id == id }
    }

    fun encontrarTareaPendienteConEtiqueta(
        tareas: List<Tarea>,
        etiqueta: String,
    ): Tarea? {
        // Encuentra la primera tarea que NO esté completada y contenga la etiqueta
        return tareas.find { !it.completada && it.etiquetas.contains(etiqueta) }
    }

    // Parte B: Operaciones con Any

    fun hayTareasUrgentesPendientes(tareas: List<Tarea>): Boolean {
        // Verifica si al menos una tarea de prioridad 3 está sin completar
        return tareas.any { it.prioridad == 3 && !it.completada }
    }

    fun hayTareasQueSuperanHoras(
        tareas: List<Tarea>,
        horasLimite: Int,
    ): Boolean {
        // Verifica si alguna tarea supera el límite de horas estricto
        return tareas.any { it.tiempoEstimadoHoras > horasLimite }
    }

    fun existeTareaConEtiqueta(
        tareas: List<Tarea>,
        etiqueta: String,
    ): Boolean {
        // Verifica si alguna tarea contiene la etiqueta especificada
        return tareas.any { it.etiquetas.contains(etiqueta) }
    }

    // Parte C: Operaciones con All

    fun todasCompletadas(tareas: List<Tarea>): Boolean {
        // Verifica si absolutamente todas las tareas están marcadas como completadas
        return tareas.all { it.completada }
    }

    fun todasTienenEtiquetas(tareas: List<Tarea>): Boolean {
        // Verifica si todas las tareas tienen una lista de etiquetas no vacía
        return tareas.all { it.etiquetas.isNotEmpty() }
    }

    fun todasDentroDeHoras(
        tareas: List<Tarea>,
        horasMaximo: Int,
    ): Boolean {
        // Verifica si todas las tareas toman un tiempo menor o igual al máximo
        return tareas.all { it.tiempoEstimadoHoras <= horasMaximo }
    }

    // Parte D: Combinación de Find, Any y All

    fun proyectoListoParaEntrega(tareas: List<Tarea>): Boolean {
        // 1. Todas las tareas de prioridad alta (3) están completadas
        val altasCompletadas = tareas.filter { it.prioridad == 3 }.all { it.completada }

        // 2. No hay ninguna tarea pendiente con etiqueta "blocker"
        val sinBlockersPendientes = !tareas.any { !it.completada && it.etiquetas.contains("blocker") }

        // 3. Existe al menos una tarea de documentación ("docs") completada
        val tieneDocsCompletada = tareas.any { it.completada && it.etiquetas.contains("docs") }

        return altasCompletadas && sinBlockersPendientes && tieneDocsCompletada
    }

    fun generarResumenEstado(tareas: List<Tarea>): EstadoProyecto {
        // hayTareasCriticasPendientes: prioridad 3 y no completada
        val criticasPendientes = tareas.any { it.prioridad == 3 && !it.completada }

        // totalHorasPendientes: suma el tiempo de las tareas que no están completadas
        val horasPendientes = tareas.filter { !it.completada }.sumOf { it.tiempoEstimadoHoras }

        // todosLosBugsResueltos: de las tareas que son "bug", todas deben estar completadas
        val bugsResueltos = tareas.filter { it.etiquetas.contains("bug") }.all { it.completada }

        return EstadoProyecto(
            hayTareasCriticasPendientes = criticasPendientes,
            totalHorasPendientes = horasPendientes,
            todosLosBugsResueltos = bugsResueltos
        )
    }
}