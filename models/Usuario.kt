package models

/**
 * Clase abstracta que define las propiedades y comportamientos comunes de todos los usuarios del sistema.
 *
 * @property nombre nombre de usuario
 * @property password Contraseña de acceso.
 */
abstract class Usuario(val nombre: String, internal val password: String) {
    /**
     * Constructor secundario que crea un usuario asignando una contraseña predefinida.
     *
     * @param nombre Nombre de usuario.
     */
    constructor(nombre: String) : this(nombre, "123456")

    /**
     * Verifica si la contraseña coincide con la registrada.
     *
     * @param pass Contraseña a validar.
     * @return 'true' si la contraseña es corecta, 'false' si es incorrecta.
     */

    fun verificarPassword(pass: String): Boolean {
        return (password == pass)
    }

}

/**
 * Representa a un usuario básico.
 */
class Noob(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Constructor secundario que asigna la contraseña predeterminada del nivel noob.
     */
    constructor(nombre: String) : super(nombre)
}

/**
 * Representa a un usuario intermedio.
 */
class Pro(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Constructor secundario que asigna la contraseña predeterminada del nivel pro.
     */
    constructor(nombre: String) : super(nombre)
}

/**
 * Representa a un administrador.
 */
class Admin(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Constructor secundario que asigna la contraseña predeterminada del nivel admin.
     */
    constructor(nombre: String) : super(nombre)
}

/**
 * Representa a un usuario baneado.
 */
class Baneado(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Constructor secundario que asigna la contraseña predeterminada.
     */
    constructor(nombre: String) : super(nombre)
}
