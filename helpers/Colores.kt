package helpers

object Colores {
    private const val ROJO = "\u001B[31m"
    private const val VERDE = "\u001B[32m"
    private const val REINICIAR = "\u001B[0m"

    fun error(mensaje: String): String = "$ROJO$mensaje$REINICIAR"

    fun exito(mensaje: String): String = "$VERDE$mensaje$REINICIAR"
}
