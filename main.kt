import helpers.*
import models.*

// GRUPO: BENJAMÍN MUÑOZ BALBOA, CARLOS ROJAS VALDÉS, CARLOS MORENO ROJAS, MANUEL ASTUDILLO AYALA.
//
// COMANDO COMPILACIÓN (Windows):
// kotlinc .\main.kt .\helpers\Consola.kt .\helpers\Colores.kt .\models\Aldeano.kt .\models\Servidor.kt .\models\Tradeo.kt .\models\Usuario.kt -include-runtime -d .\villager-management.jar
//
// COMANDO COMPILACIÓN (Linux y macOS):
// kotlinc ./main.kt ./helpers/*.kt ./models/*.kt -include-runtime -d ./villager-management.jar
// 
// COMANDO EJECUCIÓN (Windows, Linux y macOS):
// java -jar .\villager-management.jar
    
/**
 * Punto de entrada principal del "Villager Management System".
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
                println("Cerrando programa...\n")
            }
            else -> {
                println(Colores.error("Seleccione una opción válida."))
            }
        }
    } while(opcion != 3)
}