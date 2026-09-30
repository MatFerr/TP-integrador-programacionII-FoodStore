package exception;

public class DatosInválidosException extends FoodStoreException{
   public DatosInválidosException(String mensaje) {
        super("|DATOS INVÁLIDOS| " + mensaje);
    } 
   public DatosInválidosException(String mensaje, Throwable error) {
        super("|DATOS INVÁLIDOS| " + mensaje, error);
    } 
}
