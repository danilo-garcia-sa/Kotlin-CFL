/**
 * Ejercicio 1: Map y Filter
 *
 * Implementa los métodos de esta clase para que pasen todos los tests
 * del archivo Ejercicio1MapFilterTest.kt
 *
 * IMPORTANTE: No modifiques la firma de los métodos, solo implementa su lógica.
 */

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String,
    val enStock: Boolean,
)

class ProductoManager {
    // Parte A: Operaciones con Map

    fun obtenerNombres(productos: List<Producto>): List<String> {
        // Transforma la lista de productos en una lista de Strings con sus nombres
        return productos.map { it.nombre }
    }

    fun aplicarDescuento(
        productos: List<Producto>,
        descuentoPorcentaje: Double,
    ): List<Double> {
        // Calcula el precio de cada producto aplicando el porcentaje de descuento indicado
        return productos.map { it.precio * (1 - descuentoPorcentaje / 100.0) }
    }

    fun generarEtiquetas(productos: List<Producto>): List<String> {
        // Mapea cada producto a un formato de texto específico evaluando si está disponible o agotado
        return productos.map { producto ->
            val estado = if (producto.enStock) "Disponible" else "Agotado"
            "${producto.nombre} - \$${producto.precio} ($estado)"
        }
    }

    // Parte B: Operaciones con Filter

    fun obtenerProductosEnStock(productos: List<Producto>): List<Producto> {
        // Conserva únicamente los productos cuya propiedad enStock sea verdadera
        return productos.filter { it.enStock }
    }

    fun filtrarPorPrecio(
        productos: List<Producto>,
        precioMin: Double,
        precioMax: Double,
    ): List<Producto> {
        // Filtra los productos que se encuentren dentro del rango de precio inclusivo
        return productos.filter { it.precio in precioMin..precioMax }
    }

    fun filtrarPorCategoria(
        productos: List<Producto>,
        categoria: String,
    ): List<Producto> {
        // Filtra los productos que pertenezcan a la categoría especificada
        return productos.filter { it.categoria == categoria }
    }

    // Parte C: Combinación de Map y Filter

    fun obtenerNombresProductosDisponibles(productos: List<Producto>): List<String> {
        // Primero filtramos los que tienen stock y luego extraemos sus nombres
        return productos
            .filter { it.enStock }
            .map { it.nombre }
    }

    fun aplicarDescuentoCategoria(
        productos: List<Producto>,
        categoria: String,
        descuentoPorcentaje: Double,
    ): List<Double> {
        // Filtramos por la categoría objetivo y luego calculamos los precios con descuento
        return productos
            .filter { it.categoria == categoria }
            .map { it.precio * (1 - descuentoPorcentaje / 100.0) }
    }

    fun generarReporteProductosCaros(
        productos: List<Producto>,
        precioMinimo: Double,
    ): List<String> {
        // Según el test, el reporte incluye productos que están en stock AND superan el precio mínimo
        return productos
            .filter { it.enStock && it.precio > precioMinimo }
            .map { "PRODUCTO PREMIUM: ${it.nombre} (\$${it.precio})" }
    }
}