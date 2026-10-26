package cl.ucm.models

import java.io.File
import java.io.IOException

/**
 * Administra los usuarios y aldeanos registrados, y ofrece operaciones para consultarlos y modificarlos.
 */
class Servidor() {
    private val usuarios: MutableList<Usuario> = mutableListOf()
    private val aldeanos: MutableList<Aldeano> = mutableListOf()

    init {
        usuarios.add(Admin("Notch", "miancra"))
    }

    /**
     * Carga los aldeanos definidos en `data/aldeanos.txt`.
     *
     * El archivo, relativo al directorio de ejecución, debe contener una entrada por línea
     * con los campos separados por comas: estado, nombre, posición y, opcionalmente para
     * empleados, profesión. Si no se indica profesión, se usa `"Librero"`.
     * Las líneas incompletas y los estados no reconocidos se omiten. Si no se puede crear un
     * aldeano con los datos de una línea, se informa por consola. Si el archivo no existe o no
     * se puede leer, también se informa el error.
     */
    fun cargarAldeanos() {
        val archivo = File("data/aldeanos.txt")
        try {
            if (!archivo.exists()) {
                println("Error de lectura: No se encontró el archivo 'data/aldeanos.txt'.")
                return
            }

            for ((indice, linea) in archivo.readLines().withIndex()) {
                val datos = linea.split(",")
                if (datos.size < 3) {
                    println("Error de formato: Se omitió la línea ${indice + 1} de 'data/aldeanos.txt'.")
                    continue
                }

                val tipo = datos[0].trim()
                val nombre = datos[1].trim()
                val posicion = datos[2].trim()

                when (tipo) {
                    "Empleado" -> {
                        val profesion = if (datos.size >= 4) datos[3].trim() else "Librero"
                        agregarAldeanoEmpleado(nombre, posicion, profesion)
                    }
                    "Cesante" -> agregarAldeanoNoEmpleado(nombre, posicion, "Cesante")
                    "Nitwit" -> agregarAldeanoNoEmpleado(nombre, posicion, "Nitwit")
                    else -> {
                        println("Error de formato: Tipo de aldeano desconocido en la línea ${indice + 1} de 'data/aldeanos.txt'.")
                        continue
                    }
                }
            }
            println("Datos iniciales cargados exitosamente desde 'data/aldeanos.txt'.")
        } catch (e: IOException) {
            println("Error de lectura: No se pudo leer 'data/aldeanos.txt': ${e.message}")
        } catch (e: SecurityException) {
            println("Error de acceso: No se pudo acceder a 'data/aldeanos.txt': ${e.message}")
        }
    }

    /**
     * Carga los usuarios definidos en `data/usuarios.txt`.
     *
     * El archivo debe contener una entrada por línea, con los campos separados por comas:
     * tipo, nombre y contraseña. Los tipos válidos son `Noob`, `Pro` y `Baneado`;
     * los administradores no se cargan desde el archivo.
     * Las líneas incompletas, los tipos no reconocidos, los campos vacíos y los nombres ya
     * registrados se omiten.
     * Si el archivo no existe o no se puede leer por falta de permisos, se informa el error por consola.
     */
    fun cargarUsuarios() {
        val archivo = File("data/usuarios.txt")
        try {
            if (!archivo.exists()) {
                println("Error de lectura: No se encontró el archivo 'data/usuarios.txt'.")
                return
            }

            for ((indice, linea) in archivo.readLines().withIndex()) {
                val datos = linea.split(",")
                if (datos.size < 3) {
                    println("Error de formato: Se omitió la línea ${indice + 1} de 'data/usuarios.txt'.")
                    continue
                }

                val tipo = datos[0].trim()
                val nombre = datos[1].trim()
                val password = datos[2].trim()
                if (nombre.isBlank() || password.isBlank()) {
                    println("Error de validación: Nombre o contraseña vacíos en la línea ${indice + 1} de 'data/usuarios.txt'.")
                    continue
                }
                if (usuarios.any { it.nombre == nombre }) continue

                val usuario = when (tipo) {
                    "Noob" -> Noob(nombre, password)
                    "Pro" -> Pro(nombre, password)
                    "Baneado" -> Baneado(nombre, password)
                    "Admin" -> {
                        println("Error de validación: No se permite cargar usuarios de tipo Admin.")
                        null
                    }
                    else -> {
                        println("Error de formato: Tipo de usuario desconocido en la línea ${indice + 1} de 'data/usuarios.txt'.")
                        null
                    }
                }

                if (usuario != null) usuarios.add(usuario)
            }
            println("Usuarios cargados exitosamente desde 'data/usuarios.txt'.")
        } catch (e: IOException) {
            println("Error de lectura: No se pudo leer 'data/usuarios.txt': ${e.message}")
        } catch (e: SecurityException) {
            println("Error de acceso: No se pudo acceder a 'data/usuarios.txt': ${e.message}")
        }
    }

