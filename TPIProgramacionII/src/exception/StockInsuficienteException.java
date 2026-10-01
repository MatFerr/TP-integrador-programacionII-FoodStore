package exception;

public class StockInsuficienteException extends FoodStoreException {
    public StockInsuficienteException(String mensaje) {
        super("|STOCK INSUFICIENTE| " + mensaje);
    }
}
