/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

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
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public class editProductNameController {

    @FXML
    private TextField ProductNameField;

    @FXML
    private Button saveButton;

    private moreProductNameController.ProductName selectedProductName;

    public void initData(moreProductNameController.ProductName productName) {
        selectedProductName = productName;
        ProductNameField.setText(productName.getProductName());
    }

    @FXML
    public void updatedProductName(ActionEvent event) throws IOException {
        String updatedProductName = ProductNameField.getText();

        // Cập nhật dữ liệu của tên sản phẩm đã chọn trong cơ sở dữ liệu
        if (selectedProductName != null) {
            selectedProductName.setProductName(updatedProductName);

            // Cập nhật cơ sở dữ liệu với tên sản phẩm mới ở đây
            String updateSQL = "UPDATE ProductsName SET ProductName = ? WHERE ProductNameId = ?";
            try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

                preparedStatement.setString(1, updatedProductName);
                preparedStatement.setInt(2, selectedProductName.getProductId());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showSuccessAlert("Update successful!");

                    // Đóng cửa sổ "Chỉnh sửa tên sản phẩm"
                    Stage stage = (Stage) ProductNameField.getScene().getWindow();
                    stage.close();

                    // Quay lại giao diện "Thêm Tên Sản Phẩm"
                    Main.setRoot("/admin/addProductName.fxml");

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
}
