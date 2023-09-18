package adminController;

import com.mysql.cj.conf.IntegerProperty;
import com.mysql.cj.conf.StringProperty;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.util.StringConverter;
import db.connect;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import jdk.jfr.Category;
import main.Main;

public class addProductController extends Application {

    @FXML
    private ComboBox<String> fieldViewProductCategoryId;

    @FXML
    private ComboBox<String> fieldViewProductName;

    @FXML
    private void initialize() {
        try (Connection connection = connect.getConnection()) {
            // Tạo một truy vấn SQL để lấy dữ liệu danh mục sản phẩm từ cơ sở dữ liệu
            String selectCategory = "SELECT categoryId, categoryName FROM category";

            // Tạo một PreparedStatement để thực thi truy vấn SQL
            PreparedStatement preparedStatement = connection.prepareStatement(selectCategory);

            // Thực thi truy vấn và lấy kết quả
            ResultSet resultSet = preparedStatement.executeQuery();

            // Tạo một danh sách (ObservableList) để lưu trữ tên danh mục sản phẩm
            ObservableList<String> categoryNames = FXCollections.observableArrayList();

            // Duyệt qua kết quả truy vấn và thêm tên danh mục vào danh sách
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("categoryId");
                String categoryName = resultSet.getString("categoryName");
                categoryNames.add(categoryName);
            }

            // Đặt danh sách tên danh mục vào ComboBox để hiển thị trong giao diện người dùng
            fieldViewProductCategoryId.setItems(categoryNames);

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

    @FXML
    private TextField fieldViewProductPrice;

    @FXML
    private TextField fieldViewProductDescriptions;

    // Phương thức để thêm sản phẩm
    public void moreProduct(Stage primaryStage) {

        String ProductPrice = fieldViewProductPrice.getText();
        String Description = fieldViewProductDescriptions.getText();

        // Kiểm tra xem các trường nhập liệu có rỗng hay không
//        if (ProductName.isEmpty()) {
//            showAlert("Vui lòng nhập đủ tên sản phẩm.");
//            return;
//        }
        if (ProductPrice.isEmpty()) {
            showAlert("Vui lòng nhập đủ giá sản phẩm.");
            return;
        }

        if (Description.isEmpty()) {
            showAlert("Vui lòng nhập đủ mô tả sản phẩm.");
            return;
        }

        // Chuẩn bị câu lệnh SQL để thêm sản phẩm vào cơ sở dữ liệu
        String insertSQL = "INSERT INTO product (categoryId, ProductNameId, productImportPrice, price, img, Description) VALUES (?,?,?,?,?,?)";

        try (Connection connection = connect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Đặt các tham số cho câu lệnh SQL
//            preparedStatement.setInt(1, fieldViewProductCategoryId.getValue().getCategoryId());
//            preparedStatement.setString(2, ProductName);
            preparedStatement.setString(3, ProductPrice);
            // Đặt các tham số khác ở đây

            // Thực thi câu lệnh SQL
            int rowsAffected = preparedStatement.executeUpdate();

//            if (rowsAffected > 0) {
//                System.out.println("Thêm sản phẩm thành công!");
//                showSuccessAlert("Thêm sản phẩm thành công!");
//                fieldViewProductName.clear();
//            } else {
//                showAlert("Thêm sản phẩm không thành công.");
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hiển thị thông báo thành công
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hiển thị thông báo lỗi
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Các phương thức để chuyển đổi giao diện
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

    public void getFromProductDelivery() throws IOException {
        Main.setRoot("/admin/productDelivery.fxml");
    }

    public void getFromInventory() throws IOException {
        Main.setRoot("/admin/inventory.fxml");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        // Tạo một thể hiện của lớp logOut và thiết lập tham chiếu đến loginController
        loginController logoutHandler = new loginController();
        logoutHandler.handleLogout();
    }
}
