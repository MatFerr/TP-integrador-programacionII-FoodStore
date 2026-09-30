package exception;

public class PedidoInválidoException extends FoodStoreException {
    public PedidoInválidoException(String mensaje) {
        super("|ERROR PEDIDO| " + mensaje);
    }
    public PedidoInválidoException(String mensaje, Throwable error) {
        super("|ERROR PEDIDO| " + mensaje, error);
    }
}
