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
import javafx.event.ActionEvent;
import main.Main;
import models.UserSession;
import org.mindrot.jbcrypt.BCrypt;

public class loginController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    // Biến toàn cục để theo dõi trạng thái đăng nhập
    private boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    // Biến lưu trữ ID và tên người dùng sau khi đăng nhập thành công
    public int loggedInUserId;
    public String loggedInUsername;

    public void sqlLogin() throws IOException {
        String userName = userNameField.getText().trim();
        String password = passwordField.getText().trim();

        // Kết nối đến cơ sở dữ liệu
        Connection connection = connect.getConnection();

        if (connection != null) {
            try {
                // Truy vấn kiểm tra tên người dùng và mật khẩu
                String sql = "SELECT * FROM users WHERE username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userName);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password"); // Trích xuất mật khẩu đã mã hóa từ DB
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        int userId = resultSet.getInt("userId");
                        String role = resultSet.getString("role");
                        // Đăng nhập thành công
                        System.out.println("Đăng nhập thành công với : " + role);

                        // Lưu trạng thái đăng nhập, ID và tên người dùng
                        isLoggedIn = true;
                        loggedInUserId = userId;
                        loggedInUsername = userName;
                        UserSession.getInstance().setUserId(userId);

                        // Sau khi hiển thị thông báo thành công, mở trang home.fxml hoặc
                        if ("user".equals(role)) {
                            Main.setRoot("/web/home.fxml");
                        } else if ("admin".equals(role)) {
                            Main.setRoot("/admin/addCategory.fxml");
                        }
                    } else {
                        // Mật khẩu sai
                        System.out.println("Login failed");
                        showAlert("Login failed");
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

    @FXML
    private void formHome() throws IOException {
        Main.setRoot("/web/home.fxml");
    }

    // Phương thức để kiểm tra trạng thái đăng nhập từ bất kỳ đâu trong ứng dụng
    public int getLoggedInUserId() {
        return loggedInUserId;
    }

    // Phương thức để lấy tên người dùng sau khi đăng nhập
    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void handleLogout() throws IOException {
        isLoggedIn = false;
        loggedInUserId = -1;
        loggedInUsername = null;

        // Xóa thông tin đăng nhập trong phiên làm việc của người dùng (nếu bạn đang sử dụng phiên làm việc)
        UserSession.getInstance().setUserId(-1);

        // Tải lại màn hình đăng nhập
        Main.setRoot("/admin/login.fxml");
    }

}
