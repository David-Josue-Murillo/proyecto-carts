// converte el json en objeto java
import java.util.List;

public class ApiResponse {

    private List<Cart> carts;

    public ApiResponse() {}

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
