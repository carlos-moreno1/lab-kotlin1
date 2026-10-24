package cl.ucm.models

abstract class Aldeano(val id: Int, val nombre: String, val posicion: String) {
    init {
        require(nombre.isNotBlank()) {"Nombre no ingresado."}
        require(posicion.isNotBlank()) {"Posición no ingresada."}
    }
    constructor(id: Int, posicion: String): this(id, "Unknown", posicion)
    
    abstract val status: String
    
    open fun verDatos() {
        println("Nombre: $nombre")
        println("Status: $status")
        println("Posición: $posicion")
    }
}

class Empleado(id: Int, nombre: String, posicion: String, val profesion: String): Aldeano(id, nombre, posicion) {
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
    
    fun verTradeos(){
        if (tradeos.isEmpty()) {
            println("Sin tradeos registrados.")
        }
        tradeos.forEachIndexed { index, tradeo ->
            println("${index + 1}. En venta: $tradeo.cantidadVende ${tradeo.itemVende}. Precio a pagar: $tradeo.cantidadPago ${tradeo.itemPago}.")
        }
    }
}

class Cesante(id: Int, nombre: String, posicion: String): Aldeano(id, nombre, posicion) {
    override val status: String = "Cesante"
}

class Nitwit(id: Int, nombre: String, posicion: String): Aldeano(id, nombre, posicion) {
    override val status: String = "Nitwit"
}