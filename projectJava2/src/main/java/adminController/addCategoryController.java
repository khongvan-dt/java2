package adminController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class addCategoryController {

	@FXML
	private TextField categoryNameField;

	public void moreCategory() {
		String CategoryName = categoryNameField.getText();

		if (CategoryName.isEmpty()) {
			showAlert("Please fill in all fields.");
			return;
		}
		String insertSQL = "INSERT INTO category (categoryName) VALUES (?)";
		try (Connection connection = connect.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

			preparedStatement.setString(1, CategoryName);

			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println(" add category successfully!");

				// Thành công: Hiển thị thông báo thành công
				showSuccessAlert("Category added successfully!");
				
				// Xóa nội dung trên trường nhập liệu sau khi thêm thành công
				categoryNameField.clear();
			} else {
				showAlert("Failed to add category.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
//insert thành công sẽ hiện 
	private void showSuccessAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
// insert không thành công sẽ hiện  
	private void showAlert(String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	
// các hàm gọi giao diện
	public void getFromAddcategory() {
		callLink link = new callLink();
		link.getFormAddCategory();
	}

	public void getFromAddProduct() {
		callLink link = new callLink();
		link.getFormAddProduct();
	}

	public void getFromfromAddSupplier() {
		callLink link = new callLink();
		link.getFormfromAddSupplier();
	}
	public void getFromfromMoreProductName() {
	    callLink link = new callLink();
	    link.getProductName();
	}


}
