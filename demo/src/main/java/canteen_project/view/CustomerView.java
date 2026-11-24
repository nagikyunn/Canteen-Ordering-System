package canteen_project.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class CustomerView extends JFrame {
    private DefaultTableModel menuModel;
    private JTable menuTable;
    private DefaultTableModel cartModel;
    private JTable cartTable;

    private JTextField quantityField;
    private JLabel totalLabel;

    private JButton addToOrderBtn;
    private JButton removeItemBtn;
    private JButton backBtn;
    private JButton viewStatusBtn;
    private JButton placeOrderBtn;

    public CustomerView() {
        setTitle("Customer Menu - Canteen Ordering System");
        setSize(760, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Canteen Menu & Ordering");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        topPanel.add(headerLabel);

        JSeparator divider = new JSeparator(SwingConstants.HORIZONTAL);
        divider.setForeground(Color.GRAY);
        topPanel.add(divider);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 12, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(new JLabel("Available Menu Items (Select an item):"), BorderLayout.NORTH);

        String[] menuColumns = {"ID", "Menu Item", "Category", "Price (PHP)"};
        menuModel = new DefaultTableModel(menuColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(menuModel);
        leftPanel.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        JPanel addCartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityField = new JTextField("1", 4);
        addToOrderBtn = new JButton("Add to Order");
        addCartPanel.add(new JLabel("Qty:"));
        addCartPanel.add(quantityField);
        addCartPanel.add(addToOrderBtn);
        leftPanel.add(addCartPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(new JLabel("Current Order Summary:"), BorderLayout.NORTH);

        String[] cartColumns = {"Item", "Qty", "Subtotal (PHP)"};
        cartModel = new DefaultTableModel(cartColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartModel);
        rightPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel cartControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        removeItemBtn = new JButton("Remove Selected");
        totalLabel = new JLabel("Total Price: PHP 0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        cartControlPanel.add(removeItemBtn);
        cartControlPanel.add(totalLabel);
        rightPanel.add(cartControlPanel, BorderLayout.SOUTH);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        backBtn = new JButton("Back to Login");
        viewStatusBtn = new JButton("Check Order Status");
        placeOrderBtn = new JButton("Place Order");

        bottomPanel.add(backBtn);
        bottomPanel.add(viewStatusBtn);
        bottomPanel.add(placeOrderBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public DefaultTableModel getMenuModel() {
        return menuModel;
    }

    public JTable getMenuTable() {
        return menuTable;
    }

    public DefaultTableModel getCartModel() {
        return cartModel;
    }

    public JTable getCartTable() {
        return cartTable;
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setTotalText(String text) {
        totalLabel.setText(text);
    }

    public JButton getAddToOrderButton() {
        return addToOrderBtn;
    }

    public JButton getRemoveItemButton() {
        return removeItemBtn;
    }

    public JButton getBackButton() {
        return backBtn;
    }

    public JButton getViewStatusButton() {
        return viewStatusBtn;
    }

    public JButton getPlaceOrderButton() {
        return placeOrderBtn;
    }
}