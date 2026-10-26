package models

import helpers.Colores
import java.io.File
import java.io.IOException

/**
 * Gestiona usuarios y aldeanos registrados. Incluye un administrador inicial.
 */
class Servidor() {
    private val usuarios: MutableList<Usuario> = mutableListOf()
    private val aldeanos: MutableList<Aldeano> = mutableListOf()

    init {
        usuarios.add(Admin("Notch", "maincra"))
    }

    /**
     * Carga aldeanos desde `data/aldeanos.txt`.
     *
     * Lee tipo, nombre, posición y profesión. Si un empleado no especifica profesión,
     * usa `"Librero"`.
     */
    fun cargarAldeanos() {
        val archivo = File("data/aldeanos.txt")
        try {
            if (!archivo.exists()) {
                println(Colores.error("\nError de lectura: No se encontró el archivo 'data/aldeanos.txt'."))
                return
            }

            for ((indice, linea) in archivo.readLines().withIndex()) {
                val datos = linea.split(",")
                if (datos.size < 3) {
                    println(Colores.error("\nError de formato: Se omitió la línea ${indice + 1} de 'data/aldeanos.txt'."))
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
                        println(Colores.error("\nError de formato: Tipo de aldeano desconocido en la línea ${indice + 1} de 'data/aldeanos.txt'."))
                        continue
                    }
                }
            }
            println(Colores.exito("\nDatos iniciales cargados exitosamente desde 'data/aldeanos.txt'."))
        } catch (e: IOException) {
            println(Colores.error("\nError de lectura: No se pudo leer 'data/aldeanos.txt': ${e.message}"))
        } catch (e: SecurityException) {
            println(Colores.error("\nError de acceso: No se pudo acceder a 'data/aldeanos.txt': ${e.message}"))
        }
    }

    /**
     * Carga usuarios `Noob`, `Pro` y `Baneado` desde `data/usuarios.txt`.
     *
     * Omite los nombres duplicados y no permite cargar usuarios `Admin`.
     */
    fun cargarUsuarios() {
        val archivo = File("data/usuarios.txt")
        try {
            if (!archivo.exists()) {
                println(Colores.error("\nError de lectura: No se encontró el archivo 'data/usuarios.txt'."))
                return
            }

            for ((indice, linea) in archivo.readLines().withIndex()) {
                val datos = linea.split(",")
                if (datos.size < 3) {
                    println(Colores.error("\nError de formato: Se omitió la línea ${indice + 1} de 'data/usuarios.txt'."))
                    continue
                }

                val tipo = datos[0].trim()
                val nombre = datos[1].trim()
                val password = datos[2].trim()
                if (nombre.isBlank() || password.isBlank()) {
                    println(Colores.error("\nError de validación: Nombre o contraseña vacíos en la línea ${indice + 1} de 'data/usuarios.txt'."))
                    continue
                }
                if (usuarios.any { it.nombre == nombre }) continue

                val usuario = try {
                    when (tipo) {
                        "Noob" -> Noob(nombre, password)
                        "Pro" -> Pro(nombre, password)
                        "Baneado" -> Baneado(nombre, password)
                        "Admin" -> {
                            println(Colores.error("\nError de validación: No se permite cargar usuarios de tipo Admin."))
                            null
                        }
                        else -> {
                            println(Colores.error("\nError de formato: Tipo de usuario desconocido en la línea ${indice + 1} de 'data/usuarios.txt'."))
                            null
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    println(Colores.error("\nError de validación en la línea ${indice + 1} de 'data/usuarios.txt': ${e.message ?: "Datos de usuario inválidos."}"))
                    continue
                }

                if (usuario != null) usuarios.add(usuario)
            }
            println(Colores.exito("\nUsuarios cargados exitosamente desde 'data/usuarios.txt'."))
        } catch (e: IOException) {
            println(Colores.error("\nError de lectura: No se pudo leer 'data/usuarios.txt': ${e.message}"))
        } catch (e: SecurityException) {
            println(Colores.error("\nError de acceso: No se pudo acceder a 'data/usuarios.txt': ${e.message}"))
        }
    }

    /**
     * Registra un usuario nuevo de tipo [Noob].
     *
     * @param nombre Nombre de usuario.
     * @param password Contraseña.
     * @param passwordConfirm Confirmación de la contraseña.
     * @return `true` si se registra; `false` si el nombre está vacío o repetido, o las contraseñas no coinciden.
     */
    fun register(nombre: String, password: String, passwordConfirm: String): Boolean {
        if (usuarios.any { it.nombre == nombre }) return false
        if (password != passwordConfirm) return false
        return try {
            usuarios.add(Noob(nombre, password))
            true
        } catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de usuario inválidos."}"))
            false
        }
    }

    /**
     * Inicia sesión si las credenciales son válidas y el usuario no está baneado.
     *
     * @param nombre Nombre de usuario.
     * @param password Contraseña.
     * @return El usuario autenticado o `null` si no existe, la contraseña es incorrecta o está baneado.
     */
    fun login(nombre: String, password: String): Usuario? {
        val usuario = usuarios.firstOrNull { it.nombre == nombre } ?: run {
            println(Colores.error("\nError: Usuario $nombre no registrado."))
            return null
        }

        if (usuario is Baneado) {
            println(Colores.error("\nError: Usuario $nombre está baneado."))
            return null
        }
        if (!usuario.verificarPassword(password)) {
            println(Colores.error("\nError: Contraseña incorrecta."))
            return null
        }

        println(Colores.exito("\nInicio de sesión exitoso."))
        return usuario
    }

    /**
     * Muestra el id, nombre y tipo de los usuarios registrados.
     *
     * @return `true` si hay usuarios; `false` si la lista está vacía.
     */
    fun mostrarUsuarios():Boolean {
        if (usuarios.isEmpty()) return false

        println(Colores.exito("\nUSUARIOS REGISTRADOS"))
        println(Colores.exito("--------------------"))
        usuarios.forEachIndexed { index, usuario ->
            val tipo = when (usuario) {
                is Pro -> "Pro"
                is Admin -> "Admin"
                is Baneado -> "Baneado"
                else -> "Noob"
            }
            println(Colores.exito("${index + 1}. ${usuario.nombre} ($tipo)"))
        }
        return true
    }
    
    /**
     * Muestra el id, nombre y estado de cada aldeano.
     *
     * @return `true` si hay aldeanos; `false` si la lista está vacía.
     */
    fun verListaAldeanos():Boolean {
        if (aldeanos.isEmpty()) return false
        println(Colores.exito("\nALDEANOS REGISTRADOS"))
        println(Colores.exito("--------------------"))
        aldeanos.forEachIndexed { index, aldeano ->
            println(Colores.exito("Id: ${index + 1}. Nombre: ${aldeano.nombre}. Status: ${aldeano.status}."))
        }
        return true
    }

    /**
     * Muestra los datos del aldeano indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @return `true` si existe; `false` si el identificador no es válido.
     */
    fun verDatosAldeano(id: Int): Boolean {
        val aldeano = aldeanos.getOrNull(id - 1)
        if (aldeano == null) {
            println(Colores.error("\nError: No existe un aldeano con el ID $id."))
            return false
        }

        aldeano.verDatos()
        return true
    }

    /**
     * Muestra los intercambios del empleado indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @return `true` si corresponde a un empleado; `false` en caso contrario.
     */
    fun verTradeosAldeano(id: Int): Boolean {
        val aldeano = aldeanos.getOrNull(id - 1) as? Empleado
        if (aldeano == null) {
            println(Colores.error("\nError: El aldeano indicado no existe o no es un empleado."))
            return false
        }

        aldeano.verTradeos()
        return true
    }
    
    /**
     * Muestra todos los empleados que venden el artículo indicado.
     *
     * @param item Nombre exacto del artículo.
     * @return `true` si hay coincidencias; `false` en caso contrario.
     */
    fun buscarTradeo(item: String): Boolean {
        val coincidencias = aldeanos.withIndex().mapNotNull { (index, aldeano) ->
            (aldeano as? Empleado)?.let { empleado ->
                val tradeos = empleado.obtenerTradeo().filter { it.itemVenta == item }
                if (tradeos.isEmpty()) null else Triple(index, empleado, tradeos)
            }
        }

        if (coincidencias.isEmpty()) {
            println(Colores.error("\nNo hay aldeanos que vendan $item."))
            return false
        }

        println(Colores.exito("\nALDEANOS ENCONTRADOS"))
        println(Colores.exito("--------------------"))
        for ((index, empleado, tradeos) in coincidencias) {
            println(Colores.exito("\n${empleado.nombre} (ID: ${index + 1})"))
            tradeos.forEach { tradeo ->
                println(Colores.exito("\tVende: ${tradeo.cantVenta} ${tradeo.itemVenta}"))
                println(Colores.exito("\tPrecio: ${tradeo.cantPago} ${tradeo.itemPago}"))
            }
        }

        return true
    }
    
    /**
     * Crea y registra un empleado.
     *
     * @param nombre Nombre del aldeano.
     * @param posicion Posición del aldeano.
     * @param profesion Profesión asignada.
     * @return `true` si se registra; `false` si el nombre, la posición o la profesión no son válidos.
     */
    fun agregarAldeanoEmpleado(nombre: String, posicion: String, profesion: String): Boolean {
        try {
            aldeanos.add(Empleado(nombre, posicion, profesion))
            return true    
        }
        catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de aldeano inválidos."}"))
            return false
        }
    }

    /**
     * Crea y registra un aldeano cesante o nitwit.
     *
     * @param nombre Nombre del aldeano.
     * @param posicion Posición del aldeano.
     * @param status Estado: `"Cesante"` o `"Nitwit"`.
     * @return `true` si se registra; `false` si el estado no se admite o el nombre o la posición no son válidos.
     */
    fun agregarAldeanoNoEmpleado(nombre: String, posicion: String, status: String): Boolean {
        try {
            when (status) {
                "Cesante" -> aldeanos.add(Cesante(nombre, posicion))
                "Nitwit" -> aldeanos.add(Nitwit(nombre, posicion))
                else -> {
                    println(Colores.error("\nError: El estado del aldeano no es válido."))
                    println(Colores.error("Estados válidos: 'Empleado', 'Cesante' o 'Nitwit'."))
                    return false
                }
            }
            return true    
        }
        catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de aldeano inválidos."}"))
            return false
        }
    }

    /**
     * Convierte al aldeano cesante indicado en empleado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @param profesion Profesión asignada.
     * @return `true` si se emplea; `false` si el identificador no existe, el aldeano no es cesante o la profesión no es válida.
     */
    fun emplearAldeano(id: Int, profesion: String): Boolean {
        if (id < 1) return false
        val indice = id - 1
        val original = aldeanos.getOrNull(indice) ?: run {
            println(Colores.error("\nError: No se encontró un aldeano con ID $id."))
            return false
        }
        if (original !is Cesante) {
            println(Colores.error("\nError: El aldeano con ID $id no puede ser empleado."))
            return false
        }

        return try {
            aldeanos[indice] = Empleado(original.nombre, original.posicion, profesion)
            println(Colores.exito("\nAldeano con ID $id empleado exitosamente como $profesion."))
            true
        } catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de aldeano inválidos."}"))
            false
        }
    }

    /**
     * Elimina el aldeano indicado.
     *
     * @param id Identificador del aldeano, comenzando en 1.
     * @return `true` si se elimina; `false` si el identificador no es válido.
     */
    fun eliminarAldeano(id: Int): Boolean {
        if (id < 1) {
            println(Colores.error("\nError: ID inválido."))
            return false
        }
         
        val indice = id - 1
        if (indice !in aldeanos.indices) {
            println(Colores.error("\nError: No se encontró un aldeano con ID $id."))
            return false
        }
        aldeanos.removeAt(indice)
        println(Colores.exito("\nAldeano con ID $id eliminado exitosamente."))
        return true
    }
    
    /**
     * Registra un intercambio para el empleado indicado.
     *
     * @param idAldeano Identificador del empleado, comenzando en 1.
     * @param itemPago Artículo a pagar al empleado.
     * @param cantPago Cantidad del artículo a pagar.
     * @param itemVenta Artículo que vende el empleado.
     * @param cantVenta Cantidad del artículo en venta.
     * @return `true` si se agrega; `false` si el empleado o los datos no son válidos.
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
            println(Colores.error("\nError: No se encontró un empleado con el ID $idAldeano."))
            return false
        }
        val cumplimiento = empleado.agregarTradeo(itemPago, cantPago, itemVenta, cantVenta)
        
        if (cumplimiento) {
            println(Colores.exito("\nTradeo añadido exitosamente."))
        } else {
            println(Colores.error("\nError: No se pudo añadir el tradeo."))
        }
        return cumplimiento
    }

    /**
     * Elimina un intercambio del empleado indicado.
     *
     * @param idAldeano Identificador del empleado, comenzando en 1.
     * @param idTradeo Identificador del intercambio, comenzando en 1.
     * @return `true` si se elimina; `false` si algún identificador no es válido.
     */
    fun eliminarTradeo(idAldeano: Int, idTradeo: Int): Boolean {
        val empleado = aldeanos.getOrNull(idAldeano - 1) as? Empleado
        if (empleado == null) {
            println(Colores.error("\nError: No se encontró un empleado con el ID $idAldeano."))
            return false
        }

        if (!empleado.eliminarTradeo(idTradeo)) {
            println(Colores.error("\nError: No se encontró el intercambio con el ID $idTradeo."))
            return false
        }
        
        println(Colores.exito("\nTradeo con ID $idTradeo eliminado exitosamente del aldeano con ID $idAldeano."))
        return true
    }

    /**
     * Cambia un usuario [Noob] a [Pro], conservando sus credenciales.
     *
     * @param nombre Nombre de usuario.
     * @return `true` si existe y es [Noob]; `false` en caso contrario.
     */
    fun promoverUsuario(nombre: String): Boolean {
        val indice = usuarios.indexOfFirst { it.nombre == nombre }
        if (indice == -1) {
            println(Colores.error("\nError: No se encontró el usuario '$nombre'."))
            return false
        }

        val usuario = usuarios[indice]
        if (usuario !is Noob) {
            println(Colores.error("\nError: El usuario '$nombre' no puede ser promovido."))
            return false
        }

        try {
            usuarios[indice] = Pro(usuario.nombre, usuario.password)
        } catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de usuario inválidos."}"))
            return false
        }
        println(Colores.exito("\nEl usuario '$nombre' fue promovido a Pro."))
        return true
    }

    /**
     * Banea al usuario indicado, excepto si es [Admin].
     *
     * @param nombre Nombre de usuario.
     * @return `true` si se banea; `false` si no existe, es [Admin] o ya está baneado.
     */
    fun banearUsuario(nombre: String): Boolean {
        val indice = usuarios.indexOfFirst { it.nombre == nombre }
        if (indice == -1) {
            println(Colores.error("\nError: No se encontró el usuario '$nombre'."))
            return false
        }

        val usuario = usuarios[indice]
        if (usuario is Admin) {
            println(Colores.error("\nError: No se puede banear a un Admin."))
            return false  
        } 
        if (usuario is Baneado) {
            println(Colores.error("\nError: El usuario '$nombre' ya está baneado."))
            return false
        }

        try {
            usuarios[indice] = Baneado(usuario.nombre, usuario.password)
        } catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de usuario inválidos."}"))
            return false
        }
        println(Colores.exito("\nEl usuario '$nombre' fue BANEADO con éxito."))
        return true
    }

    /**
     * Desbanea al usuario y lo cambia a [Noob].
     *
     * @param nombre Nombre de usuario.
     * @return `true` si existe y está baneado; `false` en caso contrario.
     */
    fun desbanearUsuario(nombre: String): Boolean {
        val indice = usuarios.indexOfFirst { it.nombre == nombre }
        if (indice == -1) {
            println(Colores.error("\nError: Usuario '$nombre' no encontrado."))
            return false
        }

        val usuario = usuarios[indice]
        if (usuario !is Baneado) {
            println(Colores.error("\nError: Usuario '$nombre' no está Baneado."))
            return false
        }
        
        try {
            usuarios[indice] = Noob(usuario.nombre, usuario.password)
        } catch (e: IllegalArgumentException) {
            println(Colores.error("\nError de validación: ${e.message ?: "Datos de usuario inválidos."}"))
            return false
        }
        println(Colores.exito("\nEl usuario '$nombre' ha sido desbaneado con éxito."))
        return true
    }
}
