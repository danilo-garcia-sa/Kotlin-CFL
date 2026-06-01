/**
 * Ejercicio 5: It y Scope Functions (run, apply, also, let)
 *
 * Implementa los métodos de esta clase para que pasen todos los tests
 * del archivo Ejercicio5ItScopeFunctionsTest.kt
 *
 * IMPORTANTE: No modifiques la firma de los métodos, solo implementa su lógica.
 * IMPORTANTE: Debes usar las scope functions indicadas en cada sección.
 */

data class Usuario(
    var id: Int = 0,
    var nombre: String = "",
    var email: String = "",
    var activo: Boolean = false,
    var roles: MutableList<String> = mutableListOf(),
    var configuracion: ConfiguracionUsuario = ConfiguracionUsuario(),
)

data class ConfiguracionUsuario(
    var tema: String = "claro",
    var idioma: String = "es",
    var notificaciones: Boolean = true,
    var nivelPrivacidad: Int = 1,
)

data class Validacion(
    val campo: String,
    val valido: Boolean,
    val mensaje: String,
)

class UsuarioBuilder {
    // Parte A: Uso del parámetro implícito 'it'

    fun procesarNumeros(numeros: List<Int>): List<Int> {
        // Filtra los pares y los multiplica por 10 usando 'it'
        return numeros.filter { it % 2 == 0 }.map { it * 10 }
    }

    fun validarUsuarios(usuarios: List<Usuario>): List<List<Validacion>> {
        // Mapea cada usuario a una lista de validaciones individuales usando 'it'
        return usuarios.map {
            listOf(
                Validacion("nombre", it.nombre.isNotEmpty(), "El nombre no puede estar vacío"),
                Validacion("email", it.email.contains("@"), "El email debe contener '@'"),
                Validacion("roles", it.roles.isNotEmpty(), "Debe tener al menos un rol")
            )
        }
    }

    fun procesarTextos(textos: List<String>): List<String> {
        // Limpia los espacios, pasa a minúsculas y filtra los que queden vacíos
        return textos.map { it.trim().lowercase() }.filter { it.isNotEmpty() }
    }

    // Parte B: Función run

    fun calcularNivelAcceso(usuario: Usuario): Int {
        // 'run' ejecuta un bloque de código usando el objeto como receptor (this) y retorna el cálculo
        return usuario.run {
            var puntos = 0
            if (activo) puntos += 10
            puntos += roles.size * 5
            if (email.contains("@empresa.com")) puntos += 5
            puntos
        }
    }

    fun crearUsuarioConTipo(tipo: String): Usuario {
        // 'run' nos permite evaluar el tipo y devolver un objeto Usuario completamente configurado
        return tipo.run {
            when (this) {
                "ADMIN" -> Usuario().apply {
                    roles = mutableListOf("ADMIN")
                    configuracion.nivelPrivacidad = 3
                    configuracion.notificaciones = true
                }
                "USER" -> Usuario().apply {
                    roles = mutableListOf("USER")
                    configuracion.nivelPrivacidad = 1
                    configuracion.notificaciones = false
                }
                else -> Usuario()
            }
        }
    }

    // Parte C: Función apply

    fun crearUsuarioCompleto(
        nombre: String,
        email: String,
        roles: List<String>,
    ): Usuario {
        // 'apply' nos permite inicializar y modificar las propiedades del objeto retornándolo automáticamente
        return Usuario().apply {
            this.nombre = nombre
            this.email = email
            this.activo = true
            this.roles = roles.toMutableList()
            this.configuracion = ConfiguracionUsuario()
        }
    }

    fun actualizarUsuario(
        usuario: Usuario,
        actualizacion: Usuario.() -> Unit,
    ): Usuario {
        // Aplica la lambda de extensión recibida por parámetro sobre el usuario usando 'apply'
        return usuario.apply(actualizacion)
    }

    // Parte D: Función also

