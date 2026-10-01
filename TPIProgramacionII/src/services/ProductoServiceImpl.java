package services;

import dao.CategoriaDaoImpl;
import dao.ProductoDaoImpl;
import entities.Categoria;
import entities.DetallePedido;
import entities.Producto;
import exception.CategoriaInválidoException;
import exception.DatosInválidosException;
import exception.ProductoInválidoException;
import exception.ResourceNotFoundException;
import java.util.List;

public class ProductoServiceImpl {
    ProductoDaoImpl dao = new ProductoDaoImpl();
    CategoriaDaoImpl daoCategoria = new CategoriaDaoImpl();
    
    public void update(Producto p, Long categoriaId) throws Exception{
        if(p.getId() == null){
            throw new DatosInválidosException("| El producto a actualizar no existe |");
        }
        if(p.getNombre().trim().equals("") || p.getNombre() == null){
             throw new DatosInválidosException("| El nombre del producto no es válido |");
        }
        if(p.getDescripcion().trim().equals("") || p.getDescripcion() == null){
             throw new DatosInválidosException("| La descripcion del producto no es válida |");
        }
        if(p.getPrecio() < 0){
             throw new DatosInválidosException("| El precio del producto no es válido |");
        }
        if(p.getStock() < 0){
             throw new DatosInválidosException("| El precio del producto no es válido |");
        }
        if(p.getImagen().trim().equals("") || p.getImagen() == null){
             throw new DatosInválidosException("| La imagen del producto no es válida |");
        }
        Producto productoBD = dao.findById(p.getId());
        if(productoBD == null || !productoBD.isDisponible()){
            throw new ResourceNotFoundException("| El producto no existe |");
        }
        Categoria categoriaBD = daoCategoria.findById(categoriaId);
        if(categoriaBD == null || categoriaBD.isEliminado()){
            throw new ResourceNotFoundException("| La categoria del producto no es válida |");
        }
        if(dao.existeProducto(p, p.getId())){
            throw new DatosInválidosException("| El nombre del producto ya esta registrado |");
        }
        dao.update(p);
    }
    public void create(Producto p, Long categoriaId) throws Exception{
        if(p.getId() != null){
            throw new DatosInválidosException("| El producto a actualizar no existe |");
        }
        if(p.getNombre().trim().equals("") || p.getNombre() == null){
             throw new DatosInválidosException("| El nombre del producto no es válido |");
        }
        if(p.getDescripcion().trim().equals("") || p.getDescripcion() == null){
             throw new DatosInválidosException("| La descripcion del producto no es válida |");
        }
        if(p.getPrecio() < 0){
             throw new DatosInválidosException("| El precio del producto no es válido |");
        }
        if(p.getStock() < 0){
             throw new DatosInválidosException("| El precio del producto no es válido |");
        }
        if(p.getImagen().trim().equals("") || p.getImagen() == null){
             throw new DatosInválidosException("| La imagen del producto no es válida |");
        }
        Categoria categoriaBD = daoCategoria.findById(categoriaId);
        if(categoriaBD == null || categoriaBD.isEliminado()){
            throw new ResourceNotFoundException("| La categoria del producto no es válida |");
        }
        if(dao.existeProducto(p, null)){
            throw new DatosInválidosException("| El nombre del producto ya esta registrado |");
        }
        dao.create(p, categoriaId);
    }
    
    public void reasignarCategoria(Producto p, Long categoriaId) throws Exception{
        Producto productoBD = dao.findById(p.getId());
        if(productoBD == null || !productoBD.isDisponible()){
            throw new ProductoInválidoException("| El producto no existe |");
        }
        Categoria categoriaBD = daoCategoria.findById(categoriaId);
        if(categoriaBD == null || categoriaBD.isEliminado()){
            throw new CategoriaInválidoException("| La categoria a asignar no existe |");
        }
        dao.reasignarCategoria(p.getId(), categoriaId);
    }
    
