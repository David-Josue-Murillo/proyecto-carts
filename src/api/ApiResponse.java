package api;

import model.Cart;

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
