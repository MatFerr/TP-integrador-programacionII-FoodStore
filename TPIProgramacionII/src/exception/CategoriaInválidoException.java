package exception;

public class CategoriaInválidoException extends FoodStoreException {
    public CategoriaInválidoException(String mensaje) {
        super("|ERROR CATEGORIA| " + mensaje);
    }
}
