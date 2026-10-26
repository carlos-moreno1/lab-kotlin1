# Minecraft Villager Management

## Descripción del problema

En los mundos multijugador técnicos o de supervivencia a largo plazo en Minecraft, es común agrupar
decenas de aldeanos en zonas conocidas como "Trading Halls" (salas de comercio).
Sin embargo, a medida que la zona crece, la cantidad de aldeanos se vuelve inmanejable.

Hoy en día, los jugadores intentan organizar esta zona usando carteles de madera o
simplemente confiando en su memoria. Esto genera varios problemas:

- **Pérdida de tiempo**: Los jugadores olvidan qué aldeano vende qué objeto exacto y pasan
    minutos buscando sin rumbo.
- **Posición**: No hay registro claro de en qué coordenadas exactas se
    encuentra el aldeano que vende un encantamiento crucial (como Mending/Reparación).
- **Falta de administración**: Cualquier jugador nuevo puede entrar, alterar los intercambios
    o perder aldeanos sin que haya un registro de quién hizo qué.

**Minecraft Villager Management** reemplaza los carteles y la memoria por un sistema de
registro estructurado. El sistema permite buscar rápidamente qué aldeano vende el objeto
deseado, conocer sus coordenadas exactas y restringir quién puede añadir o eliminar aldeanos
de la base de datos.

Alcance de esta versión:

- Sistema interactivo por consola.
- Datos almacenados temporalmente en memoria durante la ejecución, inicializados desde archivos de texto.
- Fuera de alcance: Modificaciones directas dentro del juego (no es un mod, es una
  herrramienta externa de gestión).

## Estructura del Proyecto

El proyecto está contenido dentro de la carpeta principal `villager-management` y se organiza en los siguientes directorios:

*   **`/` (Raíz):** Contiene el archivo `main.kt`, que sirve como punto de entrada de la aplicación.
*   **`/helpers`:** Contiene el archivo `Consola.kt`, encargado de la interfaz de usuario, los menús y la interacción por teclado.
*   **`/models`:** Contiene la lógica de negocio y las entidades del sistema (`Servidor.kt`, `Aldeano.kt`, `Usuario.kt`, `Tradeo.kt`).
*   **`/data`:** Directorio destinado a los archivos de texto (`aldeanos.txt`, `usuarios.txt`) para la carga y persistencia inicial de datos.

## Usuarios del sistema

El sistema divide a los jugadores en tres categorias según su nivel de confianza y experiencia en
el servidor:

| Usuario | Tarea desempeñada en el sistema |
| :---: | :---: |
| Baneado | No puede acceder al sistema. Se considera un usuario problemático. |
| Noob | Tiene permisos de solo lectura. Su vista es superficial: busca objetos<br>a la venta y consulta las coordenadas para saber a dónde ir.|
| Pro | Tiene permisos de escritura. Además de consultar, es el encargado<br>de registrar nuevos aldeanos en el sistema y eliminarlos si mueren o<br>son movidos. |
| Admin | Tiene el control total. Se encarga de mantener el orden, con la<br>capacidad exclusiva de expulsar (banear) a usuarios problemáticos<br>del sistema como tambien ascenderlos de rango. |

## Entidades principales
Para que el sistema funcione y tenga sentido, se han definido los siguientes elementos:

### Servidor
Es el motor central del sistema. Actúa como el lugar virtual donde se guardan todos los
registros de los aldeanos y donde se conectan los usuarios. El servidor es quien recibe las
órdenes y actúa en consecuencia.

### Usuario
Representa a la persona real frente a la pantalla. Todo usuario debe registrarse con un nombre
(nickname) y su respectiva contraseña. Dependiendo de su rango (Noob, Pro o Admin), el sistema le permitirá
realizar distintas acciones. Puede perder acceso al sistema (de manera no permanente) y volverse
un usuario Baneado.

### Aldeano
Representa a cada aldeano dentro del juego. Cada uno de ellos recibe nombre y tiene un
identificador único (ID) basado en la posición que tiene en la lista que maneja el Servidor.
Se guarda información como nombre (apodo dado por el jugador), posición (típicamente coordenadas)
y, en el caso de los empleados, su profesión y los intercambios que ofrece.

### Tradeo
Representa los datos presentes en un intercambio realizado por un aldeano Empleado.
Contempla los items en venta y a pagar, con sus respectivas cantidades.

## Operaciones que permitirá el sistema

El programa funcionará a través de un menú interactivo centralizado, cuyas opciones varían según el rango del usuario autenticado:

**Menú de Inicio (Fuera de sesión):**
*   **Iniciar Sesión:** Permite acceder al sistema ingresando credenciales válidas.
*   **Registrarse:** Permite crear una nueva cuenta (por defecto con rango Noob).
*   **Salir:** Cierra la aplicación.

**Opciones Generales (Nivel Noob):**
*   **Ver Aldeanos:** Muestra una lista superficial con el ID, nombre y estado de todos los aldeanos.
*   **Ver datos Aldeanos:** Muestra información detallada de un aldeano específico, incluyendo su profesión mediante su ID.
*   **Buscar Intercambio:** Permite introducir el nombre de un ítem y lista todos los empleados que lo venden, incluyendo su ubicación y precio.

