package integrador.prog2;

import integrador.prog2.entities.*;
import integrador.prog2.enums.*;
import integrador.prog2.service.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {

    private final Scanner scanner;
    private final CategoriaService categoriaService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    public MenuConsola() {
        this.scanner = new Scanner(System.in);
        this.categoriaService = new CategoriaService();
        this.productoService = new ProductoService();
        this.usuarioService = new UsuarioService();
        this.pedidoService = new PedidoService();
    }

    public void iniciar() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorías");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");

            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> menuCategorias();
                case 2 -> menuProductos();
                case 3 -> menuUsuarios();
                case 4 -> menuPedidos();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void menuCategorias() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== GESTIÓN DE CATEGORÍAS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarCategorias();
                case 2 -> crearCategoria();
                case 3 -> editarCategoria();
                case 4 -> eliminarCategoria();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void listarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.listar();

            System.out.println();
            System.out.println("=== LISTADO DE CATEGORÍAS ===");

            if (categorias.isEmpty()) {
                System.out.println("No hay categorías cargadas.");
                return;
            }

            for (Categoria categoria : categorias) {
                System.out.println(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void crearCategoria() {
        try {
            System.out.println();
            System.out.println("=== CREAR CATEGORÍA ===");

            String nombre = leerTexto("Nombre: ");
            String descripcion = leerTexto("Descripción: ");

            Categoria categoria = new Categoria(nombre, descripcion);

            categoriaService.crear(categoria);

            System.out.println("Categoría creada correctamente con id: " + categoria.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void editarCategoria() {
        try {
            System.out.println();
            System.out.println("=== EDITAR CATEGORÍA ===");

            listarCategorias();

            Long id = leerLong("Ingrese el id de la categoría a editar: ");

            Categoria categoria = categoriaService.leer(id);

            System.out.println("Categoría actual:");
            System.out.println(categoria);

            String nuevoNombre = leerTexto("Nuevo nombre: ");
            String nuevaDescripcion = leerTexto("Nueva descripción: ");

            categoria.setNombre(nuevoNombre);
            categoria.setDescripcion(nuevaDescripcion);

            categoriaService.actualizar(categoria);

            System.out.println("Categoría actualizada correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        try {
            System.out.println();
            System.out.println("=== ELIMINAR CATEGORÍA ===");

            listarCategorias();

            Long id = leerLong("Ingrese el id de la categoría a eliminar: ");

            Categoria categoria = categoriaService.leer(id);

            System.out.println("Categoría seleccionada:");
            System.out.println(categoria);

            String confirmacion = leerTexto("¿Confirmar eliminación? (S/N): ");

            if (confirmacion.equalsIgnoreCase("S")) {
                categoriaService.eliminar(id);
                System.out.println("Categoría eliminada correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void menuProductos() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== GESTIÓN DE PRODUCTOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Listar por categoría");
            System.out.println("3. Crear");
            System.out.println("4. Editar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarProductos();
                case 2 -> listarProductosPorCategoria();
                case 3 -> crearProducto();
                case 4 -> editarProducto();
                case 5 -> eliminarProducto();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void listarProductos() {
        try {
            List<Producto> productos = productoService.listar();

            System.out.println();
            System.out.println("=== LISTADO DE PRODUCTOS ===");

            if (productos.isEmpty()) {
                System.out.println("No hay productos cargados.");
                return;
            }

            for (Producto producto : productos) {
                System.out.println(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void listarProductosPorCategoria() {
        try {
            System.out.println();
            System.out.println("=== LISTAR PRODUCTOS POR CATEGORÍA ===");

            listarCategorias();

            Long idCategoria = leerLong("Ingrese id de la categoría: ");

            List<Producto> productos = productoService.listarPorCategoria(idCategoria);

            if (productos.isEmpty()) {
                System.out.println("No hay productos cargados para esa categoría.");
                return;
            }

            for (Producto producto : productos) {
                System.out.println(producto);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void crearProducto() {
        try {
            System.out.println();
            System.out.println("=== CREAR PRODUCTO ===");

            listarCategorias();

            String nombre = leerTexto("Nombre: ");
            double precio = leerDouble("Precio: ");
            String descripcion = leerTexto("Descripción: ");
            int stock = leerEntero("Stock: ");
            String imagen = leerTexto("Imagen: ");
            boolean disponible = leerBoolean("¿Disponible? (S/N): ");
            Long idCategoria = leerLong("Id categoría: ");

            Categoria categoria = categoriaService.leer(idCategoria);

            Producto producto = new Producto(
                    nombre,
                    precio,
                    descripcion,
                    stock,
                    imagen,
                    disponible,
                    categoria
            );

            productoService.crear(producto);

            System.out.println("Producto creado correctamente con id: " + producto.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void editarProducto() {
        try {
            System.out.println();
            System.out.println("=== EDITAR PRODUCTO ===");

            listarProductos();

            Long id = leerLong("Ingrese id del producto a editar: ");

            Producto producto = productoService.leer(id);

            System.out.println("Producto actual:");
            System.out.println(producto);

            listarCategorias();

            String nombre = leerTexto("Nuevo nombre: ");
            double precio = leerDouble("Nuevo precio: ");
            String descripcion = leerTexto("Nueva descripción: ");
            int stock = leerEntero("Nuevo stock: ");
            String imagen = leerTexto("Nueva imagen: ");
            boolean disponible = leerBoolean("¿Disponible? (S/N): ");
            Long idCategoria = leerLong("Nueva categoría id: ");

            Categoria categoria = categoriaService.leer(idCategoria);

            producto.setNombre(nombre);
            producto.setPrecio(precio);
            producto.setDescripcion(descripcion);
            producto.setStock(stock);
            producto.setImagen(imagen);
            producto.setDisponible(disponible);
            producto.setCategoria(categoria);

            productoService.actualizar(producto);

            System.out.println("Producto actualizado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            System.out.println();
            System.out.println("=== ELIMINAR PRODUCTO ===");

            listarProductos();

            Long id = leerLong("Ingrese id del producto a eliminar: ");

            Producto producto = productoService.leer(id);

            System.out.println("Producto seleccionado:");
            System.out.println(producto);

            String confirmacion = leerTexto("¿Confirmar eliminación? (S/N): ");

            if (confirmacion.equalsIgnoreCase("S")) {
                productoService.eliminar(id);
                System.out.println("Producto eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }


    private void menuUsuarios() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== GESTIÓN DE USUARIOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarUsuarios();
                case 2 -> crearUsuario();
                case 3 -> editarUsuario();
                case 4 -> eliminarUsuario();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listar();

            System.out.println();
            System.out.println("=== LISTADO DE USUARIOS ===");

            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios cargados.");
                return;
            }

            for (Usuario usuario : usuarios) {
                System.out.println(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void crearUsuario() {
        try {
            System.out.println();
            System.out.println("=== CREAR USUARIO ===");

            String nombre = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");
            String mail = leerTexto("Mail: ");
            String celular = leerTexto("Celular: ");
            String contrasena = leerTexto("Contraseña: ");
            Rol rol = leerRol();

            Usuario usuario = new Usuario(
                    nombre,
                    apellido,
                    mail,
                    celular,
                    contrasena,
                    rol
            );

            usuarioService.crear(usuario);

            System.out.println("Usuario creado correctamente con id: " + usuario.getId());

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void editarUsuario() {
        try {
            System.out.println();
            System.out.println("=== EDITAR USUARIO ===");

            listarUsuarios();

            Long id = leerLong("Ingrese id del usuario a editar: ");

            Usuario usuario = usuarioService.leer(id);

            System.out.println("Usuario actual:");
            System.out.println(usuario);

            String nombre = leerTexto("Nuevo nombre: ");
            String apellido = leerTexto("Nuevo apellido: ");
            String mail = leerTexto("Nuevo mail: ");
            String celular = leerTexto("Nuevo celular: ");
            String contrasena = leerTexto("Nueva contraseña: ");
            Rol rol = leerRol();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setMail(mail);
            usuario.setCelular(celular);
            usuario.setContraseña(contrasena);
            usuario.setRol(rol);

            usuarioService.actualizar(usuario);

            System.out.println("Usuario actualizado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            System.out.println();
            System.out.println("=== ELIMINAR USUARIO ===");

            listarUsuarios();

            Long id = leerLong("Ingrese id del usuario a eliminar: ");

            Usuario usuario = usuarioService.leer(id);

            System.out.println("Usuario seleccionado:");
            System.out.println(usuario);

            String confirmacion = leerTexto("¿Confirmar eliminación? (S/N): ");

            if (confirmacion.equalsIgnoreCase("S")) {
                usuarioService.eliminar(id);
                System.out.println("Usuario eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void menuPedidos() {
        int opcion;

        do {
            System.out.println();
            System.out.println("=== GESTIÓN DE PEDIDOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Ver detalle por id");
            System.out.println("3. Crear pedido");
            System.out.println("4. Actualizar estado / forma de pago");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");

            opcion = leerEntero("Seleccione: ");

            switch (opcion) {
                case 1 -> listarPedidos();
                case 2 -> verDetallePedido();
                case 3 -> crearPedido();
                case 4 -> actualizarPedido();
                case 5 -> eliminarPedido();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void listarPedidos() {
        try {
            System.out.println();
            System.out.println("=== LISTADO DE PEDIDOS ===");

            List<String> pedidos = pedidoService.listarResumenPedidos();

            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos cargados.");
                return;
            }

            for (String pedido : pedidos) {
                System.out.println(pedido);
            }

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void verDetallePedido() {
        try {
            System.out.println();
            System.out.println("=== DETALLE DE PEDIDO ===");

            listarPedidos();

            Long id = leerLong("Ingrese id del pedido: ");

            Pedido pedido = pedidoService.leer(id);

            System.out.println(pedido);

            if (pedido.getDetalles().isEmpty()) {
                System.out.println("El pedido no tiene detalles.");
                return;
            }

            System.out.println("Detalles:");
            for (DetallePedido detalle : pedido.getDetalles()) {
                System.out.println(detalle);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void crearPedido() {
        try {
            System.out.println();
            System.out.println("=== CREAR PEDIDO ===");

            listarUsuarios();

            Long idUsuario = leerLong("Ingrese id del usuario: ");

            // Validamos que exista antes de seguir
            usuarioService.leer(idUsuario);

            FormaPago formaPago = leerFormaPago();

            Pedido pedido = new Pedido(formaPago);

            boolean seguirAgregando;

            do {
                System.out.println();
                System.out.println("=== AGREGAR PRODUCTO AL PEDIDO ===");

                listarProductos();

                Long idProducto = leerLong("Ingrese id del producto: ");
                Producto producto = productoService.leer(idProducto);

                int cantidad = leerEntero("Cantidad: ");

                pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);

                System.out.println("Producto agregado al pedido.");

                seguirAgregando = leerBoolean("¿Agregar otro producto? (S/N): ");

            } while (seguirAgregando);

            pedido.calcularTotal();

            System.out.println();
            System.out.println("Resumen del pedido:");
            System.out.println(pedido);

            for (DetallePedido detalle : pedido.getDetalles()) {
                System.out.println(detalle);
            }

            boolean confirmar = leerBoolean("¿Confirmar pedido? (S/N): ");

            if (confirmar) {
                pedidoService.crearPedido(idUsuario, pedido);
                System.out.println("Pedido creado correctamente con id: " + pedido.getId());
            } else {
                System.out.println("Pedido cancelado.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void actualizarPedido() {
        try {
            System.out.println();
            System.out.println("=== ACTUALIZAR PEDIDO ===");

            listarPedidos();

            Long id = leerLong("Ingrese id del pedido a actualizar: ");

            Pedido pedido = pedidoService.leer(id);

            System.out.println("Pedido actual:");
            System.out.println(pedido);

            Estado nuevoEstado = leerEstado();
            FormaPago nuevaFormaPago = leerFormaPago();

            pedido.setEstado(nuevoEstado);
            pedido.setFormaPago(nuevaFormaPago);

            pedidoService.actualizar(pedido);

            System.out.println("Pedido actualizado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        try {
            System.out.println();
            System.out.println("=== ELIMINAR PEDIDO ===");

            listarPedidos();

            Long id = leerLong("Ingrese id del pedido a eliminar: ");

            Pedido pedido = pedidoService.leer(id);

            System.out.println("Pedido seleccionado:");
            System.out.println(pedido);

            boolean confirmar = leerBoolean("¿Confirmar eliminación? (S/N): ");

            if (confirmar) {
                pedidoService.eliminar(id);
                System.out.println("Pedido eliminado correctamente.");
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    //auxiliares

    private FormaPago leerFormaPago() {
        while (true) {
            System.out.println("Forma de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");

            int opcion = leerEntero("Seleccione forma de pago: ");

            switch (opcion) {
                case 1 -> {
                    return FormaPago.TARJETA;
                }
                case 2 -> {
                    return FormaPago.TRANSFERENCIA;
                }
                case 3 -> {
                    return FormaPago.EFECTIVO;
                }
                default -> System.out.println("Opción de forma de pago inválida.");
            }
        }
    }

    private Estado leerEstado() {
        while (true) {
            System.out.println("Estado:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. TERMINADO");
            System.out.println("4. CANCELADO");

            int opcion = leerEntero("Seleccione estado: ");

            switch (opcion) {
                case 1 -> {
                    return Estado.PENDIENTE;
                }
                case 2 -> {
                    return Estado.CONFIRMADO;
                }
                case 3 -> {
                    return Estado.TERMINADO;
                }
                case 4 -> {
                    return Estado.CANCELADO;
                }
                default -> System.out.println("Opción de estado inválida.");
            }
        }
    }

    private Rol leerRol() {
        while (true) {
            System.out.println("Rol:");
            System.out.println("1. ADMIN");
            System.out.println("2. USUARIO");

            int opcion = leerEntero("Seleccione rol: ");

            switch (opcion) {
                case 1 -> {
                    return Rol.ADMIN;
                }
                case 2 -> {
                    return Rol.USUARIO;
                }
                default -> System.out.println("Opción de rol inválida.");
            }
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Long.parseLong(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine();

            if (valor.equalsIgnoreCase("S")) {
                return true;
            }

            if (valor.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Debe ingresar S o N.");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}