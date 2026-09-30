package services;

import dao.CategoriaDaoImpl;
import dao.ProductoDaoImpl;
import entities.Categoria;
import entities.Producto;
import exception.CategoriaInválidoException;
import exception.DatosInválidosException;
import java.util.List;

public class CategoriaServiceImpl {
    CategoriaDaoImpl dao = new CategoriaDaoImpl();
    ProductoDaoImpl daoProducto = new ProductoDaoImpl();
    
    public void update(Categoria c) throws Exception{
        if(c.getId() == null){
            throw new DatosInválidosException("| La categoria a actualizar no existe |");
        }
        if(c.getNombre().trim().equals("") || c.getNombre() == null){
            throw new DatosInválidosException("| El nombre de la categoria a actualizar no es válido |");
        }
        if(c.getDescripcion().trim().equals("") || c.getNombre() == null){
            throw new DatosInválidosException("| La descripcion de la categoria a actualizar no es válida |");
        }
        Categoria catBD = dao.findById(c.getId());
        if(catBD == null || catBD.isEliminado()){
            throw new CategoriaInválidoException("| La categoria a actualizar no existe |");
        }
        if(dao.existeCategoria(c.getNombre(), c.getId())){
            throw new DatosInválidosException("| La categoria a actualizar ya existe |");
        }
        dao.update(c);
    }
    
    public void create(Categoria c) throws Exception{
        if(c.getId() != null){
            throw new DatosInválidosException("| La categoria a crear ya existe |");
        }
        if(c.getNombre().trim().equals("") || c.getNombre() == null){
            throw new DatosInválidosException("| El nombre de la categoria a crear no es válido |");
        }
        if(c.getDescripcion().trim().equals("") || c.getNombre() == null){
            throw new DatosInválidosException("| La descripcion de la categoria a crear no es válida |");
        }
        if(dao.existeCategoria(c.getNombre(), c.getId())){
            throw new DatosInválidosException("| La categoria a crear ya existe |");
        }
        dao.save(c);
    }
    
    public Categoria findById(Long id) throws Exception{
        if(id > 0){
            return dao.findById(id);
        }
        return null;
    }
    public List<Categoria> findAll() throws Exception{
        List<Categoria> categorias = dao.findAll();
        return categorias;
    }
    
    public void deleteLogico(Long id) throws Exception{
        if(id > 0){
            Categoria categoriaBD = dao.findById(id);
        if(categoriaBD != null && !categoriaBD.isEliminado()){
            List<Producto> productosAsociados = daoProducto.listarProductoByCategoriaId(id);
            if(!productosAsociados.isEmpty()){
                throw new CategoriaInválidoException("| La categoria ha eliminar tiene productos asociados |");
            }else{
                dao.deleteLogico(id);
            }
        }else{
            throw new CategoriaInválidoException("| La categoria no se ha podido eliminar |");
        }
        }
        
    }
    public Categoria obtenerProductosPorCategoria(Long id) throws Exception{
        Categoria categoriaBD = dao.findById(id);
        if(categoriaBD == null || categoriaBD.isEliminado()){
            throw new CategoriaInválidoException("| La categoria no existe |");
        }else{
            List<Producto> productosAsociados = daoProducto.listarProductoByCategoriaId(id);
            categoriaBD.setProductos(productosAsociados);
            return categoriaBD;
        }
    }
    public Categoria obtenerCategoriaPorNombreId(String ans, List<Categoria> categoriasActivas){
         if(ans.trim().equals("")){
            throw new DatosInválidosException("| Valor ingresado no válido |");
        }
        
        for(Categoria c : categoriasActivas){
                if(c.getNombre().toLowerCase().equals(ans.toLowerCase()) || String.valueOf(c.getId()).equals(ans)){
                    return c;
                }
        }
        return null;
    }
    public void mostrarCategoria(Categoria c){
     System.out.println("\n| Id de la Categoria: '" + c.getId() + "' |" );
     System.out.println("| Nombre de la Categoria: '" + c.getNombre() + "' |" );
     System.out.println("| Descripcion de la Categoria: '" + c.getDescripcion() + "' |" );
     System.out.println("| Vigencia de la Categoria: '" + (c.isEliminado() ? "No Disponible" : "Disponible")  + "' |" );
    }
}