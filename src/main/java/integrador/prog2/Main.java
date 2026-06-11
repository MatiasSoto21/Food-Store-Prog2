package integrador.prog2;

import integrador.prog2.entities.*;
import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import integrador.prog2.enums.Rol;

public class Main {
    public static void main(String[] args) {

        try {
            // Crear categorías
            Categoria hamburguesas = new Categoria("Hamburguesas", "Comida rápida");
            Categoria bebidas = new Categoria("Bebidas", "Gaseosas y aguas");

            hamburguesas.setId(1L);
            bebidas.setId(2L);

            // Crear productos
            Producto burger = new Producto(
                    "Smash Burger",
                    7500,
                    "Hamburguesa doble smash",
                    10,
                    "burger.jpg",
                    true
            );
            burger.setId(1L);

            Producto coca = new Producto(
                    "Coca Cola",
                    2000,
                    "Gaseosa 500ml",
                    20,
                    "coca.jpg",
                    true
            );
            coca.setId(2L);

            // Asociar productos a categorías
            hamburguesas.agregarProducto(burger);
            bebidas.agregarProducto(coca);

            System.out.println("=== CATEGORÍAS ===");
            System.out.println(hamburguesas);
            System.out.println(bebidas);

            System.out.println();

            System.out.println("=== PRODUCTOS DE HAMBURGUESAS ===");
            for (Producto producto : hamburguesas.getProductos()) {
                System.out.println(producto);
            }

            System.out.println();

            // Crear usuario
            Usuario usuario = new Usuario(
                    1L,
                    "Matias",
                    "Soto",
                    "matias@gmail.com",
                    "2611234567",
                    "1234",
                    Rol.USUARIO
            );

            System.out.println("=== USUARIO ===");
            System.out.println(usuario);

            System.out.println();

            // Crear pedido
            Pedido pedido = new Pedido();
            pedido.setId(1L);
            pedido.setFormaPago(FormaPago.EFECTIVO);
            pedido.setEstado(Estado.PENDIENTE);

            // Agregar detalles // van a salir con id null pero supuestamente con sql los genera bien.
            pedido.addDetallePedido(2, burger.getPrecio(), burger);
            pedido.addDetallePedido(3, coca.getPrecio(), coca);

            // Asociar pedido al usuario
            usuario.agregarPedido(pedido);

            System.out.println("=== PEDIDO ===");
            System.out.println(pedido);

            System.out.println();

            System.out.println("=== DETALLES DEL PEDIDO ===");
            for (DetallePedido detalle : pedido.getDetalles()) {
                System.out.println(detalle);
            }

            System.out.println();

            System.out.println("=== STOCK ACTUALIZADO ===");
            System.out.println(burger);
            System.out.println(coca);

            System.out.println();

            System.out.println("=== PEDIDOS DEL USUARIO ===");
            for (Pedido p : usuario.getPedidos()) {
                System.out.println(p);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
