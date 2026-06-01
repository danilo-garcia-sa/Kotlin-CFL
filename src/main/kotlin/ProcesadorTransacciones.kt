/**
 * Ejercicio 4: Funciones como Argumentos
 *
 * Implementa los métodos de esta clase para que pasen todos los tests
 * del archivo Ejercicio4FuncionesComoArgumentosTest.kt
 *
 * IMPORTANTE: No modifiques la firma de los métodos, solo implementa su lógica.
 */

enum class TipoTransaccion { INGRESO, EGRESO }

enum class EstadoTransaccion { PENDIENTE, PROCESADA, RECHAZADA }

data class Transaccion(
    val id: String,
    val monto: Double,
    val tipo: TipoTransaccion,
    val categoria: String,
    val fecha: String, // Formato: "YYYY-MM-DD"
    val estado: EstadoTransaccion,
)

data class ConfiguracionProcesamiento(
    val filtro: (Transaccion) -> Boolean,
    val transformacion: (Transaccion) -> Double,
    val formateo: (Double) -> String,
)

class ProcesadorTransacciones {
    // Parte A: Funciones de Transformación como Parámetros

    fun transformarMontos(
        transacciones: List<Transaccion>,
        transformacion: (Double) -> Double,
    ): List<Double> {
        // Mapeamos cada transacción extrayendo su monto y aplicándole la función de transformación
        return transacciones.map { transformacion(it.monto) }
    }

    fun <T> procesarCon(
        transacciones: List<Transaccion>,
        procesador: (Transaccion) -> T,
    ): List<T> {
        // Transformamos cada objeto Transaccion usando la función genérica 'procesador'
        return transacciones.map { procesador(it) }
    }

    // Parte B: Funciones de Filtrado como Parámetros

    fun filtrarTransacciones(
        transacciones: List<Transaccion>,
        predicado: (Transaccion) -> Boolean,
    ): List<Transaccion> {
        // Filtramos las transacciones que cumplan con la condición dada por el predicado
        return transacciones.filter { predicado(it) }
    }

    fun filtrarConMultiplesCriterios(
        transacciones: List<Transaccion>,
        criterios: List<(Transaccion) -> Boolean>,
    ): List<Transaccion> {
        // Filtramos asegurándonos de que CADA transacción cumpla con TODOS los criterios de la lista
        return transacciones.filter { transaccion ->
            criterios.all { criterio -> criterio(transaccion) }
        }
    }

    // Parte C: Funciones de Agregación como Parámetros

    fun <T> agregar(
        transacciones: List<Transaccion>,
        valorInicial: T,
        agregador: (T, Transaccion) -> T,
    ): T {
        // 'fold' acumula un valor partiendo de un estado inicial usando la función 'agregador'
        return transacciones.fold(valorInicial) { acumulador, transaccion ->
            agregador(acumulador, transaccion)
        }
    }

    // Parte D: Composición de Funciones

    fun ejecutarPipeline(
        transacciones: List<Transaccion>,
        filtro1: (Transaccion) -> Boolean,
        filtro2: (Transaccion) -> Boolean,
        transformacion: (Transaccion) -> Double,
        agregacion: (Double, Double) -> Double,
    ): Double {
        return transacciones
            .filter(filtro1)                                // 1) Aplicar filtro1
            .filter(filtro2)                                // 2) Aplicar filtro2
            .map(transformacion)                            // 3) Transformar a Double
            .fold(0.0) { acc, valor -> agregacion(acc, valor) } // 4) Agregar con valor inicial 0.0
    }

    fun procesarConConfiguracion(
        transacciones: List<Transaccion>,
        config: ConfiguracionProcesamiento,
    ): List<String> {
        return transacciones
            .filter(config.filtro)             // 1) Filtrar usando config.filtro
            .map(config.transformacion)        // 2) Transformar usando config.transformacion
            .map(config.formateo)              // 3) Formatear usando config.formateo
    }

    fun procesarConEventos(
        transacciones: List<Transaccion>,
        onTransaccionProcesada: (Transaccion) -> Unit,
        onTransaccionRechazada: (Transaccion) -> Unit,
    ) {
        // Iteramos sobre las transacciones y ejecutamos la acción correspondiente según su estado
        transacciones.forEach { transaccion ->
            when (transaccion.estado) {
                EstadoTransaccion.PROCESADA -> onTransaccionProcesada(transaccion)
                EstadoTransaccion.RECHAZADA -> onTransaccionRechazada(transaccion)
                else -> { /* No se hace nada para otros estados (como PENDIENTE) */ }
            }
        }
    }
}