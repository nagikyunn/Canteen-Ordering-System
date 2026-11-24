package canteen_project.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String orderDate;
    private String status;
    private double totalPrice;
    private List<OrderItem> items;

    public Order(String orderId) {
        this.orderId = orderId;
        this.orderDate = LocalDate.now().toString();
        this.status = "Pending";
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public Order(String orderId, String orderDate, String status, double totalPrice, List<OrderItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        computeTotal();
    }

    public double computeTotal() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.computeSubtotal();
        }
        this.totalPrice = sum;
        return this.totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getItemsSummary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            OrderItem oi = items.get(i);
            sb.append(oi.getMenuItem().getName()).append(" x").append(oi.getQuantity());
            if (i < items.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public int getTotalQuantity() {
        int totalQty = 0;
        for (OrderItem oi : items) {
            totalQty += oi.getQuantity();
        }
        return totalQty;
    }
}