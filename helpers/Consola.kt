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
    fun inicioSesion(servidor: Servidor): Usuario? {
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
     * @param usuario El objeto que representa al usuario autenticado y su rol.
     */
    fun menuPrincipal(usuario: Usuario) {
        println("\n--- MENÚ PRINCIPAL ---")
        var n = 1

        println("${n++}. Ver Aldeanos")
        println("${n++}. Ver datos Aldeanos")
        println("${n++}. Buscar Intercambio")

        if (usuario is Pro || usuario is Admin) {
            println("${n++}. Agregar Aldeano")
            println("${n++}. Eliminar Aldeano")
            println("${n++}. Emplear Aldeano")
        }

        if (usuario is Admin) {
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
     * @param servidor La instancia del servidor.
     * @param usuario El objeto que representa al usuario autenticado y su rol ([Admin], [Pro] o [Noob]).
     */
    fun sesion(servidor: Servidor, usuario: Usuario) {
        while (true) {
            menuPrincipal(usuario)

            val acciones = mutableListOf<() -> Unit>()

            val accionesNoob = listOf<() -> Unit>(
                { servidor.verListaAldeanos() },
                {
                    print("Ingrese ID del aldeano: ")
                    val id = readln().toIntOrNull() ?: 0
                    servidor.verDatosAldeano(id)
                },
                {
                    print("Ingrese el item a buscar: ")
                    val item = readln()
                    servidor.buscarTradeo(item)
                }
            )

            val accionesPro = listOf<() -> Unit>(
                {
                    print("Ingrese nombre: ")
                    val nombre = readln()
                    print("Ingrese posicion: ")
                    val pos = readln()
                    print("Ingrese estado: ")
                    val estado = readln()
                    
                    val exito = if (estado.equals("Empleado", ignoreCase = true)) {
                        print("Ingrese profesion: ")
                        servidor.agregarAldeanoEmpleado(nombre, pos, readln())
                    } else {
                        servidor.agregarAldeanoNoEmpleado(nombre, pos, estado)
                    }
                    
                    if (exito) {
                        println("Aldeano '$nombre' agregado exitosamente.")
                    } else {
                        println("Error: No se pudo agregar al aldeano.")
                    }
                },
                {
                    print("Ingrese ID del aldeano a eliminar: ")
                    val id = readln().toIntOrNull() ?: 0
                    if (servidor.eliminarAldeano(id)) {
                        println("Aldeano con ID $id eliminado exitosamente.")
                    } else {
                        println("Error: No se pudo eliminar al aldeano. Verifique que el ID sea correcto.")
                    }
                },
                {
                    print("Ingrese ID del aldeano cesante: ")
                    val id = readln().toIntOrNull() ?: 0
                    print("Ingrese nueva profesion: ")
                    val prof = readln()
                    if (servidor.emplearAldeano(id, prof)) {
                        println("Aldeano con ID $id empleado exitosamente como $prof.")
                    } else {
                        println("Error: No se pudo emplear al aldeano. Verifique el ID o su estado actual.")
                    }
                }
            )

            val accionesAdmin = listOf<() -> Unit>(
                {
                    print("ID del empleado: ")
                    val id = readln().toIntOrNull() ?: 0
                    print("Item de pago: ")
                    val pago = readln()
                    print("Cantidad de pago: ")
                    val cantPago = readln().toIntOrNull() ?: 0
                    print("Item en venta: ")
                    val venta = readln()
                    print("Cantidad en venta: ")
                    val cantVenta = readln().toIntOrNull() ?: 0
                    
                    if (servidor.agregarTradeo(id, pago, cantPago, venta, cantVenta)) {
                        println("Tradeo agregado exitosamente al aldeano con ID $id.")
                    } else {
                        println("Error: No se pudo agregar el tradeo. Verifique los datos ingresados.")
                    }
                },
                {
                    print("ID del empleado: ")
                    val idE = readln().toIntOrNull() ?: 0
                    print("ID del tradeo a eliminar: ")
                    val idT = readln().toIntOrNull() ?: 0
                    
                    if (servidor.eliminarTradeo(idE, idT)) {
                        println("Tradeo con ID $idT eliminado exitosamente del aldeano $idE.")
                    } else {
                        println("Error: No se pudo eliminar el tradeo.")
                    }
                },
                {
                    print("Nickname a promover: ")
                    val nicknamePromover = readln()
                    if (servidor.promoverUsuario(nicknamePromover)) {
                        println("El usuario '$nicknamePromover' fue promovido exitosamente.")
                    } else {
                        println("Error: No se pudo promover al usuario '$nicknamePromover'.")
                    }
                },
                {
                    print("Nickname a banear: ")
                    val nick = readln()
                    if (servidor.banearUsuario(nick)) {
                        println("El usuario '$nick' fue BANEADO con éxito.")
                    } else {
                        println("Error: No se pudo banear al usuario '$nick'.")
                    }
                }
            )

            when (usuario) {
                is Admin -> {
                    acciones.addAll(accionesNoob)
                    acciones.addAll(accionesPro)
                    acciones.addAll(accionesAdmin)
                }
                is Pro -> {
                    acciones.addAll(accionesNoob)
                    acciones.addAll(accionesPro)
                }
                is Noob -> {
                    acciones.addAll(accionesNoob)
                }
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