**Opciones de Gestión de Aldeanos (Nivel Pro y Admin):**
*   **Agregar Aldeano:** Registra un nuevo aldeano especificando su nombre, ubicación, estado y profesión (si aplica).
*   **Eliminar Aldeano:** Borra el registro de un aldeano del sistema mediante su ID.
*   **Emplear Aldeano:** Permite asignar una profesión a un aldeano que actualmente se encuentra en estado "Cesante".

**Opciones de Administración (Exclusivo Admin):**
*   **Agregar Tradeos:** Registra un nuevo intercambio para un aldeano empleado, especificando ítems y cantidades.
*   **Eliminar Tradeos:** Elimina un intercambio específico de un aldeano empleado.
*   **Promover Usuario:** Asciende a un usuario de rango "Noob" a rango "Pro".
*   **Banear Usuario:** Revoca permanentemente el acceso de un usuario al sistema, cambiando su estado a "Baneado".

## Validaciones y reglas
Para evitar que el sistema falle o tenga datos irreales, el sistema asegurará que:

**Gestión de Cuentas y Accesos:**
*   Al registrarse, las contraseñas ingresadas deben coincidir de manera exacta.
*   No se puede registrar un usuario con un nickname que ya esté en uso en el servidor.
*   Un usuario en estado "Baneado" no puede iniciar sesión bajo ninguna circunstancia.
*   Los usuarios con rango "Admin" son inmunes y no pueden ser baneados por otros administradores.

**Gestión de Aldeanos:**
*   El nombre y la posición (coordenadas) de un aldeano no pueden ser campos vacíos.
*   Solo se permite el ingreso de tres estados válidos para los aldeanos: `Empleado`, `Cesante` y `Nitwit`.
*   Únicamente los aldeanos en estado `Cesante` pueden ser modificados mediante la operación "Emplear Aldeano". Los `Nitwit` son incapaces de adquirir empleo.

**Gestión de Intercambios (Tradeos):**
*   Los intercambios solo pueden ser asignados a aldeanos en estado `Empleado`.
*   El nombre del ítem entregado como pago y el nombre del ítem ofrecido en venta no pueden estar vacíos.
*   Las cantidades de los ítems (tanto para pago como para venta) deben ser estrictamente mayores que cero.

## Diagrama UML

A continuación, se detalla la estructura lógica del sistema, reflejando la separación entre la interfaz de consola, el motor lógico y los modelos de datos:

```mermaid
classDiagram
    direction LR

    class Consola {
        <<singleton>>
        + menuLogin()
        + leerOpcion(): Int
        + inicioSesion(servidor: Servidor): Usuario?
        + registrarSesion(servidor: Servidor): Boolean
        + menuPrincipal(usuario: Usuario)
        + sesion(servidor: Servidor, usuario: Usuario)
    }

    class Servidor {
        + cargarAldeanos()
        + cargarUsuarios()
        + register(nombre: String, password: String, passwordConfirm: String): Boolean
        + login(nombre: String, password: String): Usuario?
        + mostrarUsuarios(): Boolean
        + verListaAldeanos(): Boolean
        + verDatosAldeano(id: Int): Boolean
        + verTradeosAldeano(id: Int): Boolean
        + buscarTradeo(item: String): Boolean
        + buscarProfesion(profesion: String): Boolean
        + agregarAldeanoEmpleado(nombre: String, posicion: String, profesion: String): Boolean
        + agregarAldeanoNoEmpleado(nombre: String, posicion: String, status: String): Boolean
        + emplearAldeano(id: Int, profesion: String): Boolean
        + eliminarAldeano(id: Int): Boolean
        + agregarTradeo(idAldeano: Int, itemPago: String, cantPago: Int, itemVenta: String, cantVenta: Int): Boolean
        + eliminarTradeo(idAldeano: Int, idTradeo: Int): Boolean
        + promoverUsuario(nombre: String): Boolean
        + banearUsuario(nombre: String): Boolean
        + desbanearUsuario(nombre: String): Boolean
    }

    class Usuario {
        <<abstract>>
        + nombre: String
        # password: String
        + verificarPassword(pass: String): Boolean
    }

    class Noob
    class Pro
    class Admin
    class Baneado

    class Aldeano {
        <<abstract>>
        + nombre: String
        - posicion: String
        + status: String*
        + cambiarPosicion(nuevaPosicion: String): Boolean
        + verDatos()
    }

    class Empleado {
        + status: String
        + profesion: String
        + verDatos()
        + obtenerTradeo(): List~Tradeo~
        + agregarTradeo(itemPago: String, cantPago: Int, itemVenta: String, cantVenta: Int): Boolean
        + eliminarTradeo(id: Int): Boolean
        + verTradeos()
    }

    class Cesante {
        + status: String
    }

    class Nitwit {
        + status: String
    }

    class Tradeo {
        + itemPago: String
        + cantPago: Int
        + itemVenta: String
        + cantVenta: Int
    }

    %% HERENCIA
    Aldeano <|-- Empleado
    Aldeano <|-- Cesante
    Aldeano <|-- Nitwit

    Usuario <|-- Noob
    Usuario <|-- Pro
    Usuario <|-- Admin
    Usuario <|-- Baneado

    %% RELACIONES
    Empleado "1" *-- "0..*" Tradeo : Registra
    Servidor "1" *-- "0..*" Aldeano : Controla
    Servidor "1" o-- "1..*" Usuario : Administra
    Consola ..> Servidor : Delega lógica a
    Consola ..> Usuario : Determina permisos de
