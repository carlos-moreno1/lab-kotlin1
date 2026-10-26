import cl.ucm.helpers.*
import cl.ucm.models.*


// GRUPO: BENJAMÍN MUÑOZ BALBOA, CARLOS ROJAS VALDÉS, CARLOS MORENO ROJAS, MANUEL ASTUDILLO AYALA.

fun main () {
    
    val servidor = Servidor()
    var opcion: int
    var sesionIniciada = false

    do{
        Consola.menuLogin() // muestra el menu
        opcion= Consola.leerOpcion()                                                 //seleccionamos una opcion
        when(Opcion){                                                                //respuestas a la opcion elegida  
            1-> {
                sesionIniciada = Consola.inicioSesion(servidor)
                if (sesionIniciada ){
                    Consola.sesion(servidor, 1)                                     //creo que era asi favor revisar            /*Ejecuta el inicio de sesion y abre el menu principal */
                }
            }
            2->{
                Consola.registrarSesion(servidor)
            }
            3->{
                println("Cerrando programa")
            }
            else->{
                println("selecciona una opcion valida")
            }
        }
    } while(opcion != 3)                                             //el ciclo termina cuando se escoge la opcion de salida
    /*  
    val Consola = Consola()
    val Opcion = Consola.leerOpcion()

   

    val Aldeano = Aldeano()    
        val Empleado = Empleado()
                val tradeo = Tradeo()
        val Cesante = Cesante()
        val Nitwit = Nitwit()
    val Usuario= Usuario()





    Consola.menuLogin()
    Consola.inicioSesion()
    Consola.menuPrincipal()






    //Menú Principal
    verDatosAldeano()
    buscarTradeo()
    cerrarSesion()
    
    //Menú Pro
    agregarAldeano()
    modificarTradeos()
    emplearAldeano()
    eliminarAldeano()
    
    //Menú Admin
    ascender()
    banear()
     */

}