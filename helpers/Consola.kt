package helpers
import models.*

/**
 * Interfaz de consola para el "Villager Management System".
 *
 * Gestiona los menús, la captura de entrada estándar y la presentación de mensajes, 
 * delegando la ejecución de la lógica de negocio y validación al [Servidor].
 */
object Consola {
    /**
     * Imprime el menú de inicio para usuarios no autenticados.
     */
    fun menuLogin() {
        println("\n=== VILLAGER MANAGEMENT SYSTEM ===")
        println("1. Iniciar Sesión")
        println("2. Registrarse")
        println("3. Salir")
        println("==================================")
    }

    /**
     * Captura y valida una opción numérica ingresada por el usuario.
     *
     * @return La opción elegida, o `0` si la entrada es inválida o nula.
     */
    fun leerOpcion(): Int {
        print("Elija opción: ")
        val entrada = readlnOrNull() ?: return 0
        return entrada.toIntOrNull()?.takeIf { it > 0 } ?: 0
    }

    /**
     * Solicita credenciales por teclado y autentica al usuario en el sistema.
     *
     * @param servidor Instancia del servidor que procesa la validación.
     * @return El [Usuario] si las credenciales son válidas; `null` si son incorrectas o el usuario está baneado.
     */
    fun inicioSesion(servidor: Servidor): Usuario? {
        print("Ingrese usuario: ")
        val usuario = readln()
        print("Ingrese contraseña: ")
        val password = readln()

        val usuarioLogueado = servidor.login(usuario, password)
        return if (usuarioLogueado != null) {
            println("\nInicio de sesión exitoso.")
            usuarioLogueado
        } else {
            println("\nInicio de sesión incorrecto.")
            null
        }
    }

    /**
     * Solicita datos por teclado e intenta registrar un nuevo usuario en el sistema.
     *
     * @param servidor Instancia del servidor que procesa el registro.
     * @return `true` si el registro es exitoso; `false` si el nombre ya existe o las contraseñas no coinciden.
     */
    fun registrarSesion(servidor: Servidor): Boolean {
        print("Ingrese usuario: ")
        val usuario = readln()
        print("Ingrese contraseña: ")
        val password = readln()
        print("Confirme contraseña: ")
        val passwordConfirm = readln()

        return if (servidor.register(usuario, password, passwordConfirm)) {
            println("\nRegistro de usuario exitoso.")
            true
        } else {
            println("\nRegistro incorrecto. El usuario ya existe o las contraseñas no coinciden.")
            false
        }
    }

    /**
     * Imprime las opciones del menú principal correspondientes al nivel de permisos del usuario.
     *
     * @param usuario Instancia del usuario autenticado que define las opciones visibles.
     */
    fun menuPrincipal(usuario: Usuario) {
        println("\n--- MENÚ PRINCIPAL ---")
        var n = 1

        println("${n++}. Lista Aldeanos")
        println("${n++}. Ver datos Aldeano")
        println("${n++}. Buscar Tradeo")
        println("${n++}. Tradeos de Aldeano")

        if (usuario is Pro || usuario is Admin) {
            println("${n++}. Agregar Aldeano")
            println("${n++}. Eliminar Aldeano")
            println("${n++}. Emplear Aldeano")
        }

        if (usuario is Admin) {
            println("${n++}. Agregar Tradeos")
            println("${n++}. Eliminar Tradeos")
            println("${n++}. Lista Usuarios")
            println("${n++}. Promover Usuario")
            println("${n++}. Unban Usuario")
            println("${n++}. Banear Usuario")
        }

        println("${n}. Cerrar Sesión") // Para volver al menú de login.
        println("----------------------")
    }

    /**
     * Inicia el bucle interactivo de la sesión del usuario.
     *
     * Captura las entradas necesarias (IDs, profesiones, ítems) y evalúa los retornos 
     * booleanos del [Servidor] para emitir mensajes de éxito o error, evitando duplicar 
     * salidas que la lógica interna ya imprime.
     *
     * @param servidor Instancia del servidor.
     * @param usuario Usuario autenticado cuyos permisos dictan las acciones disponibles.
     */
    fun sesion(servidor: Servidor, usuario: Usuario) {
        while (true) {
            menuPrincipal(usuario)

            val acciones = mutableListOf<() -> Unit>()

            val accionesNoob = listOf<() -> Unit>(
                { 
                    servidor.verListaAldeanos() 
                },
                {
                    print("Ingrese ID del aldeano: ")
                    val id = readln().toIntOrNull() ?: 0
                    servidor.verDatosAldeano(id)
                },
                {
                    print("Ingrese el item a buscar: ")
                    val item = readln()
                    servidor.buscarTradeo(item)
                },
                {
                    print("Ingrese ID del aldeano: ")
                    val id = readln().toIntOrNull() ?: 0
                    servidor.verTradeosAldeano(id)
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
                    servidor.mostrarUsuarios()
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
                    print("Nickname para Unban: ")
                    val nick = readln()
                    if (servidor.desbanearUsuario(nick)) {
                        println("El usuario '$nick' se le realizo Unban con éxito.")
                    } else {
                        println("Error: No se pudo realizar Unban al usuario '$nick'.")
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
