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
    fun verTradeosAldeano() {
        //Muestra lista de tradeos completa
    }
    fun buscarTradeo() {
        //Definir 3 casos según lo que se reciba: solo ItemPago, solo ItemVende o ambos
        //Tal vez devidir en 3 funciones diferentes
    }
    fun cerrarSesion() {
        //Llevar a menú de login
    }
    
    //Menú Pro y Admin
    fun agregarAldeano() {
        // Antes de agregar verifica que el nombre no se repita
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