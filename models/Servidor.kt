package cl.ucm.models

class Servidor() { // Volver Object singleton
    private val usuarios: MutableList<Usuario> = mutableListOf()
    private val aldeanos: MutableList<Aldeano> = mutableListOf()
    
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
    
    //Menú Principal (Acceso TODOS)
    fun verListaAldeanos():Boolean {
        if (aldeanos.isEmpty()) return false
        
        println("Aldeanos registrados:")
        aldeanos.forEachIndexed { index, aldeano ->
            println("\n${index + 1}.")
            aldeano.verDatos()
        }

        return true
    }
    fun verDatosAldeano(val nombre, val status) {
        if (aldeanos.isEmpty()) return false
        if (aldeanos.any { it.nombre == nombre }) return false

    }
    fun buscarTradeo()
    fun cerrarSesion()
    
    //Menú Pro (y Admin)
    fun agregarAldeano()
    fun emplearAldeano()
    fun eliminarAldeano()
    
    //Menú Admin
    fun agregarTradeo()
    fun eliminarTradeo()
    fun promoverUsuario()
    fun banearUsuario()
    
}

// Hacer que Cesante pueda pasar a Empleado (copiar datos, borrar instancia de Cesante y crear una de Empleado con los mismo datos + nueva profesión)