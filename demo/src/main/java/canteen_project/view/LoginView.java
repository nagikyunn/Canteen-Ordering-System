package canteen_project.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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
        setSize(400, 320);

        panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        labelTitle = new JLabel("Canteen Ordering System");
        labelTitle.setBounds(120, 15, 200, 25);
        panel.add(labelTitle);

        labelUser = new JLabel("Username");
        labelUser.setBounds(70, 55, 80, 25);
        panel.add(labelUser);

        userText = new JTextField();
        userText.setBounds(150, 55, 150, 25);
        panel.add(userText);

        labelPass = new JLabel("Password");
        labelPass.setBounds(70, 95, 80, 25);
        panel.add(labelPass);

        passText = new JPasswordField();
        passText.setBounds(150, 95, 150, 25);
        panel.add(passText);

        loginBtn = new JButton("Staff Login");
        loginBtn.setBounds(150, 140, 150, 25);
        panel.add(loginBtn);

        customerBtn = new JButton("Order as Customer");
        customerBtn.setBounds(150, 175, 150, 25);
        panel.add(customerBtn);

        statusLabel = new JLabel("");
        statusLabel.setBounds(125, 215, 220, 25);
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