package canteen_project.controller;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import canteen_project.model.CanteenStorage;
import canteen_project.model.Customer;
import canteen_project.model.MenuItem;
import canteen_project.model.Order;
import canteen_project.model.OrderItem;
import canteen_project.view.CustomerView;
import canteen_project.view.LoginView;

public class CustomerController {
    private CustomerView view;
    private Customer customer;
    private List<MenuItem> availableItems;
    private List<OrderItem> cartItems;

    public CustomerController(CustomerView view) {
        this.view = view;
        this.customer = new Customer("CUST-01", "Student Customer", "24-1001-231");
        this.availableItems = new ArrayList<>();
        this.cartItems = new ArrayList<>();

        loadAvailableMenu();
        initListeners();
    }

    private void initListeners() {
        view.getAddToOrderButton().addActionListener(e -> addSelectedToCart());
        view.getRemoveItemButton().addActionListener(e -> removeSelectedFromCart());
        view.getPlaceOrderButton().addActionListener(e -> submitOrder());
        view.getViewStatusButton().addActionListener(e -> showOrderStatuses());
        view.getBackButton().addActionListener(e -> goBackToLogin());
    }

    private void loadAvailableMenu() {
        availableItems.clear();
        view.getMenuModel().setRowCount(0);
        for (MenuItem item : customer.viewMenu()) {
            if (item.isAvailable()) {
                availableItems.add(item);
                view.getMenuModel().addRow(new Object[]{
                    item.getItemId(),
                    item.getName(),
                    item.getCategory(),
                    String.format("%.2f", item.getPrice())
                });
            }
        }
    }

    private void addSelectedToCart() {
        int selectedRow = view.getMenuTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Select a menu item first.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(view.getQuantityField().getText().trim());
            if (qty <= 0) {
                JOptionPane.showMessageDialog(view, "Quantity must be at least 1.");
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Enter a valid number for quantity.");
            return;
        }

        MenuItem selectedMenu = availableItems.get(selectedRow);
        boolean found = false;
        for (int i = 0; i < cartItems.size(); i++) {
            OrderItem existing = cartItems.get(i);
            if (existing.getMenuItem().getItemId() == selectedMenu.getItemId()) {
                int newQty = existing.getQuantity() + qty;
                cartItems.set(i, new OrderItem(selectedMenu, newQty));
                found = true;
                break;
            }
        }
        if (!found) {
            cartItems.add(new OrderItem(selectedMenu, qty));
        }

        view.getQuantityField().setText("1");
        refreshCart();
    }

    private void removeSelectedFromCart() {
        int selectedRow = view.getCartTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Select an item in your order to remove.");
            return;
        }
        cartItems.remove(selectedRow);
        refreshCart();
    }

    private void refreshCart() {
        view.getCartModel().setRowCount(0);
        double total = 0.0;
        for (OrderItem item : cartItems) {
            double subtotal = item.computeSubtotal();
            total += subtotal;
            view.getCartModel().addRow(new Object[]{
                item.getMenuItem().getName(),
                item.getQuantity(),
                String.format("%.2f", subtotal)
            });
        }
        view.setTotalText("Total Price: PHP " + String.format("%.2f", total));
    }

    private void submitOrder() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Add at least one item before placing an order.");
            return;
        }
        Order placedOrder = customer.placeOrder(new ArrayList<>(cartItems));
        JOptionPane.showMessageDialog(
            view,
            "Order Placed Successfully!\n" +
            "Order ID: " + placedOrder.getOrderId() + "\n" +
            "Items: " + placedOrder.getItemsSummary() + "\n" +
            "Total Price: PHP " + String.format("%.2f", placedOrder.getTotalPrice()) + "\n" +
            "Order Status: " + placedOrder.getStatus()
        );
        cartItems.clear();
        refreshCart();
    }

    private void showOrderStatuses() {
        List<Order> allOrders = CanteenStorage.getOrders();
        if (allOrders.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No orders have been placed yet.");
            return;
        }
        StringBuilder sb = new StringBuilder("Recent Orders:\n\n");
        for (Order o : allOrders) {
            sb.append(o.getOrderId())
              .append(" | ")
              .append(o.getItemsSummary())
              .append(" | Total: PHP ")
              .append(String.format("%.2f", o.getTotalPrice()))
              .append(" | Status: ")
              .append(o.getStatus())
              .append("\n");
        }
        JOptionPane.showMessageDialog(view, sb.toString());
    }

    private void goBackToLogin() {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
        view.dispose();
    }
}