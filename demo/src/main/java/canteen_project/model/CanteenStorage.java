package canteen_project.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CanteenStorage {
    private static final String DB_URL = "jdbc:sqlite:canteen.db";
    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                user_id TEXT PRIMARY KEY NOT NULL,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                name TEXT NOT NULL,
                role TEXT NOT NULL
            );
            """;

        String createMenuItems = """
            CREATE TABLE IF NOT EXISTS menu_items (
                item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                price REAL NOT NULL,
                is_available INTEGER NOT NULL DEFAULT 1
            );
            """;

        String createOrders = """
            CREATE TABLE IF NOT EXISTS orders (
                order_id TEXT PRIMARY KEY NOT NULL,
                user_id TEXT,
                order_date TEXT NOT NULL,
                status TEXT NOT NULL,
                total_price REAL NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(user_id)
            );
            """;

        String createOrderItems = """
            CREATE TABLE IF NOT EXISTS order_items (
                order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
                order_id TEXT NOT NULL,
                item_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                subtotal REAL NOT NULL,
                FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
                FOREIGN KEY (item_id) REFERENCES menu_items(item_id) ON DELETE CASCADE
            );
            """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createMenuItems);
            stmt.execute(createOrders);
            stmt.execute(createOrderItems);

            try (ResultSet rsUsers = stmt.executeQuery("SELECT COUNT(*) FROM users")) {
                if (rsUsers.next() && rsUsers.getInt(1) == 0) {
                    stmt.executeUpdate("""
                        INSERT INTO users (user_id, username, password, name, role)
                        VALUES ('STAFF-01', 'admin', 'admin123', 'Canteen Admin', 'STAFF');
                        """);
                }
            }

            try (ResultSet rsMenu = stmt.executeQuery("SELECT COUNT(*) FROM menu_items")) {
                if (rsMenu.next() && rsMenu.getInt(1) == 0) {
                    stmt.executeUpdate("""
                        INSERT INTO menu_items (name, category, price, is_available) VALUES
                        ('Chicken Adobo with Rice', 'Meals', 65.00, 1),
                        ('Pork Sisig with Rice', 'Meals', 70.00, 1),
                        ('Pancit Canton', 'Snacks', 35.00, 1),
                        ('Bottled Water', 'Drinks', 20.00, 1);
                        """);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User authenticateUser(String username, String password) {
        init();
        String sql = "SELECT user_id, name, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String userId = rs.getString("user_id");
                    String name = rs.getString("name");
                    String role = rs.getString("role");
                    if ("STAFF".equalsIgnoreCase(role)) {
                        return new CanteenStaff(userId, name, "Day Shift");
                    }
                    return new Customer(userId, name, userId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<MenuItem> getMenuItems() {
        init();
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT item_id, name, category, price, is_available FROM menu_items ORDER BY item_id ASC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new MenuItem(
                    rs.getInt("item_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("is_available") == 1
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void addMenuItem(MenuItem item) {
        init();
        String sql = "INSERT INTO menu_items (name, category, price, is_available) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setInt(4, item.isAvailable() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeMenuItem(int index) {
        init();
        List<MenuItem> current = getMenuItems();
        if (index < 0 || index >= current.size()) {
            return;
        }
        int itemId = current.get(index).getItemId();
        String sql = "DELETE FROM menu_items WHERE item_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void toggleMenuAvailability(int index) {
        init();
        List<MenuItem> current = getMenuItems();
        if (index < 0 || index >= current.size()) {
            return;
        }
        MenuItem target = current.get(index);
        String sql = "UPDATE menu_items SET is_available = ? WHERE item_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, target.isAvailable() ? 0 : 1);
            pstmt.setInt(2, target.getItemId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getNextMenuId() {
        init();
        String sql = "SELECT COALESCE(MAX(item_id), 0) + 1 FROM menu_items";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static String generateOrderId() {
        init();
        int maxNumber = 100;
        String sql = "SELECT order_id FROM orders";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("order_id");
                if (id != null && id.startsWith("ORD-")) {
                    try {
                        int num = Integer.parseInt(id.substring(4));
                        if (num > maxNumber) {
                            maxNumber = num;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ORD-" + (maxNumber + 1);
    }

    public static void addOrder(Order order) {
        init();
        String insertOrderSql = "INSERT INTO orders (order_id, user_id, order_date, status, total_price) VALUES (?, NULL, ?, ?, ?)";
        String insertItemSql = "INSERT INTO order_items (order_id, item_id, quantity, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql);
                 PreparedStatement itemStmt = conn.prepareStatement(insertItemSql)) {

                orderStmt.setString(1, order.getOrderId());
                orderStmt.setString(2, order.getOrderDate());
                orderStmt.setString(3, order.getStatus());
                orderStmt.setDouble(4, order.getTotalPrice());
                orderStmt.executeUpdate();

                for (OrderItem oi : order.getItems()) {
                    int itemId = oi.getMenuItem().getItemId();
                    if (itemId <= 0) {
                        itemId = findItemIdByName(conn, oi.getMenuItem().getName());
                    }
                    itemStmt.setString(1, order.getOrderId());
                    itemStmt.setInt(2, itemId);
                    itemStmt.setInt(3, oi.getQuantity());
                    itemStmt.setDouble(4, oi.computeSubtotal());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> getOrders() {
        init();
        List<Order> orderList = new ArrayList<>();
        String orderSql = "SELECT order_id, order_date, status, total_price FROM orders ORDER BY rowid ASC";
        String itemsSql = """
            SELECT oi.quantity, oi.subtotal, mi.item_id, mi.name, mi.category, mi.price, mi.is_available
            FROM order_items oi
            JOIN menu_items mi ON oi.item_id = mi.item_id
            WHERE oi.order_id = ?
            """;

        try (Connection conn = getConnection();
             Statement orderStmt = conn.createStatement();
             ResultSet rsOrders = orderStmt.executeQuery(orderSql);
             PreparedStatement itemStmt = conn.prepareStatement(itemsSql)) {

            while (rsOrders.next()) {
                String orderId = rsOrders.getString("order_id");
                String orderDate = rsOrders.getString("order_date");
                String status = rsOrders.getString("status");
                double totalPrice = rsOrders.getDouble("total_price");

                List<OrderItem> loadedItems = new ArrayList<>();
                itemStmt.setString(1, orderId);
                try (ResultSet rsItems = itemStmt.executeQuery()) {
                    while (rsItems.next()) {
                        MenuItem menuItem = new MenuItem(
                            rsItems.getInt("item_id"),
                            rsItems.getString("name"),
                            rsItems.getString("category"),
                            rsItems.getDouble("price"),
                            rsItems.getInt("is_available") == 1
                        );
                        loadedItems.add(new OrderItem(menuItem, rsItems.getInt("quantity")));
                    }
                }

                orderList.add(new Order(orderId, orderDate, status, totalPrice, loadedItems));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public static void updateOrderStatus(String orderId, String newStatus) {
        init();
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int findItemIdByName(Connection conn, String name) throws SQLException {
        String sql = "SELECT item_id FROM menu_items WHERE name = ? LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("item_id");
                }
            }
        }
        return 1;
    }
}