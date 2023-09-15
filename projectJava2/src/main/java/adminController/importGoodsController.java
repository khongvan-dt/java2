/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

/**
 *
 * @author Administrator
 */
import db.connect;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import main.Main;

public class importGoodsController {

    @FXML
    private ComboBox<String> SupplierId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private TextField importQuantity;

    @FXML
    private TextField exchangeNumber;

    @FXML
    private TextField total_quantity_received;

    // Tạo một Map để lưu trữ tương ứng giữa tên và ID của Supplier
    private Map<String, Integer> supplierIdMap = new HashMap<>();

    // Tạo một Map để lưu trữ tương ứng giữa tên và ID của ProductsName
    private Map<String, Integer> productNameIdMap = new HashMap<>();

    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {
            // Tạo một truy vấn SQL để lấy dữ liệu từ cơ sở dữ liệu cho Supplier
            String selectSupplier = "SELECT supplierId, supplierName FROM supplier";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement = connection.prepareStatement(selectSupplier);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();

            // Tạo một danh sách (ObservableList) để lưu trữ tên Supplier
            ObservableList<String> suppliers = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên Supplier vào danh sách và Map tương ứng
            while (resultSet.next()) {
                int supplierID = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                suppliers.add(supplierName);
                supplierIdMap.put(supplierName, supplierID); // Lưu tên và ID vào Map
            }

            // Đặt danh sách tên Supplier vào ComboBox để hiển thị trong giao diện người dùng
            SupplierId.setItems(suppliers);

            // Tạo một truy vấn SQL để lấy dữ liệu tên sản phẩm từ cơ sở dữ liệu cho ProductsName
            String selectProduct = "SELECT ProductNameId, ProductName FROM ProductsName";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            // Tạo một danh sách (ObservableList) để lưu trữ tên ProductsName
            ObservableList<String> productNames = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên ProductsName vào danh sách và Map tương ứng
            while (resultSet2.next()) {
                int productNameId = resultSet2.getInt("ProductNameId");
                String productsName = resultSet2.getString("ProductName");
                productNames.add(productsName);
                productNameIdMap.put(productsName, productNameId); // Lưu tên và ID vào Map
            }

            // Đặt danh sách tên ProductsName vào ComboBox để hiển thị trong giao diện người dùng
            fieldViewProductName.setItems(productNames);

        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi kết nối hoặc truy vấn
        }
    }

    public void insertImportgoods() {
        String Quantity = importQuantity.getText();
        String exchange = exchangeNumber.getText();
        String totalQuantity = total_quantity_received.getText();

        String selectedSupplierName = SupplierId.getValue();
        String selectedProductName = fieldViewProductName.getValue();

        if (Quantity.isEmpty() || exchange.isEmpty() || totalQuantity.isEmpty() || selectedSupplierName == null || selectedProductName == null) {
            showAlert("Please fill in all fields and select a supplier and product.");
            return;
        }

        int supplierId = supplierIdMap.get(selectedSupplierName);
        int productNameId = productNameIdMap.get(selectedProductName);

        // Lấy ngày hiện tại
        java.util.Date currentDate = new java.util.Date();

        // Chuyển đổi thành java.sql.Date (loại bỏ thông tin về giờ)
        Date dateNew = new Date(currentDate.getTime());

        String insertSQL = "INSERT INTO importgoods (ProductNameId, supplier_id, import_date, quantity_imported, quantity_returned, total_quantity_received) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, productNameId);
            preparedStatement.setInt(2, supplierId);
            preparedStatement.setDate(3, dateNew);
            preparedStatement.setString(4, Quantity);
            preparedStatement.setString(5, exchange);
            preparedStatement.setString(6, totalQuantity);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add successfully!");

                // Thành công: Hiển thị thông báo thành công
                showSuccessAlert("Added successfully!");

                // Xóa nội dung trên trường nhập liệu sau khi thêm thành công
                importQuantity.clear();
                exchangeNumber.clear();
                total_quantity_received.clear();
                SupplierId.getSelectionModel().clearSelection();
                fieldViewProductName.getSelectionModel().clearSelection();
            } else {
                showAlert("Failed to add.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
