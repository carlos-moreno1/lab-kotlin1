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
    fun mostrarUsuarios() {
        
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