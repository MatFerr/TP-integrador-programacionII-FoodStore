package exception;

public class OperacionValidaException extends RuntimeException {
    public OperacionValidaException (String mensaje){
        super("| EXITO | " + mensaje);
    }
    public OperacionValidaException (String mensaje, Throwable error){
        super("| EXITO | " + mensaje, error);
    }
}
