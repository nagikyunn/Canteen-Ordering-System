package canteen_project.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginView extends JFrame {
    private JPanel panel;
    private JLabel labelTitle;
    private JLabel labelUser;
    private JTextField userText;
    private JLabel labelPass;
    private JPasswordField passText;
    private JButton loginBtn;
    private JButton customerBtn;
    private JLabel statusLabel;

    public LoginView() {
        setTitle("Canteen Ordering System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);

        panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        labelTitle = new JLabel("Canteen Ordering System", SwingConstants.CENTER);
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        labelTitle.setBounds(40, 20, 340, 30);
        panel.add(labelTitle);

        labelUser = new JLabel("Username:");
        labelUser.setFont(new Font("SansSerif", Font.PLAIN, 13));
        labelUser.setBounds(65, 70, 85, 25);
        panel.add(labelUser);

        userText = new JTextField();
        userText.setBounds(155, 70, 185, 25);
        panel.add(userText);

        labelPass = new JLabel("Password:");
        labelPass.setFont(new Font("SansSerif", Font.PLAIN, 13));
        labelPass.setBounds(65, 110, 85, 25);
        panel.add(labelPass);

        passText = new JPasswordField();
        passText.setBounds(155, 110, 185, 25);
        panel.add(passText);

        loginBtn = new JButton("Staff Login");
        loginBtn.setBounds(155, 155, 185, 28);
        panel.add(loginBtn);

        customerBtn = new JButton("Order as Customer");
        customerBtn.setBounds(155, 192, 185, 28);
        panel.add(customerBtn);

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setBounds(40, 232, 340, 25);
        panel.add(statusLabel);

        setLocationRelativeTo(null);
    }

    public String getUsername() {
        return userText.getText().trim();
    }

    public String getPassword() {
        return new String(passText.getPassword());
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public JButton getLoginButton() {
        return loginBtn;
    }

    public JButton getCustomerButton() {
        return customerBtn;
    }
}