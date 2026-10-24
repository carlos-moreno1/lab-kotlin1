package cl.ucm.models

class Servidor() { // Volver Object singleton
    private val usuarios: MutableList<Usuario> = mutableListOf()
    private val aldeanos: MutableList<Aldeano> = mutableListOf() // Considerar hacer 3 listas según status y una lista con esas 3 listas
    
    //Menú LogIn
    fun register(nombre: String, password: String): Boolean {
        if (usuarios.any { it.nombre == nombre }) return false
        usuarios.add(Noob(nombre, password))
        return true
    }
    fun login(nombre: String, password: String): Boolean {
        val usuario = usuarios.firstOrNull { it.nombre == nombre } ?: return false
        return usuario.verificarPassword(password)
    }
    fun mostrarUsuarios():Boolean {
        if (usuarios.isEmpty()) return false

        println("Usuarios registrados:")
        usuarios.forEachIndexed { index, usuario ->
            val tipo = when (usuario) {
                is Pro -> "Pro"
                is Admin -> "Admin"
                else -> "Noob"
            }
            println("${index + 1}. ${usuario.nombre} ($tipo)")
        }
        return true
    }
    
    //Menú Principal
    fun verListaAldeanos():Boolean {
        if (aldeanos.isEmpty()) return false
        aldeanos.forEachIndexed { index, aldeano ->
            println("${index + 1}. Nombre: ${aldeano.nombre}. Status: ${aldeano.status}.")
        }
        return true
    }
    fun verDatosAldeano(nombre: String): Boolean {
        val aldeano = aldeanos.firstOrNull { it.nombre == nombre } ?: return false
        aldeano.verDatos()
        return true
    }
    fun verTradeosAldeano(nombre: String): Boolean {
        val aldeano = aldeanos.firstOrNull { it.nombre == nombre && it.status == "Empleado"} ?: return false
        aldeano.verTradeos()
        return true
    }
    
    /*CONSIDERAR REPLANTEAR PARA HACER MÁS SENCILLA LA IMPLEMENTACIÓN Y UTIL PARA USUARIO*/
    fun buscarTradeo() {
        // Buscar por item deseado
        // Buscar segun profesion (crear otra funcion aparte)
    }
    
    fun cerrarSesion() {
        //Llevar a menú de login
        
        //Considerar añadir atributo a clase suario de status = "Online" o "Offline"
        //La consola usaria ese atributo para algunas verificaciones
        //Ver bien factibilidad y utilidad antes de implementar
    }
    
    //Menú Pro y Admin
    
    /*Separar para dos casos generales: Empleado y NO empleado*/
    fun agregarAldeano(nombre: String, posicion: String, status: String): Boolean {
        if (aldeanos.any {it.nombre == nombre}) return false
        try {
            
            
            return true    
        }
        catch (e: IllegalArgumentException) {
            
            return false
        }
        
        
    }
    fun emplearAldeano() {
        // Verifica si es cesante. Copia sus datos. Elimina la instancia actual de la lista y crea otra con los mismos datos + profesión
    }
    fun eliminarAldeano() {
        // Elimina de la lista
    }
    
    //Menú Admin
    fun agregarTradeo()
    fun eliminarTradeo()
    fun promoverUsuario() {
        // Solo de Noob a Pro
    }
    fun banearUsuario() {
        // Elimina de la lista de usuarios
        // Considerar hacer lista de baneados y que no se pueda registrar nadie con ese nombre (opcional)
    }
    
}
