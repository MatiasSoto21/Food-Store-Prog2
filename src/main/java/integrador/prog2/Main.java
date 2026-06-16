package integrador.prog2;

import integrador.prog2.entities.*;
import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import integrador.prog2.enums.Rol;

public class Main {
    public static void main(String[] args) {

         System.out.println("Paso 1");

        Categoria lacteos = new Categoria("Lacteos","Leches y derivados");
        Categoria panaderia = new Categoria("Panaderia","Productos de panificacion");
        Producto leche = new Producto("Leche Entera",1500.0,"Leche marca X",0,"leche.jpg");
        Producto yogur =new Producto("Yogur",1200.0,"Yogur frutilla",10,"yogur.jpg");
        Producto pan = new Producto("Pan flauta",1000.0,"Pan",8,"pan.jpg");
        Producto facturas = new Producto("Facturas",300.0,"Docena de facturas",2,"facturas.jpg");

        lacteos.agregarProducto(leche);
        lacteos.agregarProducto(yogur);
        panaderia.agregarProducto(pan);
        panaderia.agregarProducto(facturas);

        System.out.println(leche);
        System.out.println(yogur);
        System.out.println(pan);
        System.out.println(facturas);

        System.out.println("Paso 2");

        Pedido pedido =new Pedido(FormaPago.EFECTIVO);

        System.out.println(pedido);

        System.out.println("Paso 3");

        try {
            pedido.addDetallePedido(0,leche);

        } catch (Exception e) {
                System.out.println("ERROR CAPTURADO: "+ e.getMessage());
        }
        //mensaje de error para validad venta con funcion de stockInsucienteException
        //leche.validarVenta(0);
        // leche.validarDisponibilidad();
        

        System.out.println();
        System.out.println("Paso 4");

        try{
            pedido.addDetallePedido(2,facturas);
        }catch (Exception e){
            System.out.println("ERROR CAPTURADO: "+ e.getMessage());
        }

        System.out.println("Producto actualizado:");

        System.out.println(facturas);
        
        System.out.println();
        
        System.out.println("Pedido actualizado:");
        
        System.out.println(pedido);

        System.out.println();
        System.out.println("Paso 5");

        System.out.println(pedido);

    }
}
