package adminController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.connect;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.Main;

public class addProductController {


    @FXML
    private TextField fieldViewProductName;
    @FXML
    private TextField fieldViewProductPrice;
    @FXML
    private TextField fieldViewProductCategoryId;
    @FXML
    private TextField fieldViewProductDescriptions;

    public void moreProduct() {

        String ProductName = fieldViewProductName.getText();
        String ProductPrice = fieldViewProductPrice.getText();
        String Category = fieldViewProductCategoryId.getText();
        String Description = fieldViewProductDescriptions.getText();

        if (ProductName.isEmpty()) {
            showAlert("Please Enter enough ProductName.");
            return;
        }

        if (ProductPrice.isEmpty()) {
            showAlert("Please Enter enough ProductPrice.");
            return;
        }
        if (Category.isEmpty()) {
            showAlert("Please Enter enough Category.");
            return;
        }
        if (Description.isEmpty()) {
            showAlert("Please Enter enough Description .");
            return;
        }

        String insertSQL = "INSERT INTO product (categoryId,ProductNameId,productImportPrice,price,	img,Description) VALUES (?,?,?,?,?,?)";
        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
// chưa xg 
            preparedStatement.setString(1, ProductName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(" add product successfully!");

                // Thành công: Hiển thị thông báo thành công
                showSuccessAlert("product added successfully!");

                // Xóa nội dung trên trường nhập liệu sau khi thêm thành công
                fieldViewProductName.clear();
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

    // insert không thành công sẽ hiện
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // các hàm gọi giao diện
    public void getFromAddcategory() throws IOException {
//        linka.openScene("/admin/addCategory.fxml");
        Main.setRoot("/admin/addCategory.fxml");

    }

    public void getFromAddProduct() throws IOException {
//        linka.openScene("/admin/addProduct.fxml");
        Main.setRoot("/admin/addProduct.fxml");

    }

    public void getFromfromAddSupplier() throws IOException {
//        linka.openScene("/admin/addSupplier.fxml");
        Main.setRoot("/admin/addSupplier.fxml");

    }

    public void getFromfromMoreProductName() throws IOException {
//        linka.openScene("/admin/addProductName.fxml");
        Main.setRoot("/admin/addProductName.fxml");

    }
}
