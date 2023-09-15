/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adminController;

import db.connect;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import main.Main;

/**
 *
 * @author Administrator
 */
public class importGoodsController {

    @FXML
    private ComboBox<String> SupplierId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {
            // Tạo một truy vấn SQL để lấy dữ liệu  từ cơ sở dữ liệu
            String selectSupplier = "SELECT supplierId , supplierName FROM supplier";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement = connection.prepareStatement(selectSupplier);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();

            // Tạo một danh sách (ObservableList) để lưu trữ tên danh mục sản phẩm
            ObservableList<String> suppliers = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên danh mục vào danh sách
            while (resultSet.next()) {
                int supplierID = resultSet.getInt("supplierId");
                String supplierName = resultSet.getString("supplierName");
                suppliers.add(supplierName);
            }

            // Đặt danh sách tên danh mục vào ComboBox để hiển thị trong giao diện người dùng
            SupplierId.setItems(suppliers);

            // Tạo một truy vấn SQL để lấy dữ liệu tên sản phẩm từ cơ sở dữ liệu
            String selectProduct = "SELECT ProductNameId, ProductName FROM ProductsName";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement2 = connection.prepareStatement(selectProduct);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet2 = preparedStatement2.executeQuery();

            // Tạo một danh sách (ObservableList) để lưu trữ tên sản phẩm
            ObservableList<String> productNames = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên sản phẩm vào danh sách
            while (resultSet2.next()) {
                int productNameId = resultSet2.getInt("ProductNameId");
                String productsName = resultSet2.getString("ProductName");
                productNames.add(productsName);
            }

            // Đặt danh sách tên sản phẩm vào ComboBox để hiển thị trong giao diện người dùng
            fieldViewProductName.setItems(productNames);

        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi kết nối hoặc truy vấn
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
}