    public List<Producto> listarProductosPorCategoria(Long idCategoria) throws Exception{
        if(idCategoria > 0){
            Categoria categoriaBD = daoCategoria.findById(idCategoria);
            if(categoriaBD == null || categoriaBD.isEliminado()){
                throw new CategoriaInválidoException("| La categoria a asignar no existe |");
            }
            List<Producto> productosCategoria = dao.listarProductoByCategoriaId(idCategoria);
            return productosCategoria;
        }else{
            throw new DatosInválidosException("| El id de la categoria no es válido |");
        }
    }
    public Producto findById(Long id) throws Exception{
        if(id > 0){
            return dao.findById(id);
        }else{
            throw new DatosInválidosException("| El id del producto no es válido |");
        }
    }
    public List<Producto> findAll() throws Exception{
        return dao.findAll();
    }
    public void mostrarProducto(Producto p){
     System.out.println("\n| Id del Producto: '" + p.getId() + "' |" );
     System.out.println("| Nombre del Producto: '" + p.getNombre() + "' |" );
     System.out.println("| Descripcion del Producto: '" + p.getDescripcion() + "' |" );
     System.out.println("| Url de la imagen del Producto: '" + p.getImagen() + "' |" );
     System.out.println("| Precio del Producto: '$" + p.getPrecio() + "' |" );
     System.out.println("| Stock del Producto: '" + p.getStock() + "' unidad/es |" );
     System.out.println("| Disponibilidad del Producto: '" + (p.isDisponible() ? "Disponible" : "No Disponible")  + "' |" );
    }
    public Producto obtenerProductoporIdNombre(String ans, List<Categoria> categoriasActivas){
        if(ans.trim().equals("")){
            throw new DatosInválidosException("| Valor ingresado no válido |");
        }
        
        for(Categoria c : categoriasActivas){
            for(Producto p : c.getProductos()){
                if(p.getNombre().toLowerCase().equals(ans.toLowerCase()) || String.valueOf(p.getId()).equals(ans)){
                    return p;
                }
            }
        }
        return null;
    }
    public Long obtenerCategoriaId(List<Categoria> categorias, Producto p){
        for(Categoria categoria : categorias){
            for(Producto productoCategoria : categoria.getProductos()){
                if(productoCategoria.equals(p)){
                    return categoria.getId();
                }
            }
        }
        return 0L;
    }
    public void deleteLogico(Long id, List<Categoria> categorias) throws Exception{
        if(id > 0){
            Producto productoBD = dao.findById(id);
            if(productoBD == null || !productoBD.isDisponible()){
                throw new ProductoInválidoException("| El producto no existe |");
            }
            dao.deleteLogico(id);
            removerProductoDeCategorias(categorias, productoBD);
        }else{
            throw new DatosInválidosException("| El id no es válido |");
        }
    }
    public void removerProductoDeCategorias(List<Categoria> categorias, Producto p){
        for(Categoria categoria : categorias){
            for(Producto productoCategoria : categoria.getProductos()){
                if(productoCategoria.equals(p)){
                    categoria.getProductos().remove(p);
                    break;
                }
            }
        }
    }
    public void registrarTransaccionStock(List<DetallePedido> detalles) throws Exception{
        if(!detalles.isEmpty()){
            for(DetallePedido dt : detalles){
                if(dt.getCantidad() <= 0){
                    throw new DatosInválidosException("| La cantidad del pedido de '" + dt.getProducto().getNombre() + "' no es válida |");
                }
                if(dt.getSubtotal() < 0){
                    throw new DatosInválidosException("| El subtotal del pedido de '" + dt.getProducto().getNombre() + "' no es válido |");
                }
                Producto productoBD = dao.findById(dt.getProducto().getId());
                if(productoBD == null || !productoBD.isDisponible()){
                    throw new ProductoInválidoException("| El producto no existe |");
                }
            }
            dao.cambioDeStock(detalles);
        }
    }
    
    public void reponerStock(List<DetallePedido> detalles) throws Exception{
        if(!detalles.isEmpty()){
            for(DetallePedido dt : detalles){
                if(dt.getCantidad() < 0){
                    throw new DatosInválidosException("| La cantidad del pedido de '" + dt.getProducto().getNombre() + "' no es válida |");
                }
                Producto productoBD = dao.findById(dt.getProducto().getId());
                if(productoBD == null || !productoBD.isDisponible()){
                    throw new ProductoInválidoException("| El producto no existe |");
                }
            }
            dao.reponerStock(detalles);
        }
    }
}