    /**
     * Registra un usuario nuevo si el nombre aún no está en uso.
     *
     * @param nombre Nombre que identificará al usuario.
     * @param password Contraseña del usuario.
     * @param passwordConfirm Confirmación de la contraseña.
     * @return `true` si el usuario se registra; `false` si el nombre ya existe o si contraseña no coincide.
     */
    fun register(nombre: String, password: String, passwordConfirm: String): Boolean {
        if (usuarios.any { it.nombre == nombre }) return false
        if (password != passwordConfirm) return false
        usuarios.add(Noob(nombre, password))
        return true
    }

    /**
     * Valida el intento de inicio de sesión de un usuario registrado.
     *
     * @param nombre Nombre del usuario.
     * @param password Contraseña que se desea verificar.
     * @return El usuario si el inicio de sesión es válido; `null` si no existe, está baneado o la contraseña no coincide.
     */
    fun login(nombre: String, password: String): Usuario? {
        val usuario = usuarios.firstOrNull { it.nombre == nombre } ?: return null
        if (usuario is Baneado || !usuario.verificarPassword(password)) return null
        return usuario
    }

    /**
     * Muestra los usuarios registrados y su tipo por consola.
     *
     * @return `true` si se muestra al menos un usuario; `false` si la lista está vacía.
     */
    fun mostrarUsuarios():Boolean {
        if (usuarios.isEmpty()) return false

        println("Usuarios registrados:")
        usuarios.forEachIndexed { index, usuario ->
            val tipo = when (usuario) {
                is Pro -> "Pro"
                is Admin -> "Admin"
                is Baneado -> "Baneado"
                else -> "Noob"
            }
            println("${index + 1}. ${usuario.nombre} ($tipo)")
        }
        return true
    }
    
    //Menú Principal
    /**
     * Muestra en la consola una vista superficial de los datos de los aldeanos registrados en el sistema.
     *
     * @return `true` si se muestra al menos un aldeano; `false` si la lista está vacía.
     */
    fun verListaAldeanos():Boolean {
        if (aldeanos.isEmpty()) return false
        aldeanos.forEachIndexed { index, aldeano ->
            println("Id: ${index + 1}. Nombre: ${aldeano.nombre}. Status: ${aldeano.status}.")
        }
        return true
    }

    /**
     * Muestra los datos del aldeano correspondiente al identificador indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @return `true` si existe el aldeano; `false` si el identificador no es válido.
     */
    fun verDatosAldeano(id: Int): Boolean {
        if (id < 1) return false
        val aldeano = aldeanos.getOrNull(id - 1) ?: return false
        aldeano.verDatos()
        return true
    }

    /**
     * Muestra los intercambios del aldeano empleado correspondiente al identificador indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @return `true` si el identificador corresponde a un empleado; `false` en caso contrario.
     */
    fun verTradeosAldeano(id: Int): Boolean {
        if (id < 1) return false
        val aldeano = aldeanos.getOrNull(id - 1) as? Empleado ?: return false
        aldeano.verTradeos()
        return true
    }
    
