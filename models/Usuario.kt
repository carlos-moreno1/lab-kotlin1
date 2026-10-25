package cl.ucm.models

abstract class Usuario(val nombre: String, protected val password: String, val permisos: Int) {
    constructor(nombre: String, permisos: Int) : this(nombre, "123456", permisos) //permite crear usuario solo con un nombre y asigna contraseña por defecto

    fun verificarPassword(pass: String): Boolean {
        return (password == pass)
    }

    fun consultarTradeos(servidor: Servidor, item: String) {
        println("Consultando base de datos...")
        servidor.buscarTradeo(item)
    }

    fun verMapaAldeanos(servidor: Servidor) {
        println("Mostrando mapa de posiciones...")
        servidor.verListaAldeanos()
    }

    abstract fun mostrarPermisos(): String
}

class Noob(nombre: String, password: String): Usuario(nombre, password, 1) {
    constructor(nombre: String) : super(nombre, 1)

    override fun mostrarPermisos(): String {
        return "[$nombre - NOOB]: Permisos de solo lectura (Nivel $permisos)"
    }
}

class Pro(nombre: String, password: String): Usuario(nombre, password, 2) {
    constructor(nombre: String) : super(nombre, 2)

    override fun mostrarPermisos(): String {
        return "[$nombre - PRO]: Permisos de escritura (Nivel $permisos)"
    }

    fun registrarNuevoAldeano(servidor: Servidor, nombre: String, posicion: String, profesion: String): Boolean {
        println("Registrando a $nombre como $profesion...")
        return servidor.agregarAldeanoEmpleado(nombre, posicion, profesion)
    }
}

class Admin(nombre: String, password: String): Usuario(nombre, password, 3) {
    constructor(nombre: String) : super(nombre, 3)

    override fun mostrarPermisos(): String {
        return "[$nombre - ADMIN]: Control Total (Nivel $permisos)"
    }

    fun expulsarUsuario(servidor: Servidor, nickname: String): Boolean {
        println("Expulsando al usuario $nickname")
        return servidor.banearUsuario(nickname)
    }

    fun mostrarDatos(servidor: Servidor): String {
        println("Obteniendo registros del servidor...")
        servidor.mostrarUsuarios()
        return "Consulta de datos finalizada"
    }
}
