package adminController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.connect;
import java.io.IOException;
import main.Main;

public class loginController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    public void sqlLogin() throws IOException {
        String userName = userNameField.getText();
        String password = passwordField.getText();

        // Kết nối đến cơ sở dữ liệu
        Connection connection = connect.getConnection();

        if (connection != null) {
            try {
                // Truy vấn kiểm tra tên người dùng và mật khẩu
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    // Đăng nhập thành công
                    System.out.println("Đăng nhập thành công với : " + role);

                    // Sau khi hiển thị thông báo thành công, mở trang home.fxml hoặc
                    if ("user".equals(role)) {
                        Main.setRoot("/web/home.fxml");

                    } else if ("admin".equals(role)) {
                        Main.setRoot("/admin/addProductName.fxml");

                    }
                } else {
                    // Đăng nhập thất bại
                    System.out.println("Login failed");
                    showAlert("Login failed");
                }

                // Đóng kết nối
                connect.closeConnection(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void getRegistration() throws IOException {
        Main.setRoot("/admin/createAccount.fxml");
    }
}
