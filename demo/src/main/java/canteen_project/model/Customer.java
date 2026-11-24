package canteen_project.model;

import java.util.List;

public class Customer extends User {
    private String studentId;

    public Customer(String userId, String name, String studentId) {
        super(userId, name);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public List<MenuItem> viewMenu() {
        return CanteenStorage.getMenuItems();
    }

    public Order placeOrder(List<OrderItem> items) {
        Order order = new Order(CanteenStorage.generateOrderId());
        for (OrderItem item : items) {
            order.addItem(item);
        }
        order.computeTotal();
        CanteenStorage.addOrder(order);
        return order;
    }
}