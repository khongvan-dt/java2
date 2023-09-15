package adminController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import main.Main;

public class addSupplierController {

    @FXML
    private TextField SupplierNameField;

    public void moreSupplier() {
        String SupplierName = SupplierNameField.getText();

        if (SupplierName.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        String insertSQL = "INSERT INTO supplier (supplierName) VALUES (?)";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, SupplierName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(" add category successfully!");

                // Thành công: Hiển thị thông báo thành công
                showSuccessAlert("Supplier added successfully!");

                // Xóa nội dung trên trường nhập liệu sau khi thêm thành công
                SupplierNameField.clear();
            } else {
                showAlert("Failed to add category.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // insert thành công sẽ hiện
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // các hàm gọi giao diện
    public void getFromAddcategory() throws IOException {
        Main.setRoot("/admin/addCategory.fxml");

    }

    public void getFromAddProduct() throws IOException {
        Main.setRoot("/admin/addProduct.fxml");

    }

    public void getFromfromAddSupplier() throws IOException {
        Main.setRoot("/admin/addSupplier.fxml");

    }

    public void getFromfromMoreProductName() throws IOException {
        Main.setRoot("/admin/addProductName.fxml");

    }
    public void getFromImportGoods() throws IOException {
        Main.setRoot("/admin/importGoods.fxml");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }
}
