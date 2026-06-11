package integrador.prog2.entities;

import integrador.prog2.enums.Estado;
import integrador.prog2.enums.FormaPago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Pedido extends Base implements Calculable {
    private LocalDate fecha;
    private Estado estado;
    private double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles = new ArrayList<>(); // COMPOSICION

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0;
    }

    public Pedido(LocalDate fecha, Estado estado, double total, FormaPago formaPago) {
        super();
        setFecha(fecha);
        setEstado(estado);
        setTotal(total);
        setFormaPago(formaPago);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        if (formaPago == null) {
            throw new IllegalArgumentException("La forma de pago no puede ser nula");
        }
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (precioUnitario == null || precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }

        producto.validarVenta(cantidad);
        double subtotal = cantidad * precioUnitario;
        DetallePedido detalle = new DetallePedido(cantidad, subtotal, producto);
        detalles.add(detalle);

        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        for (DetallePedido detalle : detalles)
            if (detalle.getProducto().equals(producto)) {
                return detalle;
            }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);

        if (detalle == null) {
            throw new IllegalArgumentException("No se encontró un detalle con ese producto");
        }

        detalles.remove(detalle);
        calcularTotal();
    }

    @Override
    public void calcularTotal() {
        double total = 0.0;

        for (DetallePedido detalle : detalles) {
            total += detalle.getSubtotal();
        }

        this.total = total;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + getId() +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", total=" + total +
                ", formaPago=" + formaPago +
                ", cantidadDetalles=" + detalles.size() +
                '}';
    }
}
