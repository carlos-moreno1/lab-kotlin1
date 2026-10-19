package cl.ucm.models

/*
 * IDEA:
 * Hacer lista con ITEMS posibles de cada aldeano segun su profesion
 * Cada ITEM tiene un código arbitrario que lo identifica
 * Se puede ver una lista con estos codigos y se solicita al usuario indicar código + cantidades
 * la lista de items seria abstract protected val (no mutable)
 * la función agregar tradeo debe verificar código válido
 * PD: polimorfismo se verá mas en clases Usuario, aqui incluso podría usarse solo Aldeano ya que lo único que cambia es profesion y nada más
 */


abstract class Aldeano(val nombre: String?, val posicion: String) {
    init {
        require(posicion.isNotBlank()) {"Posición no ingresada."}
    }
    constructor(posicion: String): this(null, posicion)
    
    private val id: Int = 0 // Luego hacer una lista con IDs para asignar la sgte. libre
    abstract val status: String
}

class Empleado(
    nombre: String?,
    posicion: String,
    val profesion: String
) : Aldeano(nombre, posicion) {
    override val status: String = "Empleado"
    
    private val tradeos: MutableList<Tradeo> = mutableListOf()
    fun obtenerTradeo(): List<Tradeo> = tradeos.toList()
    fun agregarTradeo(
        itemPago: String,
        cantidadPago: Int,
        itemVende: String,
        cantidadVende: Int,
    ): Boolean {
        try {
            tradeos.add(Tradeo(itemPago, cantidadPago, itemVende, cantidadVende))
            return true
        } catch (error: IllegalArgumentException) {
            println("No se pudo agregar el intercambio: ${error.message}")
            return false
        }
    }
}

class Cesante(
    nombre: String?,
    posicion: String,
) : Aldeano(nombre, posicion) {
    override val status: String = "Cesante"
}

class Nitwit(
    nombre: String?,
    posicion: String,
) : Aldeano(nombre, posicion) {
    override val status: String = "Nitwit"
}