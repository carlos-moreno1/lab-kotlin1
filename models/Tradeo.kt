package cl.ucm.models

/**
 * Representa un intercambio entre un artículo de pago y uno ofrecido en venta.
 *
 * @property itemPago Nombre del artículo que se entrega como pago.
 * @property cantPago Cantidad del artículo de pago; debe ser mayor que cero.
 * @property itemVenta Nombre del artículo que se ofrece en venta.
 * @property cantVenta Cantidad del artículo ofrecido; debe ser mayor que cero.
 * @throws IllegalArgumentException Si los nombres de los artículos están vacíos o alguna cantidad no es positiva.
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