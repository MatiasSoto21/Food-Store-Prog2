package integrador.prog2.entities;

import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;
import integrador.prog2.interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static integrador.prog2.entities.Validador.*;


public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido(FormaPago formaPago) {
        super();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.formaPago = formaPago;
        this.fecha = LocalDate.now();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public Double getTotal() {
        return total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public void addDetallePedido(int cantidad, Producto producto) {
        DetallePedido detalleP = new DetallePedido(cantidad, producto);
        detalles.add(detalleP);
        if (detalles.size() != 0 && estado == Estado.PENDIENTE) {
            estado = Estado.PENDIENTE;
        }
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        objetoNulo(producto,"producto");

        for (DetallePedido detalle : detalles) {

            if (detalle.getProducto().equals(producto)) {
                System.out.println("Se encontro");
                return detalle;
            }
        }
        System.out.println("No se encontro");
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {

        DetallePedido detalle = findDetallePedidoByProducto(producto);

        if (detalle != null) {
            System.out.println("Se borro el detalle");
            producto.aumentarStock(detalle.getCantidad());
            
            detalles.remove(detalle);
            calcularTotal();
            if (detalles.isEmpty()) {
                estado = Estado.PENDIENTE;
            }
        } else {
            System.out.println("No se encontro el detalle para borrar");
        }
    }

    public void validarPedido() {
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido no posee detalles");
        }
        estado = Estado.CONFIRMADO;
    }

    @Override
    public void calcularTotal() {
        total = 0.0;

        for (DetallePedido detalle : detalles) {
            total += detalle.getSubtotal();
        }
    }

    @Override
    public String toString() {
        return "Pedido{" + "fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", formaPago=" + formaPago + ", detalles=" + detalles + '}';
    }
}
