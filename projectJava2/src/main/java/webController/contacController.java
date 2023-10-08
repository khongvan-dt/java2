/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package webController;

import adminController.loginController;
import java.io.IOException;
import javafx.event.ActionEvent;
import main.Main;
import models.UserSession;

/**
 *
 * @author Administrator
 */
public class contacController {

    public void redirectToLogin() throws IOException {
        Main.setRoot("/admin/login.fxml");
    }

    public void FromAddress() throws IOException {
        Main.setRoot("/web/contact.fxml");
    }

    public void getHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

    public void redirectToCart() throws IOException {
        int userId = UserSession.getInstance().getUserId();

        if (userId != 0) {
            // Nếu userId khác 0 (hoặc giá trị mặc định khác), chuyển đến trang giỏ hàng
            Main.setRoot("/web/cart.fxml");
        } else {
            // Nếu userId bằng 0 (hoặc giá trị mặc định), gọi hàm redirectToLogin()
            redirectToLogin();
        }
    }
     public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }
}
