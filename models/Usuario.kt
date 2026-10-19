package cl.ucm.models

abstract class Usuario(val nombre: String, protected val password: String) {
    constructor(nombre: String) : this(nombre, "123456") //permite crear usuario solo con un nombre y asigna contraseña por defecto

    fun verificarPassword(pass: String): Boolean {
        return (password == pass)
    }

    fun consultarTradeos() {
        println("Consultando base de datos...")
        //LLAMADA AL SERVIDOR
    }

    fun verMapaAldeanos() {
        println("Mostrando mapa de posiciones...")
        //LLAMADA
    }

    abstract fun mostrarPermisos(): String
}

class Noob(nombre: String, password: String): Usuario(nombre, password) { 
    constructor(nombre: String) : super(nombre)

    override fun mostrarPermisos(): String {
        return "[$nombre - NOOB]: Permisos de solo lectura"
    }
}

class Pro(nombre: String, password: String): Usuario(nombre, password) {
    constructor(nombre: String) : super(nombre)

    override fun mostrarPermisos(): String {
        return "[$nombre - PRO]: Permisos de escritura"
    }

    fun registrarNuevoAldeano(nombre: String): Boolean {
        println("Registrando a $nombre")
        return true //LOGICA PENDIENTE
    }
}

class Admin(nombre: String, password: String): Usuario(nombre, password) {
    constructor(nombre: String) : super(nombre)

    override fun mostrarPermisos(): String {
        return "[$nombre - ADMIN]: Control Total"
    }

    fun expulsarUsuario(nickname: String) {
        println("Expulsando al usuario $nickname")
        //LOGICA PENDIENTE
    }

    fun mostrarDatos(): String {
        return "Mostrando datos..."
    }
}
