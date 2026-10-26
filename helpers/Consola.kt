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
     * @return La opción elegida, o `0` si la entrada es inválida, nula o no positiva.
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
     * @return El [Usuario] si las credenciales son válidas; `null` si no existe, la contraseña es incorrecta o está baneado.
     */
    fun inicioSesion(servidor: Servidor): Usuario? {
        print("Ingrese usuario: ")
        val usuario = readln()
        print("Ingrese contraseña: ")
        val password = readln()

        val usuarioLogueado = servidor.login(usuario, password)
        return if (usuarioLogueado != null) usuarioLogueado else null
    }

    /**
     * Solicita datos por teclado e intenta registrar un nuevo usuario en el sistema.
     *
     * @param servidor Instancia del servidor que procesa el registro.
     * @return `true` si el registro es exitoso; `false` si el nombre ya existe o está vacío, o las contraseñas no coinciden.
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

        println("${(n++).toString().padStart(2)}. Lista Aldeanos")
        println("${(n++).toString().padStart(2)}. Ver datos Aldeano")
        println("${(n++).toString().padStart(2)}. Buscar Tradeo")
        println("${(n++).toString().padStart(2)}. Tradeos de Aldeano")

        if (usuario is Pro || usuario is Admin) {
            println("------------------------------")
            println("${(n++).toString().padStart(2)}. Agregar Aldeano")
            println("${(n++).toString().padStart(2)}. Eliminar Aldeano")
            println("${(n++).toString().padStart(2)}. Emplear Aldeano")
        }

        if (usuario is Admin) {
            println("------------------------------")
            println("${(n++).toString().padStart(2)}. Agregar Tradeos")
            println("${(n++).toString().padStart(2)}. Eliminar Tradeos")
            println("${(n++).toString().padStart(2)}. Lista Usuarios")
            println("${(n++).toString().padStart(2)}. Promover Usuario")
            println("${(n++).toString().padStart(2)}. Unban Usuario")
            println("${(n++).toString().padStart(2)}. Banear Usuario")
        }

        println("${n.toString().padStart(2)}. Cerrar Sesión") // Para volver al menú de login.
        println("------------------------------")
    }

    /**
     * Muestra las opciones según los permisos del usuario y ejecuta las acciones elegidas.
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
                    print("Item que vende el aldeano: ")
                    val venta = readln()
                    print("Cantidad del item en venta: ")
                    val cantVenta = readln().toIntOrNull() ?: 0
                    print("Item a pagar al aldeano: ")
                    val pago = readln()
                    print("Cantidad de pago: ")
                    val cantPago = readln().toIntOrNull() ?: 0
                    
                    
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
