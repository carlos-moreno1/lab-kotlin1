package cl.ucm.helpers
import cl.ucm.models.*

/**
 * Interfaz de consola de "Villager Management System".
 *
 * Concentra los menús, la lectura por teclado y el despacho de acciones hacia
 * [servidor]. Implementada como un Singleton (object).
 */
object Consola {
    /**
     * Imprime el menú de inicio (fuera de sesión).
     */
    fun menuLogin() {
        println("\n=== VILLAGER MANAGEMENT SYSTEM ===")
        println("1. Iniciar Sesión")
        println("2. Registrarse")
        println("3. Salir")
    }

    /**
     * Lee una opción del menú desde la entrada estándar.
     *
     * Descarta entradas no numéricas o fuera de rango.
     *
     * @return la opción elegida, o `0` si la entrada es inválida o se cerró el flujo.
     */
    fun leerOpcion(): Int {
        print("Elija opción: ")
        val entrada = readlnOrNull() ?: return 0
        return entrada.toIntOrNull()?.takeIf { it in 1..11 } ?: 0
    }

    
    /**
     * Autentica al usuario en el [servidor].
     *
     * @return `true` si las credenciales son válidas.
     */
    fun inicioSesion(servidor: Servidor): Boolean {
        print("Ingrese usuario: ")
        val usuario = readln()
        print("Ingrese contraseña: ")
        val password = readln()

        return if (servidor.login(usuario, password)) {
            println("Inicio de sesión exitoso.")
            true
        } else {
            println("Inicio de sesión incorrecto.")
            false
        }
    }

    /**
     * Pide usuario y contraseña dos veces y registra la cuenta en [servidor].
     *
     * @return `true` si el registro fue exitoso; `false` si las contraseñas no
     *   coinciden o el usuario ya existe.
     * @see inicioSesion
     */
    fun registrarSesion(servidor: Servidor): Boolean {
        print("Ingrese usuario: ")
        val usuario = readln()
        print("Ingrese contraseña: ")
        val password = readln()
        print("Confirme contraseña: ")
        val passwordConfirm = readln()

        return if (servidor.register(usuario, password, passwordConfirm)) {
            println("Registro de usuario exitoso.")
            true
        } else {
            println("Registro incorrecto. El usuario ya existe o las contraseñas no coinciden.")
            false
        }
    }


    /**
     * Imprime el menú principal mostrando solo las opciones habilitadas.
     *
     * @param permisos nivel de acceso: "1" = Noob, "2" = Pro, "3" = Admin.
     */
    fun menuPrincipal(permisos: Int) {
        println("\n--- MENÚ PRINCIPAL ---")
        var n = 1

        if (permisos >= 1) {
            println("${n++}. Ver Aldeanos")
            println("${n++}. Ver datos Aldeanos")
            println("${n++}. Buscar Intercambio")
        }

        if (permisos >= 2) {
            println("${n++}. Agregar Aldeano")
            println("${n++}. Eliminar Aldeano")
            println("${n++}. Emplear Aldeano")
        }

        if (permisos >= 3) {
            println("${n++}. Agregar Tradeos")
            println("${n++}. Eliminar Tradeos")
            println("${n++}. Promover Usuario")
            println("${n++}. Banear Usuario")
        }

        println("${n}. Cerrar Sesión") // Para volver al menú de login.
        print("Elija opción: ")
    }

    /**
     * Ejecuta el bucle de sesión hasta que el usuario elige «Cerrar Sesión».
     *
     * @param permisos nivel de acceso: "1" = Noob, "2" = Pro, "3" = Admin.
     */
    fun sesion(servidor: Servidor, permisos: Int) {
        while (true) {
            menuPrincipal(permisos)

            val acciones = mutableListOf<() -> Unit>()

            if (permisos >= 1) {
                acciones.add { servidor.verListaAldeanos() }
                acciones.add { servidor.verDatosAldeano() }
                acciones.add { servidor.buscarTradeo() }
            }

            if (permisos >= 2) {
                acciones.add { servidor.agregarAldeano() }
                acciones.add { servidor.eliminarAldeano() }
                acciones.add { servidor.emplearAldeano() }
            }

            if (permisos >= 3) {
                acciones.add { servidor.agregarTradeo() }
                acciones.add { servidor.eliminarTradeo() }
                acciones.add { servidor.promoverUsuario() }
                acciones.add { servidor.banearUsuario() }
            }

            when (val opcion = leerOpcion()) {
                in 1..acciones.size -> acciones[opcion - 1]()
                acciones.size + 1 -> {
                    println("Sesión cerrada.")
                    return
                }
                else -> println("Opción inválida.")
            }
        }
    }
}
