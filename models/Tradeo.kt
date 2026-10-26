package models

/**
 * Intercambio ofrecido por un [Empleado].
 *
 * @property itemPago Artículo que el empleado recibe como pago.
 * @property cantPago Cantidad recibida; debe ser positiva.
 * @property itemVenta Artículo que ofrece el empleado.
 * @property cantVenta Cantidad ofrecida; debe ser positiva.
 * @throws IllegalArgumentException Si un artículo está vacío o una cantidad no es positiva.
 */
class Tradeo (
    val itemPago: String,
    val cantPago: Int,
    val itemVenta: String,
    val cantVenta: Int
) {
    init {
        require(itemPago.isNotBlank()) { "El artículo de pago no puede estar vacío." }
        require(itemVenta.isNotBlank()) { "El artículo en venta no puede estar vacío." }
        require(cantPago > 0) { "La cantidad del artículo de pago debe ser mayor que cero." }
        require(cantVenta > 0) { "La cantidad del artículo en venta debe ser mayor que cero." }
    }
}