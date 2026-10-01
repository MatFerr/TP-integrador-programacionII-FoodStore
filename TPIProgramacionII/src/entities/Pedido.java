package entities;

import enums.Estado;
import enums.FormaPago;
import exception.DatosInválidosException;
import exception.StockInsuficienteException;
import interfaces.Calculable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable{
    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private List<DetallePedido> detalles;

    public Pedido(LocalDate fecha, Estado estado, Double total, FormaPago formaPago, Long id, boolean eliminado, LocalDateTime createdAt) {
        super(id, eliminado, createdAt);
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.formaPago = formaPago;
        detalles = new ArrayList<>();
    }

    public Pedido() {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
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

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }
    
    public void addDetallePedido(int cantidad, double precioUnitario, Producto producto){
        if(precioUnitario > 0 && cantidad > 0){
            if(cantidad > producto.getStock()){
                throw new StockInsuficienteException("La cantidad a solicitar del producto no puede ser mayor que la del stock");
            }else{
                double subtotal = cantidad * precioUnitario;
            detalles.add(new DetallePedido(cantidad, subtotal, producto));
            calcularTotal();
            }  
        }else{
            throw new DatosInválidosException("El precio o la cantidad no pueden ser menores o iguales a 0 para realizar un pedido");
        }
    }
    
    public DetallePedido findDetallePedidoByProducto(Producto producto){
        if(producto != null){
            for(DetallePedido dt : detalles){
                if(dt.getProducto() != null && dt.getProducto().getId().equals(producto.getId())){
                    return  dt;
                }
            }
        }
        return null;
    }
    
    public void deleteDetalleProductoByProducto(Producto producto){
        DetallePedido aEliminar = findDetallePedidoByProducto(producto);
        if(aEliminar != null){
            this.detalles.remove(aEliminar);
        }
        calcularTotal();
    }
   
    
    @Override
    public void calcularTotal(){
        if(!detalles.isEmpty()){
            double valorAcum = 0.0;
            for(DetallePedido dp : detalles){
                valorAcum += dp.getSubtotal();
            }
            this.total = valorAcum;
        }
    }

    @Override
    public String toString() {
        return "Pedido{" + "fecha=" + fecha + ", estado=" + estado + ", total=" + total + ", formaPago=" + formaPago + ", detalles=" + detalles + '}';
    }
    
    
    
}
