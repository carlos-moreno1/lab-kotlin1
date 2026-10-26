package models

import helpers.Colores

/**
 * Aldeano registrado en el sistema.
 *
 * @property nombre Nombre del aldeano.
 * @property posicion Ubicación del aldeano.
 * @throws IllegalArgumentException Si el nombre o la posición están vacíos.
 */
abstract class Aldeano(val nombre: String, posicion: String) {
    var posicion: String = posicion
        private set

    init {
        require(nombre.isNotBlank()) { "El nombre del aldeano no puede estar vacío." }
        require(posicion.isNotBlank()) { "La posición del aldeano no puede estar vacía." }
    }
    
    abstract val status: String

    /**
     * Cambia la posición del aldeano.
     *
     * @param nuevaPosicion Nueva posición, no vacía.
     * @return `true` si se actualiza; `false` si está vacía.
     */
    fun cambiarPosicion(nuevaPosicion: String): Boolean {
        if (nuevaPosicion.isBlank()) return false
        posicion = nuevaPosicion
        return true
    }
    
    /**
     * Muestra nombre, estado y posición.
     */
    open fun verDatos() {
         println(Colores.exito("\nDATOS ALDEANO"))
         println(Colores.exito("-------------"))
         println(Colores.exito("Nombre: $nombre"))
         println(Colores.exito("Status: $status"))
         println(Colores.exito("Posición: $posicion"))
    }
}

/**
 * Aldeano con profesión e intercambios.
 * 
 * @property profesion Profesión del aldeano.
 * @throws IllegalArgumentException Si el nombre o la posición están vacíos.
 */
class Empleado(nombre: String, posicion: String, val profesion: String): Aldeano(nombre, posicion) {
    override val status: String = "Empleado"
    private val tradeos: MutableList<Tradeo> = mutableListOf()
    
    /**
     * Muestra los datos del aldeano y su profesión.
     */
    override fun verDatos(): kotlin.Unit {
        super.verDatos()
        println(Colores.exito("Profesión: $profesion"))
    }
    
    /**
     * Devuelve una copia de los intercambios registrados.
     *
     * @return Lista de intercambios del empleado.
     */
    fun obtenerTradeo(): List<Tradeo> = tradeos.toList()
    
    /**
     * Agrega un intercambio al empleado.
     *
     * @param itemPago Artículo a pagar.
     * @param cantPago Cantidad del item a pagar.
     * @param itemVenta Artículo ofrecido por el aldeano.
     * @param cantVenta Cantidad del item ofrecido.
     * @return `true` si se agrega; `false` si los datos no son válidos.
     */
    fun agregarTradeo(
        itemPago: String,
        cantPago: Int,
        itemVenta: String,
        cantVenta: Int,
    ): Boolean {
        try {
            tradeos.add(Tradeo(itemPago, cantPago, itemVenta, cantVenta))
            return true
        } catch (error: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${error.message ?: "Datos de intercambio inválidos."}"))
            return false
        }
    }

    /**
     * Elimina un intercambio por su identificador.
     *
     * @param id Identificador del intercambio.
     * @return `true` si se elimina; `false` si el identificador no es válido.
     */
    fun eliminarTradeo(id: Int): Boolean {
        if (id !in 1..tradeos.size) return false
        tradeos.removeAt(id - 1)
        return true
    }
    
    /**
     * Muestra los intercambios registrados o indica si no hay.
     */
    fun verTradeos(){
        if (tradeos.isEmpty()) {
            println(Colores.error("\nSin tradeos registrados."))
            return
        }
        println(Colores.exito("\nTRADEOS ENCONTRADOS"))
        println(Colores.exito("--------------------"))
        tradeos.forEachIndexed { index, tradeo ->
            println(Colores.exito("${(index + 1).toString().padStart(2)} | En venta: ${tradeo.cantVenta} ${tradeo.itemVenta}. Precio a pagar: ${tradeo.cantPago} ${tradeo.itemPago}."))
        }
    }
}

/**
 * Aldeano sin empleo que puede adquirir una profesión.
 */
class Cesante(nombre: String, posicion: String): Aldeano(nombre, posicion) {
    override val status: String = "Cesante"
}

/**
 * Aldeano sin empleo que no puede adquirir una profesión.
 */
class Nitwit(nombre: String, posicion: String): Aldeano(nombre, posicion) {
    override val status: String = "Nitwit"
}