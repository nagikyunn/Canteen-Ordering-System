package canteen_project.model;

public class OrderItem {
    private MenuItem menuItem;
    private int quantity;
    private double subtotal;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.subtotal = computeSubtotal();
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double computeSubtotal() {
        this.subtotal = menuItem.getPrice() * quantity;
        return this.subtotal;
    }
}