    fun crearUsuarioConLog(
        nombre: String,
        email: String,
        onLog: (String) -> Unit,
    ): Usuario {
        // 'also' es perfecto para ejecutar efectos secundarios secuenciales (como el logging) sin alterar el flujo de retorno
        return Usuario().also { it.nombre = nombre }
            .also { onLog("Usuario creado: ${it.nombre}") }
            .also { it.email = email }
            .also { onLog("Email asignado: ${it.email}") }
            .also { it.activo = true }
            .also { onLog("Usuario activado") }
    }

    fun crearYValidar(
        nombre: String,
        email: String,
    ): Pair<Usuario, Boolean> {
        var esValido = false
        // Creamos el usuario y aprovechamos 'also' para calcular la validación
        val usuario = Usuario(nombre = nombre, email = email).also {
            esValido = it.nombre.isNotEmpty() && it.email.contains("@")
        }
        return Pair(usuario, esValido)
    }

    // Parte E: Función let

    fun procesarEmailOpcional(email: String?): String {
        // Usamos el operador safe-call junto a 'let' para el flujo condicional de nulos
        return email?.let { "Usuario con email: $it" } ?: "Usuario sin email"
    }

    fun generarMensajesBienvenida(usuarios: List<Usuario>): List<String> {
        // Filtramos bajo las condiciones dadas y mapeamos el String usando 'let'
        return usuarios
            .filter { it.activo && it.email.isNotEmpty() }
            .map { it.let { u -> "Bienvenido/a ${u.nombre} (${u.email})" } }
    }

    // Parte F: Combinación de Scope Functions

    fun procesarUsuarioComplejo(datosBase: Map<String, String>): Usuario? {
        val nombre = datosBase["nombre"] ?: return null
        val email = datosBase["email"] ?: return null
        val departamento = datosBase["departamento"]

        // 2 y 3. Creamos el usuario con 'run' y lo configuramos internamente con 'apply'
        return run {
            Usuario().apply {
                this.nombre = nombre
                this.email = email
            }
        }.let { usuario ->
            // 4 y 5. Si es de "IT", usamos 'also' para añadir configuraciones paralelas
            if (departamento == "IT") {
                usuario.also {
                    it.configuracion.tema = "oscuro"
                    it.roles.add("IT_USER")
                }
            } else {
                usuario
            }
        }
    }

    fun procesarLoteUsuarios(usuarios: List<Usuario>): List<Usuario> {
        return usuarios.map { usuario ->
            usuario
                .apply { activo = true } // 1. Activar todos (apply)
                .also { if (it.roles.isEmpty()) it.roles.add("USER") } // 2. Rol USER si está vacío (also)
                .apply { configuracion.notificaciones = true } // 3. Notificaciones = true (apply)
                .run { // 4. Si el nombre es "Admin", ejecuta configuraciones especiales
                    if (nombre == "Admin") {
                        roles.add("ADMIN")
                        configuracion.nivelPrivacidad = 3
                    }
                    this // Retorna el usuario mutado hacia la lista
                }
        }
    }

    fun parsearYCrearUsuario(datosRaw: String): Usuario? {
        return try {
            // 1. Limpiamos y convertimos la cadena raw en un mapa clave-valor de forma fluida
            val datosMap = datosRaw.trim()
                .split("|")
                .filter { it.contains(":") }
                .associate {
                    val partes = it.split(":")
                    partes[0].trim() to partes[1].trim()
                }

            if (!datosMap.containsKey("nombre") || !datosMap.containsKey("email")) return null

            // 2 y 3. Construimos y configuramos el objeto completo usando las funciones de alcance idóneas
            Usuario().apply {
                id = datosMap["id"]?.toIntOrNull() ?: 0
                nombre = datosMap["nombre"].orEmpty()
                email = datosMap["email"].orEmpty()
                activo = datosMap["activo"]?.toBoolean() ?: false

                datosMap["roles"]?.let { r ->
                    roles = r.split(",").map { it.trim() }.toMutableList()
                }

                configuracion = ConfiguracionUsuario().apply {
                    datosMap["tema"]?.let { tema = it }
                    datosMap["idioma"]?.let { idioma = it }
                }
            }
        } catch (e: Exception) {
            null // 4. Retornar null ante cualquier inconsistencia o fallo de formato
        }
    }
}