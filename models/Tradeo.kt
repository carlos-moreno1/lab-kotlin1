package cl.ucm.models

/**
 * Representa un intercambio entre un artículo de pago y uno ofrecido en venta.
 *
 * @property itemPago Nombre del artículo que se entrega como pago.
 * @property cantPago Cantidad del artículo de pago; debe ser mayor que cero.
 * @property itemVenta Nombre del artículo que se ofrece en venta.
 * @property cantVenta Cantidad del artículo ofrecido; debe ser mayor que cero.
 */
class Tradeo (
    val itemPago: String,
    val cantPago: Int,
    val itemVenta: String,
    val cantVenta: Int
) {
    init {
        require(itemPago.isNotBlank()) { "Item de pago inválido" }
        require(itemVenta.isNotBlank()) { "Item en venta inválido" }
        require(cantPago > 0) { "Cantidad de item de pago inválida" }
        require(cantVenta > 0) { "Cantidad de item en venta inválida" }
    }
}