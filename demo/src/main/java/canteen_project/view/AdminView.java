package canteen_project.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class AdminView extends JFrame {
    private JLabel headerLabel;
    private CardLayout cardLayout;
    private JPanel contentPanel;

    private JButton viewOrdersBtn;
    private JButton manageMenuBtn;
    private JButton logoutBtn;

    private DefaultTableModel ordersModel;
    private JTable ordersTable;
    private JButton refreshBtn;
    private JButton pendingBtn;
    private JButton preparingBtn;
    private JButton readyBtn;

    private DefaultTableModel menuModel;
    private JTable menuTable;
    private JTextField nameField;
    private JComboBox<String> categoryBox;
    private JTextField priceField;
    private JButton addBtn;
    private JButton toggleBtn;
    private JButton deleteBtn;

    public AdminView() {
        setTitle("Admin Dashboard - Canteen Ordering System");
        setSize(820, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        headerLabel = new JLabel("Admin Dashboard - View Orders");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(headerLabel);

        JSeparator divider = new JSeparator(SwingConstants.HORIZONTAL);
        divider.setForeground(Color.GRAY);
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        topPanel.add(divider);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        navPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        viewOrdersBtn = new JButton("View Orders");
        manageMenuBtn = new JButton("Manage Menu Items");
        logoutBtn = new JButton("Logout");

        navPanel.add(viewOrdersBtn);
        navPanel.add(manageMenuBtn);
        navPanel.add(logoutBtn);
        topPanel.add(navPanel);

        add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        contentPanel.add(createOrdersPanel(), "ORDERS");
        contentPanel.add(createMenuPanel(), "MENU");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        String[] columns = {"Order ID", "Menu Item", "Quantity", "Total Price (PHP)", "Order Status"};
        ordersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(ordersModel);
        ordersTable.setRowHeight(22);
        ordersTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        ordersTable.getColumnModel().getColumn(1).setPreferredWidth(260);
        ordersTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        ordersTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        ordersTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        panel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("Refresh");
        pendingBtn = new JButton("Mark Pending");
        preparingBtn = new JButton("Mark Preparing");
        readyBtn = new JButton("Mark Ready");

        statusPanel.add(refreshBtn);
        statusPanel.add(pendingBtn);
        statusPanel.add(preparingBtn);
        statusPanel.add(readyBtn);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        String[] columns = {"ID", "Menu Item", "Category", "Price (PHP)", "Available"};
        menuModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        menuTable = new JTable(menuModel);
        menuTable.setRowHeight(22);
        menuTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        menuTable.getColumnModel().getColumn(1).setPreferredWidth(240);
        menuTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        menuTable.getColumnModel().getColumn(3).setPreferredWidth(95);
        menuTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        panel.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        JPanel bottomControls = new JPanel(new BorderLayout(5, 5));

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nameField = new JTextField(15);
        categoryBox = new JComboBox<>(new String[]{"Meals", "Snacks", "Drinks"});
        priceField = new JTextField(6);
        addBtn = new JButton("Add Item");

        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Category:"));
        addPanel.add(categoryBox);
        addPanel.add(new JLabel("Price:"));
        addPanel.add(priceField);
        addPanel.add(addBtn);

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toggleBtn = new JButton("Toggle Availability");
        deleteBtn = new JButton("Delete Selected");

        editPanel.add(toggleBtn);
        editPanel.add(deleteBtn);

        bottomControls.add(addPanel, BorderLayout.WEST);
        bottomControls.add(editPanel, BorderLayout.SOUTH);
        panel.add(bottomControls, BorderLayout.SOUTH);

        return panel;
    }

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
        if ("ORDERS".equals(cardName)) {
            headerLabel.setText("Admin Dashboard - View Orders");
        } else if ("MENU".equals(cardName)) {
            headerLabel.setText("Admin Dashboard - Manage Menu Items");
        }
    }

    public JButton getViewOrdersButton() {
        return viewOrdersBtn;
    }

    public JButton getManageMenuButton() {
        return manageMenuBtn;
    }

    public JButton getLogoutButton() {
        return logoutBtn;
    }

    public DefaultTableModel getOrdersModel() {
        return ordersModel;
    }

    public JTable getOrdersTable() {
        return ordersTable;
    }

    public JButton getRefreshButton() {
        return refreshBtn;
    }

    public JButton getPendingButton() {
        return pendingBtn;
    }

    public JButton getPreparingButton() {
        return preparingBtn;
    }

    public JButton getReadyButton() {
        return readyBtn;
    }

    public DefaultTableModel getMenuModel() {
        return menuModel;
    }

    public JTable getMenuTable() {
        return menuTable;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JComboBox<String> getCategoryBox() {
        return categoryBox;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public JButton getAddButton() {
        return addBtn;
    }

    public JButton getToggleButton() {
        return toggleBtn;
    }

    public JButton getDeleteButton() {
        return deleteBtn;
    }
}