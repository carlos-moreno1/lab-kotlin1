package cl.ucm.helpers
import cl.ucm.models.*
    
class Consola {
    companion object {
        
        fun leerOpcion(): Int {
            val entrada = readlnOrNull() ?: return 0
            print("Elija opción: ")
            return entrada.toIntOrNull()?.takeIf { it in 1..8 } ?: 0
        }
        
        // Menú inicial (Fuera de sesión)
        fun menuLogin() {
            println("\n=== VILLAGER MANAGEMENT SYSTEM ===")
            println("1. Iniciar Sesión")
            println("2. Registrarse")
            println("3. Salir")
            print("Elija opción: ")
        }
        
        fun registrarSesion(): Boolean {
            print("Ingrese usuario: ")
            val usuario = readln()
            print("Ingrese contraseña: ")
            val password = readln()
            print("Confirme contraseña: ")
            val passVer = readln()
            if (password == passVer):
                return if (servidor.register(usuario, password)) {
                    println("Registro de usuario exitoso.")
                    true
                } else {
                    println("Registro de sesión incorrecto.")
                    false
                }
            // Falta
        }
    
        fun inicioSesion(): Boolean {
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
        
        // Menú secundario (Sesión iniciada)(TODOS)    
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

            println("${n}. Cerrar Sesión") // Para volver al menu de login.
            print("Elija opción: ")
        }

        fun sesion(permisos: String) {
        while (true) {
            menuPrincipal(permisos)

            val acciones = mutableListOf<() -> Unit>()

                if (permisos == "Noob") {
                    acciones.add { servidor.verListaAldeanos() }
                    acciones.add { servidor.verDatosAldeano() }
                    acciones.add { servidor.buscarTradeo() }
                }

                if (permisos == "Pro") {
                    acciones.add { servidor.agregarAldeano() }
                    acciones.add { servidor.eliminarAldeano() }
                    acciones.add { servidor.emplearAldeano() }
                }

                if (permisos == "Admin") {
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
}
