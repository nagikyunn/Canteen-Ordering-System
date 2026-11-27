package canteen_project.controller;

import javax.swing.JOptionPane;

import canteen_project.model.CanteenStaff;
import canteen_project.model.CanteenStorage;
import canteen_project.model.MenuItem;
import canteen_project.model.Order;
import canteen_project.view.AdminView;
import canteen_project.view.LoginView;

public class AdminController {
    private AdminView view;
    private CanteenStaff staff;

    public AdminController(AdminView view, String adminUsername) {
        this.view = view;
        this.staff = new CanteenStaff("STAFF-01", adminUsername, "Day Shift");

        refreshOrdersTable();
        refreshMenuTable();
        view.showCard("ORDERS");
        initListeners();
    }

    private void initListeners() {
        view.getViewOrdersButton().addActionListener(e -> {
            refreshOrdersTable();
            view.showCard("ORDERS");
        });

        view.getManageMenuButton().addActionListener(e -> {
            refreshMenuTable();
            view.showCard("MENU");
        });

        view.getLogoutButton().addActionListener(e -> logout());

        view.getRefreshButton().addActionListener(e -> refreshOrdersTable());
        view.getPendingButton().addActionListener(e -> updateSelectedOrderStatus("Pending"));
        view.getPreparingButton().addActionListener(e -> updateSelectedOrderStatus("Preparing"));
        view.getReadyButton().addActionListener(e -> updateSelectedOrderStatus("Ready"));

        view.getAddButton().addActionListener(e -> addMenuItem());
        view.getToggleButton().addActionListener(e -> toggleAvailability());
        view.getDeleteButton().addActionListener(e -> deleteMenuItem());
    }

    private void refreshOrdersTable() {
        view.getOrdersModel().setRowCount(0);
        for (Order order : CanteenStorage.getOrders()) {
            view.getOrdersModel().addRow(new Object[]{
                order.getOrderId(),
                order.getItemsSummary(),
                order.getTotalQuantity(),
                String.format("%.2f", order.getTotalPrice()),
                order.getStatus()
            });
        }
    }

    private void refreshMenuTable() {
        view.getMenuModel().setRowCount(0);
        for (MenuItem item : CanteenStorage.getMenuItems()) {
            view.getMenuModel().addRow(new Object[]{
                item.getItemId(),
                item.getName(),
                item.getCategory(),
                String.format("%.2f", item.getPrice()),
                item.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void updateSelectedOrderStatus(String newStatus) {
        int selectedRow = view.getOrdersTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Select an order first.");
            return;
        }
        String orderId = (String) view.getOrdersModel().getValueAt(selectedRow, 0);
        staff.updateOrderStatus(orderId, newStatus);
        refreshOrdersTable();
    }

    private void addMenuItem() {
        String name = view.getNameField().getText().trim();
        String category = (String) view.getCategoryBox().getSelectedItem();
        String priceText = view.getPriceField().getText().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Enter both item name and price.");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                JOptionPane.showMessageDialog(view, "Price must be greater than zero.");
                return;
            }
            int newId = CanteenStorage.getNextMenuId();
            staff.addMenuItem(new MenuItem(newId, name, category, price, true));
            refreshMenuTable();
            view.getNameField().setText("");
            view.getPriceField().setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Enter a valid numeric price.");
        }
    }

    private void toggleAvailability() {
        int row = view.getMenuTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Select a menu item first.");
            return;
        }
        staff.toggleMenuAvailability(row);
        refreshMenuTable();
    }

    private void deleteMenuItem() {
        int row = view.getMenuTable().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Select a menu item first.");
            return;
        }
        staff.removeMenuItem(row);
        refreshMenuTable();
    }

    private void logout() {
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
        view.dispose();
    }
}