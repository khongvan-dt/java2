/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

import adminController.addCategoryController.Category;
import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.Main;

/**
 *
 * @author Administrator
 */
public class editCategoryController {

    @FXML
    private TextField CategoryNameField;

    @FXML
    private Button saveButton;

    private Category selectedCategory;

    public void initData(Category Category) {

        selectedCategory = Category;
        CategoryNameField.setText(Category.getCategoryName());
    }

    @FXML
    public void UpdateCategory(ActionEvent event) throws IOException {
        String updatedSupplierName = CategoryNameField.getText().trim();

        // Cập nhật dữ liệu của nhà cung cấp đã chọn trong cơ sở dữ liệu
        if (selectedCategory != null) {
            selectedCategory.setCategoryName(updatedSupplierName);

            // Cập nhật cơ sở dữ liệu với tên nhà cung cấp mới ở đây
            String updateSQL = "UPDATE category SET categoryName = ? WHERE categoryId = ?";
            try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

                preparedStatement.setString(1, updatedSupplierName);
                preparedStatement.setInt(2, selectedCategory.getCategoryId()); // Sử dụng ID của nhà cung cấp đã chọn để xác định bản ghi cần cập nhật

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showSuccessAlert("Update successful!");

                    // Đóng cảnh "Sửa nhà cung cấp"
                    Stage stage = (Stage) CategoryNameField.getScene().getWindow();
                    stage.close();

                    Main.setRoot("/admin/addCategory.fxml");

                } else {
                    showAlert("Update failed!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void initData(addSupplierController.Supplier selectedSupplier) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
