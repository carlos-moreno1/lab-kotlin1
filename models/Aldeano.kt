package cl.ucm.models

// Considerar hacer todas las variables de datos private o protected y que se entreguen a traves de una funcion
abstract class Aldeano(val nombre: String?, val posicion: String) {
    init {
        require(posicion.isNotBlank()) {"Posición no ingresada."}
    }
    constructor(posicion: String): this(null, posicion)
    
    private val id: Int = 0 // Luego hacer una lista con IDs para asignar la sgte. libre
    abstract val status: String
    
    open fun verDatos() {
        println("ID: $id")
        println("Nombre: $nombre")
        println("Status: $status")
        println("Posición: $posicion")
    }
}

class Empleado(nombre: String?, posicion: String, val profesion: String): Aldeano(nombre, posicion) {
    override val status: String = "Empleado"
    private val tradeos: MutableList<Tradeo> = mutableListOf()
    
    override fun verDatos(): kotlin.Unit {
        super.verDatos()
        println("Profesión: $profesion")
    }
    
    fun obtenerTradeo(): List<Tradeo> = tradeos.toList()
    
    // Ver manera de acortar longitud de variables
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

class Cesante(nombre: String?, posicion: String): Aldeano(nombre, posicion) {
    override val status: String = "Cesante"
}

class Nitwit(nombre: String?, posicion: String): Aldeano(nombre, posicion) {
    override val status: String = "Nitwit"
}