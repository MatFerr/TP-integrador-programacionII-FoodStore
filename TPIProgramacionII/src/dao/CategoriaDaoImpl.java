package dao;

import config_.DatabaseConfig;
import entities.Categoria;
import exception.ConexionFallidaException;
import exception.OperacionValidaException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl implements GenericDAO<Categoria> {
    @Override
    public void update(Categoria c) throws Exception{
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? " +
                     "WHERE id = ? AND eliminado = FALSE";
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDescripcion());
            stmt.setLong(3, c.getId());
            stmt.executeUpdate();   
            throw new OperacionValidaException("| Categoria actualizada correctamente | ");
        }catch(SQLException e){
            throw new ConexionFallidaException("| Hubo un error al conectarse a la BD | " + e.getMessage());
        }
    }
    public void save(Categoria c) throws Exception{
        String sql = "INSERT INTO categorias (nombre, descripcion, eliminado, created_at) VALUES (?, ?, ?, ?)";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, c.getNombre());
            stmt.setString(2, c.getDescripcion());
            stmt.setBoolean(3, c.isEliminado());
            stmt.setTimestamp(4, Timestamp.valueOf(c.getCreatedAt()));
            stmt.executeUpdate();
            try(ResultSet rs = stmt.getGeneratedKeys()){
                if(rs.next()){
                    c.setId(rs.getLong(1));
                    throw new OperacionValidaException("| Categoria guardada correctamente | ");
                }
            }
        } catch (SQLException e) {
            throw new ConexionFallidaException("| Error al insertar la categoria en la base de datos. | ", e);
        }
    }
    @Override
    public void deleteLogico(Long id) throws Exception{
        String sql = "UPDATE categorias SET eliminado = TRUE WHERE id = ?";
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, id); 
             stmt.executeUpdate(); 
             throw new OperacionValidaException("| Categoria eliminada correctamente | ");
        }catch(SQLException e){
            throw new ConexionFallidaException("| Hubo un error al realizar la baja lógica | " + e.getMessage());
        }
    }
    @Override
    public Categoria findById(Long id) throws Exception{
        String sql = "SELECT * FROM categorias WHERE id = ? AND eliminado = FALSE";
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, id); 
             try(ResultSet rs = stmt.executeQuery()){
                 if(rs.next()){
                     return new Categoria(rs.getLong("id"), rs.getBoolean("eliminado"), (rs.getTimestamp("created_at").toLocalDateTime() != null ? rs.getTimestamp("created_at").toLocalDateTime() : LocalDateTime.now()) , rs.getString("nombre"), rs.getString("descripcion") );
                 }
             } 
        }catch(SQLException e){
            throw new ConexionFallidaException("| Hubo un error al conectar la BD | " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Categoria> findAll() throws Exception{
        String sql = "SELECT * FROM categorias WHERE eliminado = FALSE";
        List<Categoria> categorias = new ArrayList<>();
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){ 
             try(ResultSet rs = stmt.executeQuery()){
                 while(rs.next()){
                     categorias.add(new Categoria(rs.getLong("id"), rs.getBoolean("eliminado"), (rs.getTimestamp("created_at").toLocalDateTime() != null ? rs.getTimestamp("created_at").toLocalDateTime() : LocalDateTime.now()) , rs.getString("nombre"), rs.getString("descripcion") )); 
                 }
             } 
        }catch(SQLException e){
            throw new ConexionFallidaException("| Hubo un error al conectar la BD | " + e.getMessage());
        }
        return categorias;
    }
    
    
    public boolean existeCategoria(String nombre, Long idExcluido){
        String sql = "SELECT COUNT(*) FROM categorias WHERE LOWER(nombre)= LOWER(?) AND eliminado = FALSE";
        if(idExcluido != null){
            sql += " AND id != ?";
        }
        try(Connection conn = DatabaseConfig.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, nombre);
            if(idExcluido != null){
            stmt.setLong(2, idExcluido);
            }
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1) > 0;
                }
            }
            }catch(SQLException e){
            throw new ConexionFallidaException("| Hubo un error al conectar la BD | " + e.getMessage());
        }
        return false;
    }
    
}
