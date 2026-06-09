package integrador.prog2.entities;

import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//va a tirar error hasta que se creen todas las clases
public class Pedido implements Calculable { //falta extender la clase padre
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles = new ArrayList<>(); // COMPOSICION

    public Pedido() {
    }

    public Pedido(LocalDate fecha, Estado estado, double total, FormaPago formaPago) {
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void addDetallePedido(int cantidad, double precio, Producto producto) {
        if (cantidad == 0 || precio <= 0 || producto == null ) {
            throw new IllegalArgumentException("El  detalle tiene campos invalidos");
        }
        detalles.add(new DetallePedido(cantidad,precio, producto));//
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto){
        for (DetallePedido detalle : detalles)
            if (detalle.getProducto().equals(producto)){
                return detalle;
            }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El  detalle no puede ser nulo");
        }

        DetallePedido busqueda = findDetallePedidoByProducto(producto);

        if (busqueda == null){
            throw new RuntimeException("No se encontro el producto ingresado.");//no se si esta execpcion va o es otro tipo de excepcion
        } else {
            detalles.remove(busqueda);
        }
    }

    @Override
    public void calcularTotal() { // falta implementar
        System.out.println("Hola");
    }
}
