package adminController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.connect;

public class loginController extends linkA {

	@FXML
	private TextField userNameField;

	@FXML
	private PasswordField passwordField;

	public void sqlLogin() {
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
					// addcategory.fxml tùy theo vai trò
					if ("user".equals(role)) {
						openHomePage();
					} else if ("admin".equals(role)) {
						getFromfromMoreProductName();
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
	private void getRegistration() {
		// Tạo một Stage mới cho form đăng nhập
		callLink link = new callLink();
		link.getLogin();
	}

	private void openHomePage() {
		// Mở trang home.fxml
		callLink link = new callLink();
		link.getHome();
	}

	public void getFromfromMoreProductName() {
		callLink link = new callLink();
		link.getProductName();
	}
}
