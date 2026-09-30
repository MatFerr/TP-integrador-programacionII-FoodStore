package exception;

public class ProductoInválidoException extends FoodStoreException {
    public ProductoInválidoException(String mensaje) {
        super("|ERROR PRODUCTO| " + mensaje);
    }
        public ProductoInválidoException(String mensaje, Throwable error) {
        super("|ERROR PRODUCTO| " + mensaje, error);
    }

}
