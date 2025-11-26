package model;

public class Cart {
    private int id;
    private double total;
    private double discountedTotal;
    private int userId;
    private int totalProducts;
    private int totalQuantity;

    public Cart(int id, double total, double discountedTotal, int userId, int totalProducts, int totalQuantity) {
        this.id = id;
        this.total = total;
        this.discountedTotal = discountedTotal;
        this.userId = userId;
        this.totalProducts = totalProducts;
        this.totalQuantity = totalQuantity;
    }

    public int getId() {
        return id;
    }

    public Cart setId(int id) {
        this.id = id;
        return this;
    }

    public double getTotal() {
        return total;
    }

    public Cart setTotal(double total) {
        this.total = total;
        return this;
    }

    public double getDiscountedTotal() {
        return discountedTotal;
    }

    public Cart setDiscountedTotal(double discountedTotal) {
        this.discountedTotal = discountedTotal;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Cart setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public Cart setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
        return this;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public Cart setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
        return this;
    }
}
