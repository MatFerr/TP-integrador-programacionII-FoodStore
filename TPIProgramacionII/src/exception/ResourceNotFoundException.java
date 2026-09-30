package exception;

public class ResourceNotFoundException extends FoodStoreException {
    public ResourceNotFoundException(String mensaje) {
        super("|NO HAY RESULTADOS| " + mensaje);
    }

}
