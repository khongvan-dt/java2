package adminController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;
import db.connect;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import org.mindrot.jbcrypt.BCrypt;

import javafx.stage.Stage;
import main.Main;

public class RegistrationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    public void registerUser() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid email format.");
            return;
        }

        // Kiểm tra xem tên tài khoản đã tồn tại hay không
        if (isUsernameExists(username)) {
            showAlert("Username already exists.");
            return;
        }

        // Kiểm tra xem email đã tồn tại hay không
        if (isEmailExists(email)) {
            showAlert("Email already exists.");
            return;
        }

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String insertSQL = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'user')";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword); // Lưu mật khẩu đã được mã hóa
            preparedStatement.setString(3, email);

            preparedStatement.executeUpdate();

            System.out.println("User registered successfully!");

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
            getLogin();
            // Hiển thị thông báo thành công
            showSuccess("User registered successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getLogin() throws IOException {
        Main.setRoot("/admin/login.fxml");
    }

    private void showRegistrationForm() throws IOException {
        Main.setRoot("/admin/createAccount.fxml");
    }

    // Kiểm tra xem tên tài khoản đã tồn tại hay chưa
    private boolean isUsernameExists(String username) {
        // Dòng này định nghĩa một truy vấn SQL để kiểm tra xem tên người dùng đã tồn
        // tại hay chưa
        String checkUsername = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = connect.getConnection(); // kết nối db
                 PreparedStatement preparedStatement = connection.prepareStatement(checkUsername)) {// so sánh chuỗi
            // nhập với user
            // đã nhập
            // Đặt giá trị của tham số ? trong truy vấn SQL bằng tên người dùng được truyền
            // vào
            preparedStatement.setString(1, username);

            // Thực hiện truy vấn SQL và trả về kết quả (danh sách dòng từ bảng `users`)
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            // Xử lý lỗi nếu có
            e.printStackTrace();
        }

        // Trả về false nếu có lỗi xảy ra hoặc không có dòng dữ liệu nào trùng khớp
        return false;
    }

    // Kiểm tra xem email đã tồn tại hay chưa
    private boolean isEmailExists(String email) {
        String checkEmailSQL = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(checkEmailSQL)) {
            preparedStatement.setString(1, email);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) throws IOException {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        Main.setRoot("login.fxml");
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    // Phương thức để lấy tên người dùng sau khi đăng nhập

}
