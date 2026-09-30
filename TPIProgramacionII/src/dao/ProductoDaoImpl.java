package dao;

import config_.DatabaseConfig;
import entities.DetallePedido;
import entities.Producto;
import exception.ConexionFallidaException;
import exception.DatosInválidosException;
import exception.OperacionValidaException;
import exception.ProductoInválidoException;
import exception.StockInsuficienteException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ProductoDaoImpl implements GenericDAO<Producto>{
    @Override
    public void update(Producto p) throws Exception{
        String sql = "UPDATE productos SET nombre = ?, precio = ?, descripcion = ?, stock = ?, imagen = ?, disponible = ? WHERE id = ? AND eliminado = FALSE";
        
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setString(3, p.getDescripcion());
            stmt.setInt(4, p.getStock());
            stmt.setString(5, p.getImagen());
            stmt.setBoolean(6, p.isDisponible());
            stmt.setLong(7, p.getId());
            stmt.executeUpdate();
            throw new OperacionValidaException("| Producto actualizado correctamente |");
        }catch (SQLException e) {
            throw new ProductoInválidoException("| Error al actualizar producto. |", e);
        }
    }
    public void create(Producto p, Long categoriaId) throws Exception{
        String sql = "INSERT INTO productos (nombre, precio, descripcion, stock, imagen, disponible, eliminado, created_at, categoria_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, p.getNombre());
            stmt.setDouble(2, p.getPrecio());
            stmt.setString(3, p.getDescripcion());
            stmt.setInt(4, p.getStock());
            stmt.setString(5, p.getImagen());
            stmt.setBoolean(6, p.isDisponible());
            stmt.setBoolean(7, p.isEliminado());
            stmt.setTimestamp(8, Timestamp.valueOf(p.getCreatedAt()));
            stmt.setLong(9, categoriaId);
            stmt.executeUpdate();
            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    p.setId(rs.getLong(1));
                    throw new OperacionValidaException("| Producto creado correctamente |");
                }
            }
        }catch(SQLException e){
            throw new ProductoInválidoException("| Error al crear producto. |", e);
        }
    }
    @Override
    public Producto findById(Long id) throws Exception{
        String sql = "SELECT * FROM productos WHERE id = ? AND eliminado = FALSE";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return new Producto(rs.getString("nombre"), rs.getString("descripcion"),  rs.getDouble("precio"), rs.getInt("stock"), rs.getString("imagen"), rs.getBoolean("disponible"), rs.getLong("id"), rs.getBoolean("eliminado"), (rs.getTimestamp("created_at").toLocalDateTime() != null ? rs.getTimestamp("created_at").toLocalDateTime() : LocalDateTime.now()));
                }
            }
        } catch (SQLException e) {
            throw new ProductoInválidoException("| Error al buscar el producto |.", e);
        }
        return null;
    }
    @Override
    public List<Producto> findAll() throws Exception{
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE eliminado = FALSE";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Producto(rs.getString("nombre"), rs.getString("descripcion"),  rs.getDouble("precio"), rs.getInt("stock"), rs.getString("imagen"), rs.getBoolean("disponible"), rs.getLong("id"), rs.getBoolean("eliminado"), (rs.getTimestamp("created_at").toLocalDateTime() != null ? rs.getTimestamp("created_at").toLocalDateTime() : LocalDateTime.now())));
            }
        } catch (SQLException e) {
            throw new ProductoInválidoException("| Error al consultar productos. |", e);
        }
        return lista;
    }
    @Override
    public void deleteLogico(Long id) throws Exception{
         String sql = "UPDATE productos SET eliminado = TRUE WHERE id = ?";
         try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);){
             stmt.setLong(1, id);
             stmt.executeUpdate();
             throw new OperacionValidaException("| Producto eliminado correctamente |");
            } catch (SQLException e) {
            throw new ProductoInválidoException("| Error al eliminar el producto. |", e);
        }
    }
    
    public List<Producto> listarProductoByCategoriaId(Long categoriaId) throws Exception{
         List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE categoria_id = ? AND eliminado = FALSE";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setLong(1, categoriaId);
            try( ResultSet rs = stmt.executeQuery()){
                 while (rs.next()) {
                lista.add(new Producto(rs.getString("nombre"), rs.getString("descripcion"),  rs.getDouble("precio"), rs.getInt("stock"), rs.getString("imagen"), rs.getBoolean("disponible"), rs.getLong("id"), rs.getBoolean("eliminado"), (rs.getTimestamp("created_at").toLocalDateTime() != null ? rs.getTimestamp("created_at").toLocalDateTime() : LocalDateTime.now())));
            }
            }
           
        } catch (SQLException e) {
            throw new ProductoInválidoException("| Error al consultar productos. |", e);
        }
        return lista;
    }
    public boolean existeProducto(Producto p, Long idExcluido) throws Exception{
         String sql = "SELECT COUNT(*) FROM productos WHERE LOWER(nombre)= LOWER(?) AND eliminado = FALSE";
         if(idExcluido != null){
            sql += " AND id != ?";
        }
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, p.getNombre());
            if(idExcluido != null){
            stmt.setLong(2, idExcluido);
            }
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1) > 0;
                }
            }
            }catch(SQLException e){
            throw new ProductoInválidoException("| Hubo un error al conectar la BD | " + e.getMessage());
        }
        return false;
    }
    public void reasignarCategoria(Long productoId, Long categoriaId) throws Exception{
        String sql = "UPDATE productos SET categoria_id = ? WHERE id = ? AND eliminado = FALSE";
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, categoriaId);
            stmt.setLong(2, productoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ProductoInválidoException("| Error al reasignar la categoria del producto. |", e);
        }
    }
    public int contarProductosActivosPorCategoria(Long categoriaId) throws Exception{
        String sql = "SELECT COUNT(*) FROM productos WHERE categoria_id = ? AND eliminado = FALSE";
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, categoriaId);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }catch(SQLException e){
            throw new ProductoInválidoException("| Error al reasignar la categoria del producto. |", e);
        }
        return 0;
    }
    public void cambioDeStock(List<DetallePedido> detalles) throws Exception{
        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            String sqlSelect = "SELECT nombre, stock FROM productos WHERE id = ? AND eliminado = FALSE";
            String sqlUpdate = "UPDATE productos SET stock = ? WHERE id = ? AND eliminado = FALSE";
            
            try(PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)   ){
                for(DetallePedido dt : detalles){
                    stmtSelect.setLong(1, dt.getProducto().getId());
                    try(ResultSet rs = stmtSelect.executeQuery()){
                        if(rs.next()){
                           int stockBD = rs.getInt("stock");
                           if(dt.getCantidad() > stockBD){
                               throw new StockInsuficienteException("| La cantidad solicitada del producto no puede ser mayor a la almacenada en memoria |");                              
                           }
                           int nuevoStock = stockBD - dt.getCantidad();
                           stmtUpdate.setInt(1, nuevoStock);
                           stmtUpdate.setLong(2, dt.getProducto().getId());
                           stmtUpdate.executeUpdate();
                        }
                        
                    }
                }
            }
            conn.commit();
        }catch(Exception e){
            if(conn != null){
                try{
                conn.rollback();
                }catch(SQLException ex){
                    throw new ProductoInválidoException("| Hubo un error al intentar cambiar el stock de los productos | " + e.getMessage()); 
                }
            }
                throw e;
            
                           
        }finally{
            if(conn != null){
                try{
                 conn.setAutoCommit(true);
                conn.close();   
                }catch(SQLException ex){
                    throw ex;
                }
                
            } 
        }
    }
    public void reponerStock(List<DetallePedido> detalles) throws Exception {
        Connection conn = null;
        try{
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            String sql = "UPDATE productos SET stock = stock + ? WHERE id = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (DetallePedido dp : detalles) {
                    stmt.setInt(1, dp.getCantidad());
                    stmt.setLong(2, dp.getProducto().getId());
                    stmt.executeUpdate();
                }
            } 
            conn.commit();
        }catch(SQLException e){
            if(conn != null){
                try{
                conn.rollback();
                }catch(SQLException ex){
                    throw new ProductoInválidoException("| Hubo un error al intentar cambiar el stock de los productos | " + e.getMessage()); 
                }
            }
              throw new ProductoInválidoException("| Hubo un error al reponer el stock de los productos: | " + e.getMessage(), e);             
        }finally{
            if(conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                }catch(SQLException e){
                    throw e;
                }
                
            } 
        }
    }
}
