package exception;

public class FoodStoreException extends RuntimeException {
    public FoodStoreException (String mensaje){
        super(mensaje);
    }
    public FoodStoreException (String mensaje, Throwable error){
        super(mensaje, error);
    }
}
