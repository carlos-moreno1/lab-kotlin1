import helpers.*
import models.*

// GRUPO: BENJAMÍN MUÑOZ BALBOA, CARLOS ROJAS VALDÉS, CARLOS MORENO ROJAS, MANUEL ASTUDILLO AYALA.

/**
 * Punto de entrada principal de la aplicación "Villager Management System".
 *
 * Inicializa la instancia central del [Servidor], carga los datos iniciales de 
 * usuarios y aldeanos desde los archivos de texto, y ejecuta el bucle principal 
 * del menú de inicio delegando la captura de opciones e interacciones a la [Consola].
 */
fun main() {
    val servidor = Servidor()
    
    // Carga de datos iniciales desde los archivos .txt
    servidor.cargarUsuarios()
    servidor.cargarAldeanos()
    
    var opcion = 0

    do {
        Consola.menuLogin()
        opcion = Consola.leerOpcion()
        
        when(opcion) {
            1 -> {
                val usuarioLogueado = Consola.inicioSesion(servidor)
                if (usuarioLogueado != null) {
                    Consola.sesion(servidor, usuarioLogueado)
                }
            }
            2 -> {
                Consola.registrarSesion(servidor)
            }
            3 -> {
                println("Cerrando programa...")
            }
            else -> {
                println("Seleccione una opción válida.")
            }
        }
    } while(opcion != 3)
}