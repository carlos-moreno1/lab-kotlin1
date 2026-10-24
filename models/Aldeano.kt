package cl.ucm.models

/**
 * Representa a un aldeano dentro del sistema.
 *
 * @property nombre Nombre del aldeano.
 * @property posicion Ubicación o posición del aldeano dentro del juego.
 */
abstract class Aldeano(val nombre: String, val posicion: String) {
    init {
        require(nombre.isNotBlank()) {"Nombre no ingresado."}
        require(posicion.isNotBlank()) {"Posición no ingresada."}
    }
    
    abstract val status: String
    
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
            println("No se pudo agregar el intercambio: ${error.message}")
            return false
        }
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