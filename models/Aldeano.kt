package models

/**
 * Representa a un aldeano dentro del sistema.
 *
 * @property nombre Nombre del aldeano.
 * @property posicion Ubicación o posición del aldeano dentro del juego.
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
     * Actualiza la posición del aldeano.
     *
     * @param nuevaPosicion Nueva posición; no puede estar vacía ni contener solo espacios.
     * @return `true` si se actualizó; `false` si la posición no es válida.
     */
    fun cambiarPosicion(nuevaPosicion: String): Boolean {
        if (nuevaPosicion.isBlank()) return false
        posicion = nuevaPosicion
        return true
    }
    
    /**
     * Muestra por consola el nombre, el estado y la posición del aldeano.
     */
    open fun verDatos() {
        println("Nombre: $nombre")
        println("Status: $status")
        println("Posición: $posicion")
    }
}

/**
 * Representa a un aldeano que cuenta con profesión y puede registrar intercambios.
 * 
 * @property profesion Profesión que indica la especialidad del aldeano.
 * @throws IllegalArgumentException Si el nombre o la posición están vacíos.
 */
class Empleado(nombre: String, posicion: String, val profesion: String): Aldeano(nombre, posicion) {
    override val status: String = "Empleado"
    private val tradeos: MutableList<Tradeo> = mutableListOf()
    
    /**
     * Muestra los datos del aldeano y su profesión por consola.
     */
    override fun verDatos(): kotlin.Unit {
        super.verDatos()
        println("Profesión: $profesion")
    }
    
    /**
     * Obtiene una copia de los intercambios registrados.
     *
     * @return Lista de intercambios del empleado.
     */
    fun obtenerTradeo(): List<Tradeo> = tradeos.toList()
    
    /**
     * Registra un intercambio ofrecido por el empleado.
     *
     * @param itemPago Artículo que se recibe como pago.
     * @param cantPago Cantidad del artículo de pago.
     * @param itemVenta Artículo que ofrece el empleado.
     * @param cantVenta Cantidad del artículo ofrecido.
     * @return `true` si el intercambio se registra; `false` si sus datos no son válidos.
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
            println("Error de validación: ${error.message ?: "Datos de intercambio inválidos."}")
            return false
        }
    }

    /**
     * Elimina un intercambio usando el identificador mostrado al usuario, comenzando en 1.
     *
     * @param id Identificador del intercambio en la lista.
     * @return `true` si se eliminó el intercambio; `false` si el identificador no es válido.
     */
    fun eliminarTradeo(id: Int): Boolean {
        if (id !in 1..tradeos.size) return false
        tradeos.removeAt(id - 1)
        return true
    }
    
    /**
     * Muestra por consola los intercambios registrados o indica si no hay ninguno.
     */
    fun verTradeos(){
        if (tradeos.isEmpty()) {
            println("Sin tradeos registrados.")
        }
        tradeos.forEachIndexed { index, tradeo ->
            println("${index + 1}. En venta: ${tradeo.cantVenta} ${tradeo.itemVenta}. Precio a pagar: ${tradeo.cantPago} ${tradeo.itemPago}.")
        }
    }
}

/**
 * Representa a un aldeano sin empleo pero que tiene la capacidad de adquirir uno.
 */
class Cesante(nombre: String, posicion: String): Aldeano(nombre, posicion) {
    override val status: String = "Cesante"
}

/**
 * Representa a un aldeano sin empleo que NO tiene la capacidad de adquirir uno.
 */
class Nitwit(nombre: String, posicion: String): Aldeano(nombre, posicion) {
    override val status: String = "Nitwit"
}