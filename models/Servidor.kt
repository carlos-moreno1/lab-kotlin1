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
    
    fun buscarTradeo(item: String): Boolean {
        // Buscar item deseado (que vende aldeano)
    }
    
    fun buscarProfesion(profesion: String): Boolean {
        // Imprime lista con datos de aldeanos con la profesión indicada
    }
    
    //Menú Pro y Admin
    
    /*Separar para dos casos generales: Empleado y NO empleado*/
    fun agregarAldeanoEmpleado(nombre: String, posicion: String, profesion: String): Boolean {
        try {
            aldeanos.add(Empleado(nombre, posicion, profesion))
            return true    
        }
        catch (e: IllegalArgumentException) {
            return false
        }
    }
    fun agregarAldeanoNoEmpleado(nombre: String, posicion: String, status: String): Boolean {
        try {
            when (status) {
                "Cesante" -> aldeanos.add(Cesante(nombre, posicion))
                "Nitwit" -> aldeanos.add(Nitwit(nombre, posicion))
                else -> return false
            }
            return true    
        }
        catch (e: IllegalArgumentException) {
            return false
        }
    }
    fun emplearAldeano(indice: Int, profesion: String): Boolean {
        val original = aldeanos.getOrNull(indice) ?: return false
        if (original !is Cesante) return false // Verifica que cumpla el requisito para ser empleado

        aldeanos[indice] = Empleado(original.nombre, original.posicion, profesion) // Reemplaza instancia por otra con mismos datos + profesión
        return true
    }
    fun eliminarAldeano(indice: Int): Boolean {
        if (indice !in aldeanos.indices) return false
        aldeanos.removeAt(indice)
        return true
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
