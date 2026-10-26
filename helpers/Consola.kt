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
        println("\n==========================")
        println("VILLAGER MANAGEMENT SYSTEM")
        println("==========================")
        println("1. Iniciar Sesión")
        println("2. Registrarse")
        println("3. Salir")
        println("==========================")
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
            println(Colores.exito("\nInicio de sesión exitoso."))
            usuarioLogueado
        } else {
            println(Colores.error("\nInicio de sesión incorrecto."))
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
            println(Colores.exito("\nRegistro de usuario exitoso."))
            true
        } else {
            println(Colores.error("\nRegistro incorrecto."))
            false
        }
    }

    /**
     * Imprime las opciones del menú principal correspondientes al nivel de permisos del usuario.
     *
     * @param usuario Instancia del usuario autenticado que define las opciones visibles.
     */
    fun menuPrincipal(usuario: Usuario) {
        println("\n------------------------------")
        println("        MENÚ PRINCIPAL")
        println("------------------------------")
        var n = 1

        println("${n++}. Lista Aldeanos")
        println("${n++}. Ver datos Aldeano")
        println("${n++}. Buscar Tradeo")
        println("${n++}. Tradeos de Aldeano")

        if (usuario is Pro || usuario is Admin) {
            println("------------------------------")
            println("${n++}. Agregar Aldeano")
            println("${n++}. Eliminar Aldeano")
            println("${n++}. Emplear Aldeano")
        }

        if (usuario is Admin) {
            println("------------------------------")
            println("${n++}. Agregar Tradeos")
            println("${n++}. Eliminar Tradeos")
            println("${n++}. Lista Usuarios")
            println("${n++}. Promover Usuario")
            println("${n++}. Unban Usuario")
            println("${n++}. Banear Usuario")
        }

        println("${n}. Cerrar Sesión") // Para volver al menú de login.
        println("------------------------------")
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
                    
                    if (estado.equals("Empleado", ignoreCase = true)) {
                        print("Ingrese profesion: ")
                        if (servidor.agregarAldeanoEmpleado(nombre, pos, readln())) {
                            println(Colores.exito("\nAldeano '$nombre' agregado exitosamente."))
                        }
                    } else {
                        if (servidor.agregarAldeanoNoEmpleado(nombre, pos, estado)) {
                            println(Colores.exito("\nAldeano '$nombre' agregado exitosamente."))
                        }
                    }
                },
                {
                    print("Ingrese ID del aldeano a eliminar: ")
                    val id = readln().toIntOrNull() ?: 0
                    servidor.eliminarAldeano(id)
                },
                {
                    print("Ingrese ID del aldeano cesante: ")
                    val id = readln().toIntOrNull() ?: 0
                    print("Ingrese nueva profesion: ")
                    val prof = readln()
                    
                    servidor.emplearAldeano(id, prof)
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
                    
                    servidor.agregarTradeo(id, pago, cantPago, venta, cantVenta)
                },
                {
                    print("ID del empleado: ")
                    val idE = readln().toIntOrNull() ?: 0
                    print("ID del tradeo a eliminar: ")
                    val idT = readln().toIntOrNull() ?: 0
                    servidor.eliminarTradeo(idE, idT)
                },
                {
                    servidor.mostrarUsuarios()
                },
                {
                    print("Nickname a promover: ")
                    val nicknamePromover = readln()
                    servidor.promoverUsuario(nicknamePromover)
                },
                {
                    print("Nickname para Unban: ")
                    val nick = readln()
                    servidor.desbanearUsuario(nick)
                },
                {
                    print("Nickname a banear: ")
                    val nick = readln()
                    servidor.banearUsuario(nick)
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
                    println(Colores.exito("\nSesión cerrada."))
                    return
                }
                else -> println(Colores.error("\nOpción inválida."))
            }
        }
    }
}
