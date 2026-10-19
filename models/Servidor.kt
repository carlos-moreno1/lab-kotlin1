package cl.ucm.models

class Servidor() { // Volver Object singleton
    private val aldeanos: MutableList<Aldeano> = mutableListOf()
    private val usuarios: MutableList<Usuario> = mutableListOf()
    
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
                is Noob -> "Noob"
                is Pro -> "Pro"
                is Admin -> "Admin"
                else -> "Usuario"
            }
            println("${index + 1}. ${usuario.nombre} ($tipo)")
        }
        return true
    }
    
    //Menú Principal
    fun verDatosAldeano()
    fun buscarTradeo()
    fun cerrarSesion()
    
    //Menú Pro
    fun agregarAldeano()
    fun modificarTradeos()
    fun emplearAldeano()
    fun eliminarAldeano()
    
    //Menú Admin
    fun ascender()
    fun banear()
    
}

// Hacer que Cesante pueda pasar a Empleado (copiar datos, borrar instancia de Cesante y crear una de Empleado con los mismo datos + nueva profesión)