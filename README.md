# Food Store - Sistema de Gestión de Pedidos de Comida 

**Institución:** Universidad Tecnológica Nacional (UTN)  
**Carrera:** Tecnicatura Universitaria en Programación a Distancia (TUPaD)  
**Materia:** Programación 2  
**Estudiante:** Matias Ferreyra  


##  Descripción del Proyecto

**Food Store** es un sistema de gestión en consola para un comercio de comidas desarrollado en Java 21 utilizando Programación Orientada a Objetos (POO) y una arquitectura en capas (Entidades, DAO, Repositorios, Servicios e Interfaz de Usuario).

### Modelo de Almacenamiento Mixto
- **Base de Datos Relacional:** Las entidades `Categoria` y `Producto` se persisten de forma permanente en base de datos mediante sentencias SQL parametrizadas (`PreparedStatement`).
- **Memoria Volátil (Colecciones):** Las entidades `Usuario`, `Pedido` y `DetallePedido` se gestionan mediante colecciones de Java durante la ejecución del programa y se pierden al finalizar la aplicación.


##  Configuración del Driver JDBC en `lib/`

El proyecto no utiliza gestores de dependencias. La conexión a la base de datos requiere incorporar el driver JDBC correspondiente dentro de la carpeta `lib/` en la raíz del proyecto:
 - Descarga el conector oficial `mysql-connector-j-*.jar` desde MySQL Connector/J.
 - Coloca el archivo `.jar` en la carpeta `lib/` del proyecto.

## Entregables
- **Video Demostrativo:** 
- **Documentación Académica:**
- **Codigo Base de Java en NetBeans:** `tpiprogramacionII` nombre de la carpeta donde esta almacenado todo el codigo base del proyecto
