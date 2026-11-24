package canteen_project;

import canteen_project.controller.LoginController;
import canteen_project.model.CanteenStorage;
import canteen_project.view.LoginView;

public class Main {
    public static void main(String[] args) {
        CanteenStorage.init();
        LoginView loginView = new LoginView();
        new LoginController(loginView);
        loginView.setVisible(true);
    }
}