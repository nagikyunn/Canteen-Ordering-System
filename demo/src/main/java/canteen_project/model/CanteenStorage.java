package canteen_project.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CanteenStorage {
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final List<MenuItem> menuItems = new ArrayList<>();
    private static final List<Order> orders = new ArrayList<>();
    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        loadMenu();
        if (menuItems.isEmpty()) {
            menuItems.add(new MenuItem(1, "Chicken Adobo with Rice", "Meals", 65.00, true));
            menuItems.add(new MenuItem(2, "Pork Sisig with Rice", "Meals", 70.00, true));
            menuItems.add(new MenuItem(3, "Pancit Canton", "Snacks", 35.00, true));
            menuItems.add(new MenuItem(4, "Bottled Water", "Drinks", 20.00, true));
            saveMenu();
        }
        loadOrders();
    }

    public static List<MenuItem> getMenuItems() {
        init();
        return menuItems;
    }

    public static List<Order> getOrders() {
        init();
        return orders;
    }

    public static void addMenuItem(MenuItem item) {
        init();
        menuItems.add(item);
        saveMenu();
    }

    public static void removeMenuItem(int index) {
        init();
        if (index >= 0 && index < menuItems.size()) {
            menuItems.remove(index);
            saveMenu();
        }
    }

    public static void toggleMenuAvailability(int index) {
        init();
        if (index >= 0 && index < menuItems.size()) {
            MenuItem item = menuItems.get(index);
            item.setAvailable(!item.isAvailable());
            saveMenu();
        }
    }

    public static int getNextMenuId() {
        init();
        int maxId = 0;
        for (MenuItem item : menuItems) {
            if (item.getItemId() > maxId) {
                maxId = item.getItemId();
            }
        }
        return maxId + 1;
    }

    public static String generateOrderId() {
        init();
        return "ORD-" + (101 + orders.size());
    }

    public static void addOrder(Order order) {
        init();
        orders.add(order);
        saveOrders();
    }

    public static void updateOrderStatus(String orderId, String newStatus) {
        init();
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(newStatus);
                break;
            }
        }
        saveOrders();
    }

    public static void saveMenu() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MENU_FILE))) {
            for (MenuItem item : menuItems) {
                writer.println(
                    item.getItemId() + "|" +
                    item.getName() + "|" +
                    item.getCategory() + "|" +
                    item.getPrice() + "|" +
                    item.isAvailable()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMenu() {
        menuItems.clear();
        File file = new File(MENU_FILE);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String category = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    boolean available = Boolean.parseBoolean(parts[4]);
                    menuItems.add(new MenuItem(id, name, category, price, available));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOrders() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orders) {
                StringBuilder itemsData = new StringBuilder();
                List<OrderItem> list = order.getItems();
                for (int i = 0; i < list.size(); i++) {
                    OrderItem oi = list.get(i);
                    itemsData.append(oi.getMenuItem().getName())
                             .append(":")
                             .append(oi.getQuantity())
                             .append(":")
                             .append(oi.getMenuItem().getPrice());
                    if (i < list.size() - 1) {
                        itemsData.append(";");
                    }
                }
                writer.println(
                    order.getOrderId() + "|" +
                    order.getOrderDate() + "|" +
                    order.getStatus() + "|" +
                    order.getTotalPrice() + "|" +
                    itemsData
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadOrders() {
        orders.clear();
        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            return;
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String orderId = parts[0];
                    String orderDate = parts[1];
                    String status = parts[2];
                    double totalPrice = Double.parseDouble(parts[3]);
                    List<OrderItem> loadedItems = new ArrayList<>();
                    String[] itemTokens = parts[4].split(";");
                    for (String token : itemTokens) {
                        String[] itemParts = token.split(":");
                        if (itemParts.length == 3) {
                            String itemName = itemParts[0];
                            int qty = Integer.parseInt(itemParts[1]);
                            double unitPrice = Double.parseDouble(itemParts[2]);
                            MenuItem tempItem = new MenuItem(0, itemName, "Menu", unitPrice, true);
                            loadedItems.add(new OrderItem(tempItem, qty));
                        }
                    }
                    orders.add(new Order(orderId, orderDate, status, totalPrice, loadedItems));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}