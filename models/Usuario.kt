package models

/**
 * Datos y comportamiento común de los usuarios.
 *
 * @property nombre Nombre de usuario.
 * @property password Contraseña.
 */
abstract class Usuario(val nombre: String, internal val password: String) {
    init {
    require(nombre.isNotBlank()) { "El nombre del usuario no puede estar vacío." }
    }
    
    /**
     * Crea un usuario con la contraseña predeterminada (`123456`).
     *
     * @param nombre Nombre de usuario.
     */
    constructor(nombre: String) : this(nombre, "123456")

    /**
     * Comprueba la contraseña.
     *
     * @param pass Contraseña ingresada.
     * @return `true` si coincide; `false` en caso contrario.
     */

    fun verificarPassword(pass: String): Boolean {
        return (password == pass)
    }

}

/**
 * Usuario básico.
 */
class Noob(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Crea un usuario con la contraseña predeterminada (`123456`).
     */
    constructor(nombre: String) : this(nombre, "123456")
}

/**
 * Usuario de nivel intermedio.
 */
class Pro(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Crea un usuario con la contraseña predeterminada (`123456`).
     */
    constructor(nombre: String) : this(nombre, "123456")
}

/**
 * Usuario administrador.
 */
class Admin(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Crea un administrador con la contraseña predeterminada (`123456`).
     */
    constructor(nombre: String) : this(nombre, "123456")
}

/**
 * Usuario baneado.
 */
class Baneado(nombre: String, password: String): Usuario(nombre, password) {
    /**
     * Crea un usuario baneado con la contraseña predeterminada (`123456`).
     */
    constructor(nombre: String) : this(nombre, "123456")
}
