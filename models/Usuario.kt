package cl.ucm.models

/**
 * Clase abstracta que define las propiedades y comportamientos comunes de todos los usuarios del sistema.
 *
 * @property nombre Identificador o nombre de usuario
 * @property password Contraseña de acceso, encapsulada y protegida para las subclases.
 * @property permisos Nivel numerico de privilegios asociado al rol de usuario.
 */
abstract class Usuario(val nombre: String, protected val password: String, val permisos: Int) {
    /**
     * Constructor secundario que crea un usuario asignando una contraseña predefinida.
     *
     * @param nombre Nombre de usuario.
     * @param permisos Nivel numerico de privilegios.
     */
    constructor(nombre: String, permisos: Int) : this(nombre, "123456", permisos)

    /**
     * Verifica si la contraseña coincide con la registrada.
     *
     * @param pass Contraseña a validar.
     * @return 'true' si la contraseña es corecta, 'false' si es incorrecta.
     */

    fun verificarPassword(pass: String): Boolean {
        return (password == pass)
    }

    /**
     * Busca un tradeo especifico en los tradeos disponibles (delega la accion al servidor).
     *
     * @param servidor Instancia del servidor que procesa la busqueda.
     * @param item Nombre del tradeo a buscar.
     */

    fun consultarTradeos(servidor: Servidor, item: String) {
        println("Consultando base de datos...")
        servidor.buscarTradeo(item)
    }

    /**
     * Solicita al servidor mostrar la lista de aldeanos registrados.
     *
     * @param servidor Instancia activa del servidor que contiene la informacion
     */

    fun verMapaAldeanos(servidor: Servidor) {
        println("Mostrando mapa de posiciones...")
        servidor.verListaAldeanos()
    }

    /**
     * Devuelve una cadena con el rol y nivel de permisos del usuario.
     *
     * @return String con el detalle de los privilegios.
     */
    abstract fun mostrarPermisos(): String
}

/**
 * Representa a un usuario básico con nivel de privilegios 1 (solo lectura).
 */
class Noob(nombre: String, password: String): Usuario(nombre, password, 1) {

    /**
     * Constructor secundario que asigna la contraseña predeterminada del nivel noob.
     */
    constructor(nombre: String) : super(nombre, 1)

    override fun mostrarPermisos(): String {
        return "[$nombre - NOOB]: Permisos de solo lectura (Nivel $permisos)"
    }
}

/**
 * Representa a un usuario intermedio con nivel de privilegios 2 (escritura y registro).
 */
class Pro(nombre: String, password: String): Usuario(nombre, password, 2) {

    /**
     * Constructor secundario que asigna la contraseña predeterminada del nivel pro.
     */
    constructor(nombre: String) : super(nombre, 2)

    override fun mostrarPermisos(): String {
        return "[$nombre - PRO]: Permisos de escritura (Nivel $permisos)"
    }

    /**
     * Registra un nuevo aldeano en la base de datos.
     *
     * @param servidor Instancia activa del servidor.
     * @param nombre Nombre del aldeano.
     * @param posicion Ubicacion del aldeano.
     * @param profesion Profesion del aldeano.
     * @return 'true' si el registro fue exitoso, 'false' en caso contrario.
     */
    fun registrarNuevoAldeano(servidor: Servidor, nombre: String, posicion: String, profesion: String): Boolean {
        println("Registrando a $nombre como $profesion...")
        return servidor.agregarAldeanoEmpleado(nombre, posicion, profesion)
    }
}

/**
 * Representa a un administrador con nivel de privilegios 3 (control total).
 */
class Admin(nombre: String, password: String): Usuario(nombre, password, 3) {

    /**
     * Constructor secundario que asigna la contraseña predeterminada del nivel admin.
     */
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
