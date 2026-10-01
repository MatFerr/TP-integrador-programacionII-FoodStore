package exception;

public class ConexionFallidaException extends FoodStoreException {
    public ConexionFallidaException(String mensaje) {
        super("|DATOS INVÁLIDOS| " + mensaje);
    } 
    public ConexionFallidaException(String mensaje, Throwable error) {
        super("|DATOS INVÁLIDOS| " + mensaje, error);
    } 
}
