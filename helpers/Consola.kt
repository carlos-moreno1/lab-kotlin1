package cl.ucm.helpers
import cl.ucm.models.*
    
    

    

    
    
class Consola {
    companion object {
        
        fun leerOpcion(): Int {
            return readln().toIntOrNull() ?: 0
        }
        
        // Menú inicial (Fuera de sesión)
        fun menuLogin() {
            println("\n=== VILLAGER MANAGEMENT SYSTEM ===")
            println("1. Iniciar Sesión")
            println("2. Salir")
            print("Elija opción: ")
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
        
        // Menú secundario (Sesión iniciada)
        fun menuPrincipal() {
            println("\n--- MENÚ PRINCIPAL ---")
            println("1. Mostrar datos")
            println("2. Ver datos Aldeanos")
            println("3. Agregar Aldeano")
            println("4. Modificar Tradeos")
            println("5. Emplear Aldeano")
            println("6. Eliminar Aldeano")
            println("7. Buscar intercambio")
            println("8. Cerrar Sesión")
            print("Elija opción: ")
        }


    }
}