package canteen_project.model;

import java.util.ArrayList;
import java.util.List;

public class CanteenStaff extends User {
    private String shift;

    public CanteenStaff(String userId, String name, String shift) {
        super(userId, name);
        this.shift = shift;
    }

    public String getShift() {
        return shift;
    }

    public List<Order> viewPendingOrders() {
        List<Order> pending = new ArrayList<>();
        for (Order order : CanteenStorage.getOrders()) {
            if (!order.getStatus().equals("Ready")) {
                pending.add(order);
            }
        }
        return pending;
    }

    public void updateOrderStatus(String orderId, String status) {
        CanteenStorage.updateOrderStatus(orderId, status);
    }

    public void addMenuItem(MenuItem item) {
        CanteenStorage.addMenuItem(item);
    }

    public void removeMenuItem(int index) {
        CanteenStorage.removeMenuItem(index);
    }

    public void toggleMenuAvailability(int index) {
        CanteenStorage.toggleMenuAvailability(index);
    }
}