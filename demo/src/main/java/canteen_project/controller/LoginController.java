package canteen_project.controller;

import canteen_project.view.AdminView;
import canteen_project.view.CustomerView;
import canteen_project.view.LoginView;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        view.getLoginButton().addActionListener(e -> handleLogin());
        view.getCustomerButton().addActionListener(e -> openCustomerView());
    }

    private void handleLogin() {
        String username = view.getUsername();
        String password = view.getPassword();

        if (username.equals("admin") && password.equals("admin123")) {
            view.setStatusMessage("");
            AdminView adminView = new AdminView();
            new AdminController(adminView, username);
            adminView.setVisible(true);
            view.dispose();
        } else if (username.equals("customer") && password.equals("cust123")) {
            view.setStatusMessage("");
            openCustomerView();
        } else {
            view.setStatusMessage("Login Failed, Try again!");
        }
    }

    private void openCustomerView() {
        CustomerView customerView = new CustomerView();
        new CustomerController(customerView);
        customerView.setVisible(true);
        view.dispose();
    }
}