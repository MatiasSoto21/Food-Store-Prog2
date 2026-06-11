package integrador.prog2.entities;


public class DetallePedido extends Base {
    private int cantidad;
    private double subtotal;
    private Producto producto;

    public DetallePedido() {
        super();
    }

    public DetallePedido(int cantidad, double subtotal, Producto producto) {
        super();
        setCantidad(cantidad);
        setProducto(producto);
        setSubtotal(subtotal);
    }

    public DetallePedido(int cantidad, Producto producto) {
        super();
        setCantidad(cantidad);
        setProducto(producto);
        this.subtotal = cantidad * producto.getPrecio();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        if (subtotal < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + getId() +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                ", producto=" + (producto != null ? producto.getNombre() : "Sin producto") +
                '}';
    }
}
