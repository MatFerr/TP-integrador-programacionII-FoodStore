package services;

import dao.ProductoDaoImpl;
import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import enums.Rol;
import exception.DatosInválidosException;
import exception.OperacionValidaException;
import exception.PedidoInválidoException;
import exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import repository.UsuarioRepository;

public class PedidoServiceImpl {
    ProductoDaoImpl productoDao = new ProductoDaoImpl();
    UsuarioRepository usuarioRepository = new UsuarioRepository();
    
    //Generadores automaticos de IDs
    private static Long idGeneradorPedido = 0000000L; 
    private static Long idGeneradorDetallePedido = 0000000L; 
    
    //USUARIO SERVICE
    public boolean validarUsuario(Usuario u, boolean update)throws Exception{
        boolean valido = false;
        if(!update){
            if(u.getId() != null){
                throw new DatosInválidosException("| El usuario ya existe |");
            }
        }else{
            if(u.getId() == null){
                throw new DatosInválidosException("| El usuario no existe |");
            }
        }
        if (u.getEmail() == null || !u.getEmail().contains("@")) {
            throw new DatosInválidosException("| El formato del correo electronico es invalido. |");
        }
        if (usuarioRepository.existeEmail(u.getEmail(), u)) {
            throw new DatosInválidosException("| El correo electronico ya esta registrado. |");
        }
        if (u.getNombre() == null || u.getNombre().trim().equals("")) {
            throw new DatosInválidosException("| El nombre es invalido. |");
        }
        if (u.getApellido() == null || u.getApellido().trim().equals("")) {
            throw new DatosInválidosException("| El apellido es invalido.|");
        }
        if (u.getCelular() == null || u.getCelular().trim().equals("")) {
            throw new DatosInválidosException("| El celular es invalido. |");
        }
        if (u.getContraseña() == null || u.getContraseña().trim().equals("")) {
            throw new DatosInválidosException("| La contraseña es invalida. |");
        }
        if (u.getRol() != Rol.ADMIN && u.getRol() != Rol.USUARIO){
            throw new DatosInválidosException("| El rol es invalido |");
        }
        valido = true;
        return valido;
    }
    public void saveUsuario(Usuario u, boolean update) throws Exception{
        validarUsuario(u, update); 
        usuarioRepository.save(u, update);
    }       
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarActivos();
    }
    public Usuario findUsuario(Long id){
        if(id == null){
            throw new DatosInválidosException("| El id no es válido |");
        }
        return usuarioRepository.findById(id);
    }
    public void eliminarUsuario(Long id) throws Exception {
        Usuario u = usuarioRepository.findById(id);
        if (u == null) throw new ResourceNotFoundException("| Usuario no encontrado en memoria con ID: " + id + " |");
        for(Pedido pedidoAsociado : u.getPedidos()){
            pedidoAsociado.setEstado(Estado.CANCELADO);
            pedidoAsociado.setEliminado(true);
        }
        usuarioRepository.eliminarLogico(id);
    }
    public void mostrarUsuario(Usuario u){
        System.out.println("\n| Id del Usuario: '" + u.getId() + "' |" );
        System.out.println("| Nombre del Usuario: '" + u.getNombre() + " " + u.getApellido() + "' |" );
        System.out.println("| Telefono del Usuario: '" + u.getCelular() + "' |" );
        System.out.println("| Email del Usuario: '" + u.getEmail() + "' |" );
        System.out.println("| Rol del Usuario: '" + u.getRol().name() + "' |" );
        System.out.println("| Vigencia del Usuario: '" + (u.isEliminado() ? "Expirada" : "Vigente")  + "' |" );
    }
    
    
    //PEDIDO SERVICE
    
    public List<Pedido> listarPedidos() {
        List<Pedido> acumulados = new ArrayList<>();
        for (Usuario u : usuarioRepository.listarActivos()) {
            for (Pedido p : u.getPedidos()) {
                    acumulados.add(p);
            }
        }
        return acumulados;
    }
    public List<Pedido> listarPedidosPorUsuario(Long id) {
        List<Pedido> acumulados = new ArrayList<>();
        for (Usuario u : usuarioRepository.listarActivos()) {
            if(u.getId().equals(id)){
               for (Pedido p : u.getPedidos()) {
                   
                    acumulados.add(p);
                } 
            }     
        }
        return acumulados;
    }
    public Pedido buscarPedidoMemoria(Long pedidoId) {
        for (Usuario u : usuarioRepository.listarActivos()) {
            for (Pedido p : u.getPedidos()) {
                if (p.getId().equals(pedidoId) && !p.isEliminado()){
                    return p;
                }
            }
        }
        return null;
    }
    public void mostrarPedido(Pedido p){
        System.out.println("\n| PEDIDO N° '" + p.getId() + "' |");
         System.out.println("\n| Id del Pedido: '" + p.getId() + "' |" );
         System.out.println("| Fecha de creacion del Pedido: '" + p.getFecha() + "' |" );
         System.out.println("| Estado del Pedido: '" + p.getEstado() + "' |" );
         System.out.println("| Total del Pedido: '" + p.getTotal() + "' |" );
         System.out.println("| Forma de pago: '" + p.getFormaPago().name() + "' |" );
         if(!p.getDetalles().isEmpty()){
             System.out.println("\n| DETALLES DEL PEDIDO |" );
             for(DetallePedido dt : p.getDetalles()){
                System.out.println("\n| Id del Detalle: '" + dt.getId() + "' |" );
                System.out.println("| Subtotal del Detalle: '" + dt.getSubtotal() + "' |" );
                System.out.println("| Cantidad del Detalle: '" + dt.getCantidad() + "' |" );
                System.out.println("| Nombre del Producto del Detalle: '" + (dt.getProducto() != null ? dt.getProducto().getNombre() : "Producto no disponible") + "' |" );
              }
         }
         
             
    }
            
    public List<DetallePedido> obtenerDetallesPedidos(Long id){
        Pedido p = buscarPedidoMemoria(id);
        if(p != null){
            return p.getDetalles();
        }else{
            throw new ResourceNotFoundException("| Pedido ID " + id + " no encontrado. |");
        }
    }
    public DetallePedido obtenerDetallePedido(Long id){
        List<Pedido> pedidosVigentes = listarPedidos();
        for(Pedido p : pedidosVigentes){
            for(DetallePedido dt : p.getDetalles()){
                if(dt.getId().equals(id)){
                    return dt;
                }
            }
        }
        return null;
    }
    public void eliminarPedidoLogico(Long pedidoId) throws Exception {
        Pedido pedido = buscarPedidoMemoria(pedidoId);
        if (pedido == null) throw new ResourceNotFoundException("| Pedido ID " + pedidoId + " no encontrado. |");

        if (pedido.getEstado() == Estado.TERMINADO) {
            productoDao.reponerStock(pedido.getDetalles());
        }else{
            pedido.setEstado(Estado.CANCELADO);
        }
        pedido.setEliminado(true);
        
        for (DetallePedido dp : pedido.getDetalles()) {
            dp.setEliminado(true);
        }
        throw new OperacionValidaException("| Pedido eliminado correctamente |");
    }
    public boolean metodoPagoVálido(String texto) {
        for (FormaPago fp : FormaPago.values()) {
            if (fp.name().equalsIgnoreCase(texto)) {
                return true;
            }
        }
        return false;
    }
    public void crearPedido(Usuario usuario, List<DetallePedido> detalles, FormaPago formaPago){
        Usuario usuarioExistente = usuarioRepository.findById(usuario.getId());
        if(usuarioExistente == null){
            throw new PedidoInválidoException("| Usuario no existente |");
        }
        List<DetallePedido> detallesVigentes = new ArrayList<>();
        for(DetallePedido dt : detalles){
            if(!dt.isEliminado()){
                detallesVigentes.add(dt);
            }
        }
        if(detallesVigentes.isEmpty()){
            throw new PedidoInválidoException("| No hay detalles del pedido adjuntados |");
        }
        if(formaPago == null || !metodoPagoVálido(formaPago.name())){
            throw new PedidoInválidoException("| La forma de pago no es válida |");
        }
        
        Pedido pedido = new Pedido();
        pedido.setEliminado(false);
        pedido.setId(idGeneradorPedido);
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(Estado.PENDIENTE);
        pedido.setFormaPago(formaPago);
        pedido.setDetalles(detallesVigentes);
        pedido.calcularTotal();
        
        usuarioExistente.addPedido(pedido);
        idGeneradorPedido++;
        throw new OperacionValidaException("| Pedido creado correctamente |");
    }
    public void confirmarPedido(Pedido p){
        if(buscarPedidoMemoria(p.getId()) == null){
            throw new PedidoInválidoException("| No hay un pedido existente para el id |");
        }
        if(p.getEstado() != Estado.PENDIENTE){
             throw new PedidoInválidoException("| El pedido no esta en estado pendiente |");
        }
        // No es parte de lo solicitado en el proyecto, pero aqui se realizaria una confirmacion del pago por parte del usuario
        p.setEstado(Estado.CONFIRMADO);
        
    }
    
    public void crearPedidoTransaccional(Pedido p) throws Exception{
        if(buscarPedidoMemoria(p.getId()) == null){
            throw new PedidoInválidoException("| No hay un pedido existente para el id |");
        }
        if(p.getDetalles().isEmpty()){
            throw new PedidoInválidoException("| No hay detalles del pedido adjuntados |");
        }
        if(p.getEstado() != Estado.CONFIRMADO){
             throw new PedidoInválidoException("| El pedido no cumple con un estado válido para realizar la transaccion |");
        }
        
        productoDao.cambioDeStock(p.getDetalles());
        p.setEstado(Estado.TERMINADO);
        throw new OperacionValidaException("| Pedido confirmado correctamente |");
        
    }
   
    //Metodos usando el detalle Pedido
    
            
    public void añadirDetallePedido(Long idPedido, int cantidad, Long idProducto) throws Exception{
        Pedido pedidoExistente = buscarPedidoMemoria(idPedido);
        
        if(pedidoExistente.getEstado() != Estado.PENDIENTE){
             throw new PedidoInválidoException("| No se pueden agregar nuevos detalles al pedido |");
        }
        Producto productoExistente = productoDao.findById(idProducto);
        if(productoExistente.getStock() <= 0){
            throw new PedidoInválidoException("| El stock del producto no es válido |");
        }
        pedidoExistente.addDetallePedido(cantidad, productoExistente.getPrecio(), productoExistente);
        throw new OperacionValidaException("| Detalle del pedido agregado perfectamente |");
    }
    
    public void eliminarDetallePedido(Long idPedido, Long idProducto) throws Exception{
        Pedido pedidoExistente = buscarPedidoMemoria(idPedido);
        if(pedidoExistente == null){
             throw new PedidoInválidoException("| No hay un pedido existente para el id |");
        }
        if(pedidoExistente.getEstado() != Estado.PENDIENTE){
             throw new PedidoInválidoException("| No se pueden eliminar detalles dels pedido |");
        }
        Producto productoExistente = productoDao.findById(idProducto);
        if(productoExistente == null){
            throw new PedidoInválidoException("| No hay un producto existente para el id |");
        }
        pedidoExistente.deleteDetalleProductoByProducto(productoExistente);
        if(pedidoExistente.getDetalles().isEmpty()){
            pedidoExistente.setEstado(Estado.CANCELADO);
        }
        throw new OperacionValidaException("| Detalle del pedido eliminado perfectamente |");
    }
    
    public DetallePedido findDetallePedidoByProductoId(Long idPedido, Long idProducto) throws Exception{
        Pedido pedidoExistente = buscarPedidoMemoria(idPedido);
        if(pedidoExistente == null){
             throw new PedidoInválidoException("| No hay un pedido existente para el id |");
        }
        if(pedidoExistente.getEstado() == Estado.CANCELADO || pedidoExistente.getEstado() == Estado.TERMINADO){
             throw new PedidoInválidoException("| No se pueden agregar nuevos detalles al pedido |");
        }
        Producto productoExistente = productoDao.findById(idProducto);
        if(productoExistente == null){
            throw new PedidoInválidoException("| No hay un producto existente para el id |");
        }
        return pedidoExistente.findDetallePedidoByProducto(productoExistente);
    }
    public void mostrarDetallePedido(DetallePedido dt){
        System.out.println("\n| DETALLES DEL PEDIDO |" );
        System.out.println("\n| Id del Detalle: '" + dt.getId() + "' |" );
        System.out.println("| Subtotal del Detalle: '" + dt.getSubtotal() + "' |" );
        System.out.println("| Cantidad del Detalle: '" + dt.getCantidad() + "' |" );
        System.out.println("| Producto del Detalle | Nombre: '" + dt.getProducto().getNombre() + "' | ID: '" + dt.getProducto().getId() + "' |" );
              
    }
    
    
}
