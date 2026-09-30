package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Categoria extends Base{
    private String nombre;
    private String descripcion;
    private List<Producto> productos = new ArrayList<>();
    
    public Categoria(Long id, boolean eliminado, LocalDateTime createdAt, String nombre, String descripcion){
        super(id, eliminado, createdAt);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Categoria(String nombre, String descripcion){
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Categoria{" + "id=" + super.getId() + ", nombre=" + nombre + ", descripcion=" + descripcion + ", productos=" + productos + ", eliminado=" + super.isEliminado() + ", Fecha Creacion=" + super.getCreatedAt() +  '}';
    }
    
}
