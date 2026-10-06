# Minecraft Villager Management 

## Problema a resolver:
Se tiene una cantidad no manejable de aldeanos en cierta zona de un mundo de Minecraft. Los jugadores tienen dificultades para organizar, registrar y administrar
esta zona, por lo tanto, se busca facilitar y llevar a cabo un sistema que permita la gestion eficiente de este recurso digital.

## Usuarios del sistema
| Usuarios | Tarea desempeñada  |
| -------- | -------------------|
| Noob | Vista superficial del registro de aldeanos por objeto a la venta y sus coordenadas |
| Pro | Vista detallada del registro y permisos para registrar aldeanos en el sistema sistema |
| Admin | Tiene el control total del sistema, encargado principalmente de mantener la estabilidad en el sistema |

![image](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSN60cRLfe2wYPUMDscFPGhzHs6EzydyBI1b-2ZswZi1nxPdUkII5QLp0g&s=10)

## Diagrama UML (Avance)

```mermaid
classDiagram

    class Aldeano {
        <<abstract>>
    }

    class Usuario {
        <<abstract>>
    }

    class Noob
    class Pro
    class Admin

    class Granjero
    class Herrero
    class Bibliotecario

    Aldeano <|-- Granjero
    Aldeano <|-- Herrero
    Aldeano <|-- Bibliotecario

    Usuario <|-- Noob
    Usuario <|-- Pro
    Usuario <|-- Admin
```
