package models

/**
 * Datos y comportamiento común de los usuarios.
 *
 * @property nombre Nombre de usuario.
 * @property password Contraseña obligatoria.
 */
abstract class Usuario(val nombre: String, internal val password: String) {
    init {
    require(nombre.isNotBlank()) { "El nombre del usuario no puede estar vacío." }
    }

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
class Noob(nombre: String, password: String): Usuario(nombre, password)

/**
 * Usuario de nivel intermedio.
 */
class Pro(nombre: String, password: String): Usuario(nombre, password)

/**
 * Usuario administrador.
 */
class Admin(nombre: String, password: String): Usuario(nombre, password)

/**
 * Usuario baneado.
 */
class Baneado(nombre: String, password: String): Usuario(nombre, password)
