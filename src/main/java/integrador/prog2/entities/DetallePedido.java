package integrador.prog2.entities;



public class DetallePedido { // falta heredar el padre
    private int cantidad;
    private double subtotal;
    private Producto producto;

<<<<<<< Updated upstream
=======
    // constrcutor vacio
    public DetallePedido() {
        super();
    }

    public DetallePedido(int cantidad, double subtotal, Producto producto) {
        super();
        setCantidad(cantidad);
        setProducto(producto);
        setSubtotal(subtotal);
    }
>>>>>>> Stashed changes

    public DetallePedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = cantidad * producto.getPrecio();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    //se deberia calcular automaticamente
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    // al pedo prodcuto, ya tiene toString en producto
    public String toString() {
        return "DetallePedido{" +
                "cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                ", producto=" + producto.getNombre() + // falta validar null
                '}';
    }
}