    /**
     * Busca y muestra todos los empleados que venden el artículo indicado.
     *
     * @param item Artículo que se busca en los intercambios.
     * @return `true` si al menos un empleado vende un artículo con el nombre exacto indicado;
     * `false` si no hay coincidencias.
     */
    fun buscarTradeo(item: String): Boolean {
        var encontrado = false

        for ((index, aldeano) in aldeanos.withIndex()) {
            val empleado = aldeano as? Empleado ?: continue
            val tradeosEncontrados = empleado.obtenerTradeo().filter { it.itemVenta == item }
            if (tradeosEncontrados.isEmpty()) continue

            encontrado = true
            println("${index + 1}. ${empleado.nombre} (Ubicado en: ${empleado.posicion}):")
            tradeosEncontrados.forEach { tradeo ->
                println(" Vende: ${tradeo.cantVenta} ${tradeo.itemVenta}")
                println(" Precio: ${tradeo.cantPago} ${tradeo.itemPago}")
            }
        }

        if (!encontrado) {
            println("No hay aldeanos que vendan $item.")
            return false
        }
        return true
    }
    
    /**
     * Muestra los empleados que tienen la profesión indicada y su ubicación.
     *
     * @param profesion Profesión que se busca.
     * @return `true` si se muestra al menos un empleado; `false` si no hay coincidencias.
     */
    fun buscarProfesion(profesion: String): Boolean {
        var encontrado = false
        for ((index, aldeano) in aldeanos.withIndex()) {
            val empleado = aldeano as? Empleado ?: continue
            if (empleado.profesion != profesion) continue

            println("${index + 1}. ${empleado.nombre} (Ubicado en: ${empleado.posicion})")
            encontrado = true
        }

        if (!encontrado) {
            println("No hay aldeanos con la profesión $profesion.")
            return false
        }
        return true
    }
    
    /**
     * Crea y registra un aldeano empleado.
     *
     * @param nombre Nombre del aldeano.
     * @param posicion Posición del aldeano.
     * @param profesion Profesión del aldeano.
     * @return `true` si se registra el aldeano; `false` si alguno de sus datos no es válido.
     */
    fun agregarAldeanoEmpleado(nombre: String, posicion: String, profesion: String): Boolean {
        try {
            aldeanos.add(Empleado(nombre, posicion, profesion))
            return true    
        }
        catch (e: IllegalArgumentException) {
            println("Error de validación: ${e.message ?: "Datos de aldeano inválidos."}")
            return false
        }
    }

    /**
     * Crea y registra un aldeano sin empleo según el estado indicado.
     *
     * @param nombre Nombre del aldeano.
     * @param posicion Posición del aldeano.
     * @param status Estado del aldeano; debe ser `"Cesante"` o `"Nitwit"`.
     * @return `true` si se registra el aldeano; `false` si el estado o alguno de sus datos no es válido.
     */
    fun agregarAldeanoNoEmpleado(nombre: String, posicion: String, status: String): Boolean {
        try {
            when (status) {
                "Cesante" -> aldeanos.add(Cesante(nombre, posicion))
                "Nitwit" -> aldeanos.add(Nitwit(nombre, posicion))
                else -> {
                    println("Error de validación: El estado del aldeano no es válido.")
                    return false
                }
            }
            return true    
        }
        catch (e: IllegalArgumentException) {
            println("Error de validación: ${e.message ?: "Datos de aldeano inválidos."}")
            return false
        }
    }

    /**
     * Cambia a empleado al aldeano cesante indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @param profesion Profesión que se le asignará al aldeano.
     * @return `true` si el aldeano cesante fue empleado; `false` si el identificador no es válido, no corresponde a un cesante o la profesión no es válida.
     */
    fun emplearAldeano(id: Int, profesion: String): Boolean {
        if (id < 1) return false
        val indice = id - 1
        val original = aldeanos.getOrNull(indice) ?: return false
        if (original !is Cesante) return false // Verifica que cumpla el requisito para ser empleado

        return try {
            aldeanos[indice] = Empleado(original.nombre, original.posicion, profesion)
            true
        } catch (e: IllegalArgumentException) {
            println("Error de validación: ${e.message ?: "Datos de aldeano inválidos."}")
            false
        }
    }

