package cl.ucm.models

abstract class Usuario(val nombre: String, protected val password: String) {
    fun verificarPassword(pass: String): Boolean { return (password == pass) }
}

class Noob(nombre: String, password: String): Usuario(nombre, password) {
    
}

class Pro(nombre: String, password: String): Usuario(nombre, password) {
    
}

class Admin(nombre: String, password: String): Usuario(nombre, password) {
    
}