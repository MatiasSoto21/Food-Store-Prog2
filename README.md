# Food Store - Sistema de Gestión de Pedidos

Trabajo Práctico Integrador de Programación II.

## Integrantes

- Matías Soto
- Guillermo Pinto
- Franco Astorani

## Descripción

Food Store es una aplicación de consola desarrollada en Java 21 para gestionar categorías, productos, usuarios y pedidos de un negocio de comidas.

El sistema utiliza Programación Orientada a Objetos, patrón DAO, servicios, JDBC y MySQL. Toda la interacción se realiza mediante menú de consola.

## Tecnologías utilizadas

- Java 21
- Maven
- MySQL 8.4.9 LTS
- JDBC
- MySQL Workbench
- Netbeans
- IntelliJ IDEA

## Estructura del proyecto

```text
src/main/java/integrador/prog2
├── config
├── dao
├── entities
├── enums
├── exception
├── service
├── Main.java
└── MenuConsola.java
```

## Base de datos

El script de creación de la base de datos se encuentra en:

```text
src/main/resources/schema.sql
```

Para preparar la base de datos:

1. Abrir MySQL Workbench.
2. Ejecutar el archivo `schema.sql`.
3. Verificar que se cree la base de datos `food_store_db`.

Tablas principales:

- categoria
- producto
- usuario
- pedido
- detalle_pedido

## Configuración de conexión

La conexión se configura en:

```text
src/main/java/integrador/prog2/config/ConexionDB.java
```

Configuración por defecto:

```java
private static final String URL = "jdbc:mysql://localhost:3306/food_store_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

Si MySQL tiene contraseña, modificar `PASSWORD`.

## Cómo ejecutar

1. Abrir el proyecto en IntelliJ IDEA.
2. Cargar dependencias Maven.
3. Ejecutar `schema.sql` en MySQL Workbench.
4. Ejecutar la clase:

```text
integrador.prog2.Main
```

## Funcionalidades

El sistema permite realizar operaciones CRUD desde consola sobre:

- Categorías
- Productos
- Usuarios
- Pedidos

También permite:

- Crear pedidos con uno o más detalles.
- Calcular subtotales y total del pedido.
- Descontar stock al crear pedidos.
- Restaurar stock al eliminar pedidos.
- Realizar baja lógica mediante el campo eliminado.
- Listar pedidos con usuario asociado.
- Ver detalles de un pedido.
- Validar stock insuficiente.
- Impedir la eliminación de categorías con productos activos asociados.
- Validar mail único para usuarios.
- Validar precio y stock no negativos.

### Reglas de negocio implementadas
- No se permite crear productos con precio negativo.
- No se permite crear productos con stock negativo.
- No se permite crear pedidos sin usuario.
- No se permite crear pedidos sin detalles.
- No se permite agregar detalles con cantidad menor o igual a cero.
- No se permite vender productos sin stock suficiente.
- No se permite eliminar una categoría si tiene productos activos asociados.
- Las eliminaciones se realizan mediante baja lógica.

## Video demostrativo

Link del video:

```text
PENDIENTE
```

## Documentación PDF

La documentación académica se adjunta en la entrega final.