    /**
     * Elimina de la lista al aldeano con el identificador indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @return `true` si se eliminó el aldeano; `false` si el identificador no es válido.
     */
    fun eliminarAldeano(id: Int): Boolean {
        if (id < 1) return false
        val indice = id - 1
        if (indice !in aldeanos.indices) return false
        aldeanos.removeAt(indice)
        return true
    }
    
    /**
     * Registra un intercambio en el empleado indicado.
     *
     * @param idAldeano Identificador del aldeano empleado, comenzando en 1.
     * @param itemPago Artículo que se recibe como pago.
     * @param cantPago Cantidad del artículo de pago.
     * @param itemVenta Artículo que ofrece el empleado.
     * @param cantVenta Cantidad del artículo ofrecido.
     * @return `true` si el intercambio se agregó; `false` si el empleado o los datos no son válidos.
     */
    fun agregarTradeo(
        idAldeano: Int,
        itemPago: String,
        cantPago: Int,
        itemVenta: String,
        cantVenta: Int
    ): Boolean {
        val empleado = aldeanos.getOrNull(idAldeano - 1) as? Empleado
        if (empleado == null) {
            println("Error: No se encontró un empleado con el identificador $idAldeano.")
            return false
        }

        return empleado.agregarTradeo(itemPago, cantPago, itemVenta, cantVenta)
    }

    /**
     * Elimina del empleado indicado el intercambio con el identificador proporcionado.
     *
     * @param idAldeano Identificador del aldeano empleado, comenzando en 1.
     * @param idTradeo Identificador del intercambio, comenzando en 1.
     * @return `true` si el intercambio se eliminó; `false` si los identificadores no son válidos.
     */
    fun eliminarTradeo(idAldeano: Int, idTradeo: Int): Boolean {
        val empleado = aldeanos.getOrNull(idAldeano - 1) as? Empleado
        if (empleado == null) {
            println("Error: No se encontró un empleado con el identificador $idAldeano.")
            return false
        }

        if (!empleado.eliminarTradeo(idTradeo)) {
            println("Error: No se encontró el intercambio con el identificador $idTradeo.")
            return false
        }
        return true
    }

    /**
     * Promueve a un usuario de tipo [Noob] a [Pro], conservando sus datos de acceso.
     *
     * @param nombre Nombre del usuario que se desea promover.
     * @return `true` si el usuario existe y es [Noob]; `false` en caso contrario.
     */
    fun promoverUsuario(nombre: String): Boolean {
        val indice = usuarios.indexOfFirst { it.nombre == nombre }
        if (indice == -1) return false

        val usuario = usuarios[indice]
        if (usuario !is Noob) return false

        usuarios[indice] = Pro(usuario.nombre, usuario.password)
        return true
    }

    /**
     * Convierte en [Baneado] al usuario indicado, excepto si es [Admin].
     *
     * @param nombre Nombre del usuario que se desea banear.
     * @return `true` si el usuario existe y no es [Admin]; `false` si no existe o es administrador.
     */
    fun banearUsuario(nombre: String): Boolean {
        val indice = usuarios.indexOfFirst { it.nombre == nombre }
        if (indice == -1) {
            println("Error: No se encontró el usuario '$nombre'.")
            return false
        }

        val usuario = usuarios[indice]
        if (usuario is Admin) return false

        usuarios[indice] = Baneado(usuario.nombre, usuario.password)
        println("Usuario $nombre baneado exitosamente.")
        return true
    }

    /**
     * Desbanea al usuario indicado y lo convierte en [Noob].
     *
     * @param nombre Nombre del usuario que se desea desbanear.
     * @return `true` si el usuario existe y es [Baneado]; `false` en caso contrario.
     */
    fun desbanearUsuario(nombre: String): Boolean {
        val indice = usuarios.indexOfFirst { it.nombre == nombre }
        if (indice == -1) return false

        val usuario = usuarios[indice]
        if (usuario !is Baneado) return false

        usuarios[indice] = Noob(usuario.nombre, usuario.password)
        return true
    }
}
