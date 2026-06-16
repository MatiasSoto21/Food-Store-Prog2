package integrador.prog2.entities;
import static integrador.prog2.entities.Validador.*;


public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) {
        super();
        setCantidad(cantidad);
        objetoNulo(producto,"producto");
        
        producto.validarVenta(cantidad);
        
        this.producto = producto;
        
        calcularSubtotal();
    }

    public int getCantidad() {
        return cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setCantidad(int cantidad) {
        //mayorQueCero(cantidad,"cantidad");
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetallePedido{" + "cantidad=" + cantidad + ", subtotal=" + subtotal + '}';
    }
    
    public boolean validar(Producto producto){
        objetoNulo(producto,"producto");
        if (producto.getDisponible()){
            System.out.println("Hay stock disponible para validar la compra");
            return cantidad<= producto.getStock();
        }else{
            System.out.println("No hay stock en este momento.(stock actual 0)");
            return false;
        }
    }
    
    public void calcularSubtotal(){
        this.subtotal = cantidad * producto.getPrecio();
    }

    public Producto getProducto() {
        return producto;
    }
    
}

