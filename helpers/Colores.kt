package helpers

/**
 * Aplica colores a los mensajes de consola.
 */
object Colores {
    private const val ROJO = "\u001B[31m"
    private const val VERDE = "\u001B[32m"
    private const val REINICIAR = "\u001B[0m"

    /**
     * Devuelve el mensaje en rojo.
     *
     * @param mensaje Texto que se colorea.
     * @return Mensaje con formato rojo.
     */
    fun error(mensaje: String): String = "$ROJO$mensaje$REINICIAR"

    /**
     * Devuelve el mensaje en verde.
     *
     * @param mensaje Texto que se colorea.
     * @return Mensaje con formato verde.
     */
    fun exito(mensaje: String): String = "$VERDE$mensaje$REINICIAR"
}
