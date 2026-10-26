import helpers.*
import models.*


// GRUPO: BENJAMÍN MUÑOZ BALBOA, CARLOS ROJAS VALDÉS, CARLOS MORENO ROJAS, MANUEL ASTUDILLO AYALA.

fun main () {
    
    val servidor = Servidor()
    var opcion: int
    var sesionIniciada = false

    do{
        Consola.menuLogin()                                                          // muestra el menu
        opcion= Consola.leerOpcion()                                                 //seleccionamos una opcion
        when(Opcion){                                                                //respuestas a la opcion elegida  
            1-> {
                sesionIniciada = Consola.inicioSesion(servidor)
                if (sesionIniciada ){
                    Consola.sesion(servidor, 1)                                     //creo que era asi favor revisar            /*Ejecuta el inicio de sesion y abre el menu principal */
                }
            }
            2->{
                Consola.registrarSesion(servidor)                                   //registra un nuevo usuario
            }
            3->{
                println("Cerrando programa")
            }
            else->{
                println("selecciona una opcion valida")
            }
        }
    } while(opcion != 3)                                             //el ciclo termina cuando se escoge la opcion de salida
 
}